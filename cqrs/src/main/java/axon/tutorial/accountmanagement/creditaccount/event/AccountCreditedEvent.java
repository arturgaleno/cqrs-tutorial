package axon.tutorial.accountmanagement.creditaccount.event;
public class AccountCreditedEvent {
 
  private final String accountNo;
  private final Double amountCredited;
  private final Double balance;
 
  public AccountCreditedEvent(String accountNo, Double amountCredited, Double balance) {
      this.accountNo = accountNo;
      this.amountCredited = amountCredited;
      this.balance = balance;
  }
 
  public String getAccountNo() {
      return accountNo;
  }
 
  public Double getAmountCredited() {
      return amountCredited;
  }
 
  public Double getBalance() {
      return balance;
  }
}