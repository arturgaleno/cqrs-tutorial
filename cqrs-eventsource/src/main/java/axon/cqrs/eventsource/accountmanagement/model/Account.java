package axon.cqrs.eventsource.accountmanagement.model;

import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import axon.cqrs.eventsource.accountmanagement.createaccount.event.AccountCreatedEvent;
import axon.cqrs.eventsource.accountmanagement.creditaccount.event.AccountCreditedEvent;
import axon.cqrs.eventsource.accountmanagement.debitaccount.event.AccountDebitedEvent;
		
public class Account extends AbstractAnnotatedAggregateRoot<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1527700969769949359L;
	
	@AggregateIdentifier
	private String id;
	
	private Double balance;

	public Account() {}
	
	public Account(String id) {
		apply(new AccountCreatedEvent(id));
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
    	   AccountDebitedEvent accountDebitedEvent = new AccountDebitedEvent(this.id, debitAmount, this.balance);
    	   apply(accountDebitedEvent);
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
           AccountCreditedEvent accountCreditedEvent = new AccountCreditedEvent(this.id, creditAmount, this.balance);
           apply(accountCreditedEvent);
       } else {
           throw new IllegalArgumentException("Cannot credit with the amount");
       }
   }
   
   @EventSourcingHandler
   private void applyDebit(AccountDebitedEvent event) {
     this.balance -= event.getAmmountDebited();
   }

   @EventSourcingHandler
   private void applyCredit(AccountCreditedEvent event) {
    this.balance += event.getAmountCredited();
   }
   
   @EventSourcingHandler
   public void applyAccountCreation(AccountCreatedEvent event) {
     this.id = event.getId();
     this.balance = 0.0d;
   }
   
}
