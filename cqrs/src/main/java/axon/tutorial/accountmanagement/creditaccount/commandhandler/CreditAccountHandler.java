package axon.tutorial.accountmanagement.creditaccount.commandhandler;

import javax.inject.Inject;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.repository.Repository;
import org.axonframework.unitofwork.UnitOfWork;

import axon.tutorial.accountmanagement.creditaccount.command.CreditAccount;
import axon.tutorial.accountmanagement.model.Account;

public class CreditAccountHandler implements CommandHandler<CreditAccount> {

	@Inject
	private Repository<Account> repository;
	
	@Override
	public Object handle(CommandMessage<CreditAccount> commandMessage, UnitOfWork unitOfWork) throws Throwable {
		
		CreditAccount creditAccount = (CreditAccount) commandMessage.getPayload();
		
		Account accountToCredit = repository.load(creditAccount.getAccount());
		
		accountToCredit.credit(creditAccount.getAmount());

		return null;
	}

}
