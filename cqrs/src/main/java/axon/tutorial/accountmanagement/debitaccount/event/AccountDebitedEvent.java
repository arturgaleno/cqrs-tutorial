package axon.tutorial.accountmanagement.debitaccount.event;

public class AccountDebitedEvent {

	private final String account;
	private final double ammountDebited;
	private final double balance;
	
	public AccountDebitedEvent(String account, double ammountDebited, double balance) {
		this.account = account;
		this.ammountDebited = ammountDebited;
		this.balance = balance;
	}

	public String getAccount() {
		return account;
	}

	public double getAmmountDebited() {
		return ammountDebited;
	}

	public double getBalance() {
		return balance;
	}
}
