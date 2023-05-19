package put.poznan.account;

import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Loan {

    private final HistoryOfTransactions historyOfTransactions = new HistoryOfTransactions();
    private InterestRate interestRate;
    private BigDecimal amount;
    private LocalDate endDate;

    public Loan(InterestRate interestRate, LocalDate endDate) {
        this.interestRate = interestRate;
        this.amount = BigDecimal.ZERO;
        this.endDate = endDate;
    }

    public HistoryOfTransactions getHistoryOfTransactions() {
        return historyOfTransactions;
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

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
