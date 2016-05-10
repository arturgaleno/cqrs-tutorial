package axon.cqrs.eventsource;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;

@ApplicationScoped
public class EventStoreProducer {

	private EventStore eventStore;
	
	@Produces @Default public EventStore produceEventStore() {
		return eventStore;
	}
	
	@PostConstruct
	private void init() {
		File file = new File("./events");
		SimpleEventFileResolver simpleEventFileResolver = new SimpleEventFileResolver(file);
		eventStore = new FileSystemEventStore(simpleEventFileResolver);
	}
	
}
