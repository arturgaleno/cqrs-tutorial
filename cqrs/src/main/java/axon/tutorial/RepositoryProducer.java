package axon.tutorial;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.repository.GenericJpaRepository;
import org.axonframework.repository.Repository;

import axon.tutorial.accountmanagement.debitaccount.event.AccountDebitedEvent;
import axon.tutorial.accountmanagement.model.Account;
import axon.tutorial.util.CdiEventListener;
import axon.tutorial.util.EventHandler;

@ApplicationScoped
public class RepositoryProducer {
	
	@Inject
	private EventHandler<AccountDebitedEvent> accountDebitedEventHandler;
	
	@Inject
    private EntityManager em;
	
	@Inject
	private EventBus eventBus;
	
	@Produces @Default Repository<Account> produceAccountRepository() {
		
		SimpleEntityManagerProvider simpleEntityManagerProvider = new SimpleEntityManagerProvider(em);
		
		GenericJpaRepository<Account> jpaRepository = new GenericJpaRepository<Account>(simpleEntityManagerProvider, Account.class);
				
		CdiEventListener cdiEventListener = new CdiEventListener();
		
		cdiEventListener.registerEventHandler(AccountDebitedEvent.class, accountDebitedEventHandler);
		
		eventBus.subscribe(cdiEventListener);
		
		jpaRepository.setEventBus(eventBus);
		
		return jpaRepository;
	}
	
}
