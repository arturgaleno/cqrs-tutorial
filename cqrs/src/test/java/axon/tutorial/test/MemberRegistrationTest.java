package axon.tutorial.test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.Repository;
import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import axon.tutorial.CommandGatwayProducer;
import axon.tutorial.EventBusProducer;
import axon.tutorial.RepositoryProducer;
import axon.tutorial.accountmanagement.creditaccount.command.CreditAccount;
import axon.tutorial.accountmanagement.creditaccount.commandhandler.CreditAccountHandler;
import axon.tutorial.accountmanagement.debitaccount.command.DebitAccount;
import axon.tutorial.accountmanagement.debitaccount.commandhandler.DebitAccountHandler;
import axon.tutorial.accountmanagement.debitaccount.event.AccountDebitedEvent;
import axon.tutorial.accountmanagement.debitaccount.eventhandler.AccountDebitedEventHandler;
import axon.tutorial.accountmanagement.model.Account;
import axon.tutorial.accountmanagement.model.AccountView;
import axon.tutorial.util.ApplicationEntityManagerFactory;
import axon.tutorial.util.CdiEventListener;
import axon.tutorial.util.EventHandler;
import axon.tutorial.util.Resources;


@RunWith(Arquillian.class)
public class MemberRegistrationTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
        		.addAsLibraries(Maven.resolver().resolve("org.axonframework:axon-core:2.4.1").withoutTransitivity().asFile())
        		.addAsLibraries(Maven.resolver().resolve("org.axonframework:axon-test:2.4.1").withoutTransitivity().asFile())
        		.addAsLibraries(Maven.resolver().resolve("joda-time:joda-time:2.9.3").withoutTransitivity().asFile())
                .addClasses(
                		CreditAccount.class,
                		CreditAccountHandler.class,
                		DebitAccount.class,
                		DebitAccountHandler.class,
                		AccountDebitedEvent.class,
                		AccountDebitedEventHandler.class,
                		Account.class,
                		AccountView.class,
                		ApplicationEntityManagerFactory.class,
                		CdiEventListener.class,
                		EventHandler.class,
                		Resources.class,
                		CommandGatwayProducer.class, 
                		EventBusProducer.class,
                		RepositoryProducer.class,
                		JPATransationManager.class
                		)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
    }
    
    @Inject private CommandGateway commandGatway;
    
    @Inject private Repository<Account> repository;
    
    @Inject private UserTransaction tx;
    
    @Inject private EntityManager em;
    
    @Before
    public void beforeTest() {    	
    	UnitOfWork unitOfWork = DefaultUnitOfWork.startAndGet(new JPATransationManager(tx));
		repository.add(new Account("cc-1"));
		unitOfWork.commit();
    }
    
    @After
    public void afterTeste() {
    	UnitOfWork unitOfWork = DefaultUnitOfWork.startAndGet(new JPATransationManager(tx));
    	Account acc = repository.load("cc-1");
    	acc.markDeleted();
		unitOfWork.commit();
    }
    
    @Test
    public void debitShouldNotResultInNegativeBalance() throws Exception {
    	
    	UnitOfWork unitOfWork = DefaultUnitOfWork.startAndGet(new JPATransationManager(tx));
    	
    	double debitValue = 100.00;
    	String account = "cc-1";
    	
    	Account acc = debitAccount(account, debitValue);
    	
    	unitOfWork.commit();
    	
    	Assert.assertEquals(acc.getBalance(), 0.0000, 0.001);
    }
    
    @Test
    public void debitShouldDecreaseBalance() throws Exception {
    	
    	UnitOfWork unitOfWork = DefaultUnitOfWork.startAndGet(new JPATransationManager(tx));
    	
    	double value = 100.00;
    	String account = "cc-1";
    	
    	Account acc = creditAccount(account, value);
    	
    	acc = debitAccount(account, value);
    	
    	unitOfWork.commit();
    	
    	Assert.assertEquals(acc.getBalance(), 0.0000, 0.001);
    }
    
    @Test
    public void shouldNotCreditNegativeValue() throws Exception {
    	
    	UnitOfWork unitOfWork = DefaultUnitOfWork.startAndGet(new JPATransationManager(tx));
    	
    	double creditValue = -100.00;
    	String account = "cc-1";  	
    	
    	Account acc = creditAccount(account, creditValue);
    	
    	unitOfWork.commit();
    	
    	Assert.assertEquals(acc.getBalance(), 0.0000, 0.001);
    }
    
    @Test
    public void creditShouldIncreaseBalance() throws Exception {
    	
    	UnitOfWork unitOfWork = DefaultUnitOfWork.startAndGet(new JPATransationManager(tx));
    	
    	double creditValue = 100.00;
    	String account = "cc-1";  	
    	
    	Account acc = creditAccount(account, creditValue);
    	
    	unitOfWork.commit();
    	
    	Assert.assertEquals(acc.getBalance(), creditValue, 0.001);
    }

    @Test
    public void debitEventShouldReflectStateChange() throws Exception {
    	
    	double value = 100.00;
    	String account = "cc-1";
    	
    	UnitOfWork unitOfWork = DefaultUnitOfWork.startAndGet(new JPATransationManager(tx));
    	Account acc = creditAccount(account, value);
    	acc = debitAccount(account, value);
    	unitOfWork.commit();
    	
    	AccountView accountView = em.find(AccountView.class, acc.getIdentifier());
    	
    	Assert.assertEquals(Double.valueOf(value), accountView.getAmmount(), 0.001);
    	Assert.assertEquals(acc.getBalance(), accountView.getBalance(), 0.001);
    }
    
    
    public Account debitAccount(String account, double debitValue) {
    	
    	DebitAccount debitAccount = new DebitAccount(account, debitValue);
    	commandGatway.send(debitAccount);
    	
    	Account acc = repository.load(account);
    	
    	return acc;
    }
    
    public Account creditAccount(String account, double creditValue) {
    	
    	CreditAccount creditAccount = new CreditAccount(account, creditValue);
    	commandGatway.send(creditAccount);
    	
    	Account acc = repository.load(account);
    	
    	return acc;
    }
}
