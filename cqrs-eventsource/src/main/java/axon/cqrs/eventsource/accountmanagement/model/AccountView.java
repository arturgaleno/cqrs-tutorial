package axon.cqrs.eventsource.accountmanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccountView {
	
	@Id
	private String account;
	
	@Column
	private Double ammount;
	
	@Column
	private Double balance;
	
	public AccountView() {}

	public AccountView(String account, Double balance, Double ammount) {
		this.account = account;
		this.balance = balance;
		this.ammount = ammount;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getAmmount() {
		return ammount;
	}

	public void setAmmount(Double ammount) {
		this.ammount = ammount;
	}
}
