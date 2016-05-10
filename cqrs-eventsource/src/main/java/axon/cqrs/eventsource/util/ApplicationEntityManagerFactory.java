package axon.cqrs.eventsource.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class ApplicationEntityManagerFactory {
	
	@PersistenceContext
	@Produces
	private EntityManager em;
	
}
