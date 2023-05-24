package put.poznan.account;

import put.poznan.transaction.Transaction;
import put.poznan.transaction.TransactionType;

import java.math.BigDecimal;

public class WithdrawMoney extends Transaction {

    private final Account account;
    private final BigDecimal withdrawAmount;

    public WithdrawMoney(Account account,
                         BigDecimal withdrawAmount) {
        super(account.getHistoryOfTransactions());
        this.account = account;
        this.withdrawAmount = withdrawAmount;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.WITHDRAW_MONEY;
    }

    @Override
    public String getDescription() {
        return String.format("Transaction to withdraw money from account: %s with amount: %s", account, withdrawAmount);
    }

    /**
     * Performs withdrawal.
     * @throws IllegalStateException if money could not be withdrawn from the account
     */
    @Override
    protected void executeImplementation() {
        if (!account.hasFunds(withdrawAmount)) {
            throw new IllegalStateException("can not withdraw such money from the account!");
        }
        final BigDecimal newAccountBalance = account.getBalance().subtract(withdrawAmount);
        account.setBalance(newAccountBalance);
    }
}
