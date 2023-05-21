package put.poznan.account;

import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OpenLoan extends Transaction {

    private final InterestRate interestRate;
    private final Account account;
    private final BigDecimal loanAmount;
    private final LocalDate endDate;

    public OpenLoan(Account account,
                       BigDecimal loanAmount,
                       LocalDate endDate,
                       BigDecimal rateOfInterest,
                       int interestPeriod) {
        super(account.getHistoryOfTransactions());
        this.interestRate = new InterestRate(rateOfInterest, interestPeriod);
        this.account = account;
        this.loanAmount = loanAmount;
        this.endDate = endDate;
    }

    @Override
    protected void executeImplementation() {
        final BigDecimal newAccountBalance = account.getBalance().add(loanAmount);
        account.setBalance(newAccountBalance);
        Loan loan = new Loan(interestRate, LocalDate.now(), endDate, account, loanAmount);
        account.addLoan(loan);
    }
}
