package put.poznan.account;

import put.poznan.transaction.HistoryOfTransactions;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;

public class WithdrawMoney extends Transaction {

    private final Account account;
    private final BigDecimal withdrawAmount;

    public WithdrawMoney(HistoryOfTransactions historyOfTransactions,
                         Account account,
                         BigDecimal withdrawAmount) {
        super(historyOfTransactions);
        this.account = account;
        this.withdrawAmount = withdrawAmount;
    }

    /**
     * Performs withdrawal.
     * @throws IllegalStateException if money could not be withdrawn from the account
     */
    @Override
    protected void executeImplementation() {
        if (!account.canMoneyBeWithdrawn(withdrawAmount)) {
            throw new IllegalStateException("can not withdraw such money from the account!");
        }
        final BigDecimal newAccountBalance = account.getBalance().subtract(withdrawAmount);
        account.setBalance(newAccountBalance);
    }
}
