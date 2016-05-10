package axon.tutorial.accountmanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.axonframework.domain.AbstractAggregateRoot;

import axon.tutorial.accountmanagement.creditaccount.event.AccountCreditedEvent;
import axon.tutorial.accountmanagement.debitaccount.event.AccountDebitedEvent;

@Entity
public class Account extends AbstractAggregateRoot<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1527700969769949359L;

	@Id
	private String id;
	
	@Column
	private Double balance;
	
	public Account() {
		this(null, null);
	}
	
	public Account(String id) {
		this(id, 0.0d);
	}

	public Account(String id, Double balance) {
		super();
		this.id = id;
		this.balance = balance;
	}

	@Override
	public String getIdentifier() {
		return id;
	}
	
   public Double getBalance() {
       return balance;
   }
 
   public void setIdentifier(String id) {
       this.id = id;
   }
	
   @Override
   public void markDeleted() {
	super.markDeleted();
   }

   /**
    * Business Logic
    * Cannot debit with a negative amount
    * Cannot debit with more than a million amount (You laundering money?)
    * @param debitAmount
    */
   public void debit(Double debitAmount) {
 
       if (Double.compare(debitAmount, 0.0d) > 0 && this.balance - debitAmount > -1) {
    	   this.balance -= debitAmount;
    	   
    	   AccountDebitedEvent accountDebitedEvent = new AccountDebitedEvent(this.id, debitAmount, this.balance);
    	   registerEvent(accountDebitedEvent);
       } else {
           throw new IllegalArgumentException("Cannot debit with the amount");
       }
 
   }
 
   /**
    * Business Logic
    * Cannot credit with a negative amount
    * Cannot credit with amount that leaves the account 
    * balance in a negative state
    * @param creditAmount
    */
   public void credit(Double creditAmount) {
       if (Double.compare(creditAmount, 0.0d) > 0 && Double.compare(creditAmount, 1000000) < 0) {
           this.balance += creditAmount;
           
           AccountCreditedEvent accountCreditedEvent = new AccountCreditedEvent(this.id, creditAmount, this.balance);
    	   registerEvent(accountCreditedEvent);
       } else {
           throw new IllegalArgumentException("Cannot credit with the amount");
       }
   }
}
