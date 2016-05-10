package axon.cqrs.eventsource;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;

import axon.cqrs.eventsource.accountmanagement.creditaccount.command.CreditAccount;
import axon.cqrs.eventsource.accountmanagement.debitaccount.command.DebitAccount;

@ApplicationScoped
public class CommandGatwayProducer {
	
	@Inject
	private CommandHandler<DebitAccount> debitAccountHandler;
	
	@Inject
	private CommandHandler<CreditAccount> creditAccountHandler;
	
	private CommandBus commandBus;
		
	@Produces @Default CommandGateway produceCommandGatway() {
		return new DefaultCommandGateway(commandBus);
	}
	
	@PostConstruct
	public void init () {
		
		commandBus = new SimpleCommandBus();
		
		registerCommandBus(commandBus);
	}
	
	public void registerCommandBus(CommandBus commandBus) {
		
		commandBus.subscribe(DebitAccount.class.getName(), debitAccountHandler);
		commandBus.subscribe(CreditAccount.class.getName(), creditAccountHandler);
	}
	
}
