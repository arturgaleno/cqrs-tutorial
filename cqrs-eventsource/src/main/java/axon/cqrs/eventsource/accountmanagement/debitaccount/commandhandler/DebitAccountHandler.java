package axon.cqrs.eventsource.accountmanagement.debitaccount.commandhandler;

import javax.inject.Inject;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.repository.Repository;
import org.axonframework.unitofwork.UnitOfWork;

import axon.cqrs.eventsource.accountmanagement.debitaccount.command.DebitAccount;
import axon.cqrs.eventsource.accountmanagement.model.Account;

public class DebitAccountHandler implements CommandHandler<DebitAccount> {

	@Inject
	private Repository<Account> repository;
	
	@Override
	public Object handle(CommandMessage<DebitAccount> commandMessage, UnitOfWork unitOfWork) throws Throwable {
		
		DebitAccount debitAccount = (DebitAccount) commandMessage.getPayload();
		
		Account accountToDebit = repository.load(debitAccount.getAccount());
		
		accountToDebit.debit(debitAccount.getAmount());
		
		return null;
	}

}
