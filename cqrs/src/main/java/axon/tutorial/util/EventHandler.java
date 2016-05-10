package axon.tutorial.util;

import org.axonframework.domain.EventMessage;

public interface EventHandler<T> {
	void handle(EventMessage<T> eventMessage);
}
