package axon.cqrs.eventsource.accountmanagement.debitaccount.eventhandler;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.axonframework.domain.EventMessage;

import axon.cqrs.eventsource.accountmanagement.debitaccount.event.AccountDebitedEvent;
import axon.cqrs.eventsource.accountmanagement.model.AccountView;
import axon.cqrs.eventsource.util.EventHandler;

public class AccountDebitedEventHandler implements EventHandler<AccountDebitedEvent> {

	@Inject
	private EntityManager em;
	
	@Override
	public void handle(EventMessage<AccountDebitedEvent> eventMessage) {
	
		AccountDebitedEvent accountDebitedEvent = eventMessage.getPayload();
		
		double newBalance = accountDebitedEvent.getBalance()-accountDebitedEvent.getAmmountDebited();
		
		AccountView accountView = new AccountView(accountDebitedEvent.getAccount(), 
												  newBalance, 
												  accountDebitedEvent.getAmmountDebited());
		
		em.merge(accountView);
	}

}
