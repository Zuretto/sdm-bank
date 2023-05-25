package put.poznan.account;

import put.poznan.reporter.Visitor;
import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Loan {

    private final HistoryOfTransactions historyOfTransactions = new HistoryOfTransactions();
    private final Account account;
    private final BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;

    public Loan(LocalDate startDate, LocalDate endDate, Account account, BigDecimal depositAmount) {
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

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitLoan(this);
    }
}
