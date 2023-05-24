package put.poznan.account;

import put.poznan.transaction.Transaction;
import put.poznan.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CloseDeposit extends Transaction {

    private final Deposit deposit;
    private final Account account;

    public CloseDeposit(Account account,
                        Deposit deposit) {
        super(account.getHistoryOfTransactions());
        this.account = account;
        this.deposit = deposit;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.CLOSE_DEPOSIT;
    }

    @Override
    public String getDescription() {
        return String.format("Transaction to close deposit. Deposit: %s, account: %s", deposit, account);
    }

    @Override
    protected void executeImplementation() {
        final BigDecimal newAccountAmount;
        if (deposit.getEndDate().isBefore(LocalDate.now())) {
            BigDecimal interest = account.getInterestMechanism().calculateInterest(deposit.getAmount(),
                    deposit.getStartDate(),
                    deposit.getEndDate());
            newAccountAmount = account.getBalance()
                    .add(deposit.getAmount())
                    .add(interest);
        } else {
            newAccountAmount = account.getBalance().add(deposit.getAmount());
        }
        account.setBalance(newAccountAmount);
        account.removeDeposit(deposit);
    }
}
