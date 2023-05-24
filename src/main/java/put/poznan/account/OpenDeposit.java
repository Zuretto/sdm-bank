package put.poznan.account;

import put.poznan.transaction.Transaction;
import put.poznan.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OpenDeposit extends Transaction {

    private final Account account;
    private final BigDecimal amountToBeDeposited;
    private final LocalDate endDate;

    public OpenDeposit(Account account,
                          BigDecimal amountToBeDeposited,
                          LocalDate endDate) {
        super(account.getHistoryOfTransactions());
        this.account = account;
        this.amountToBeDeposited = amountToBeDeposited;
        this.endDate = endDate;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.OPEN_DEPOSIT;
    }

    @Override
    public String getDescription() {
        return String.format("Transaction to open deposit for account: %s, amount: %s and end date: %s",
                account,
                amountToBeDeposited,
                endDate);
    }

    @Override
    protected void executeImplementation() {
        if (!account.hasFunds(amountToBeDeposited)) {
            throw new IllegalStateException("Can not open a deposit because of insufficient funds.");
        }
        final BigDecimal newAccountBalance = account.getBalance().subtract(amountToBeDeposited);
        account.setBalance(newAccountBalance);
        Deposit deposit = new Deposit(LocalDate.now(), endDate, account, amountToBeDeposited);
        account.addDeposit(deposit);
    }
}
