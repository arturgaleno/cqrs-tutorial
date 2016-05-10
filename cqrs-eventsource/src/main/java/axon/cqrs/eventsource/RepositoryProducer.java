package axon.cqrs.eventsource;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;

import axon.cqrs.eventsource.accountmanagement.model.Account;

@ApplicationScoped
public class RepositoryProducer {
	
	@Inject
	private EventBus eventBus;
	
	@Inject
	private EventStore eventStore;
	
	@Produces @Default EventSourcingRepository<Account> eventSourcingRepository() {
		  EventSourcingRepository<Account> eventSourcingRepository = new EventSourcingRepository<Account>(Account.class, eventStore);
		  eventSourcingRepository.setEventBus(eventBus);
		  return eventSourcingRepository;
	}
	
}
