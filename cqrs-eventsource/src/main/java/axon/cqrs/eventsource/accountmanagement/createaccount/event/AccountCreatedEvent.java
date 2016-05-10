package axon.cqrs.eventsource.accountmanagement.createaccount.event;

public class AccountCreatedEvent {

	private final String id;

	public AccountCreatedEvent(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
}
