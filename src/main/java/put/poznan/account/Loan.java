package put.poznan.account;

import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Loan {

    private final HistoryOfTransactions historyOfTransactions = new HistoryOfTransactions();
    private final Account account;
    private InterestRate interestRate;
    private final BigDecimal amount;

    private LocalDate startDate;
    private LocalDate endDate;

    public Loan(InterestRate interestRate, LocalDate startDate, LocalDate endDate, Account account, BigDecimal depositAmount) {
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.account = account;
        this.amount = depositAmount;
    }

    public HistoryOfTransactions getHistoryOfTransactions() {
        return historyOfTransactions;
    }

    public Account getAccount() {
        return account;
    }

    public InterestRate getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(InterestRate interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
