package axon.tutorial.accountmanagement.debitaccount.eventhandler;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.axonframework.domain.EventMessage;

import axon.tutorial.accountmanagement.debitaccount.event.AccountDebitedEvent;
import axon.tutorial.accountmanagement.model.AccountView;
import axon.tutorial.util.EventHandler;

public class AccountDebitedEventHandler implements EventHandler<AccountDebitedEvent> {

	@Inject
	private EntityManager em;
	
	@Override
	public void handle(EventMessage<AccountDebitedEvent> eventMessage) {
	
		AccountDebitedEvent accountDebitedEvent = eventMessage.getPayload();
		
		AccountView accountView = new AccountView(accountDebitedEvent.getAccount(), 
												  accountDebitedEvent.getBalance(), 
												  accountDebitedEvent.getAmmountDebited());
		
		em.merge(accountView);
	}

}
