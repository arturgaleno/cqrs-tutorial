package axon.cqrs.eventsource.test;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.axonframework.unitofwork.TransactionManager;

public class JPATransationManager implements TransactionManager<UserTransaction> {

	private UserTransaction userTransaction;
	
	public JPATransationManager(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}

	@Override
	public UserTransaction startTransaction() {
		try {
			userTransaction.begin();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return userTransaction;
	}

	@Override
	public void commitTransaction(UserTransaction userTransaction) {
		try {
			userTransaction.commit();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rollbackTransaction(UserTransaction userTransaction) {
		try {
			userTransaction.rollback();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}


}