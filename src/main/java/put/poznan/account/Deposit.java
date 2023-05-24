package put.poznan.account;

import put.poznan.reporter.Visitor;
import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Deposit {

    private final HistoryOfTransactions historyOfTransactions = new HistoryOfTransactions();
    private final Account account;
    private final BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;

    public Deposit(LocalDate startDate, LocalDate endDate, Account account, BigDecimal depositAmount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.account = account;
        this.amount = depositAmount;
    }

    public Account getAccount() {
        return account;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public HistoryOfTransactions getHistoryOfTransactions() {
        return historyOfTransactions;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String accept (Visitor visitor) {
        return visitor.visitDeposit(this);
    }
}
