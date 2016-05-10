package axon.cqrs.eventsource.accountmanagement.creditaccount.command;

public class CreditAccount {

	private final String account;
	
	private final Double amount;

	public CreditAccount(String account, Double amount) {
		super();
		this.account = account;
		this.amount = amount;
	}

	public String getAccount() {
		return account;
	}

	public Double getAmount() {
		return amount;
	}	
	
}
