package put.poznan.account;

import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Deposit {

    private final HistoryOfTransactions historyOfTransactions = new HistoryOfTransactions();
    private final Account account;
    private InterestRate interestRate;
    private BigDecimal amount;
    private LocalDate endDate;

    public Deposit(InterestRate interestRate, LocalDate endDate, Account account) {
        this.interestRate = interestRate;
        this.amount = BigDecimal.ZERO;
        this.endDate = endDate;
        this.account = account;
    }

    public Account getAccount() {
        return account;
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
