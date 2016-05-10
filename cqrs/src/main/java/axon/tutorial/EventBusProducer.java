package axon.tutorial;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;

import axon.tutorial.accountmanagement.debitaccount.event.AccountDebitedEvent;
import axon.tutorial.util.CdiEventListener;
import axon.tutorial.util.EventHandler;

@ApplicationScoped
public class EventBusProducer {

	@Inject
	private CdiEventListener cdiEventListener;
	
	@Inject
	private EventHandler<AccountDebitedEvent> accountDebitedEventHandler;
	
	private SimpleEventBus eventBus;
	
	@Produces @Default EventBus produceEventBus() {
		return eventBus;
	}
	
	@PostConstruct
	public void init() {

		eventBus = new SimpleEventBus();
		
		AnnotationEventListenerAdapter.subscribe(cdiEventListener, eventBus);

		cdiEventListener.registerEventHandler(AccountDebitedEvent.class, accountDebitedEventHandler);
		
		eventBus.subscribe(cdiEventListener);
	}
	
}
