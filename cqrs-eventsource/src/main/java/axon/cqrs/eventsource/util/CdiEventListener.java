package axon.cqrs.eventsource.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.EventListener;

@SuppressWarnings("rawtypes") 
public class CdiEventListener implements EventListener {

	private final Map<Class, EventHandler> eventHandlers = new ConcurrentHashMap<Class, EventHandler>();

	@SuppressWarnings("unchecked")
	@Override
	public void handle(EventMessage message) {
		eventHandlers.get(message.getPayloadType()).handle(message);
	}
	
	public <T> void registerEventHandler(Class<T> claz, EventHandler<T> event) {
		eventHandlers.put(claz, event);
	}
	
	public <T> void unregisterEventHandler(Class<T> claz, EventHandler<T> event) {
		eventHandlers.remove(claz);
	}
}
