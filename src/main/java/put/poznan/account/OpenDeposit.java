package put.poznan.account;

import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OpenDeposit extends Transaction {

    private final InterestRate interestRate;
    private final Account account;
    private final BigDecimal amountToBeDeposited;
    private final LocalDate endDate;

    protected OpenDeposit(Account account,
                          BigDecimal amountToBeDeposited,
                          LocalDate endDate,
                          BigDecimal rateOfInterest,
                          int interestPeriod) {
        super(account.getHistoryOfTransactions());
        this.interestRate = new InterestRate(rateOfInterest, interestPeriod);
        this.account = account;
        this.amountToBeDeposited = amountToBeDeposited;
        this.endDate = endDate;
    }

    @Override
    protected void executeImplementation() {
        if (!account.hasFunds(amountToBeDeposited)) {
            throw new IllegalStateException("Can not open a deposit because of insufficient funds.");
        }
        final BigDecimal newAccountBalance = account.getBalance().subtract(amountToBeDeposited);
        account.setBalance(newAccountBalance);
        Deposit deposit = new Deposit(interestRate, LocalDate.now(), endDate, account, amountToBeDeposited);
        account.addDeposit(deposit);
    }
}