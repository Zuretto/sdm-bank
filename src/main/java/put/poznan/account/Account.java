package put.poznan.account;

import put.poznan.transaction.HistoryOfTransactions;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {

    private final HistoryOfTransactions historyOfTransactions = new HistoryOfTransactions();

    private BigDecimal balance;
    private Person person;
    private final List<Deposit> deposits;
    private final List<Loan> loans;

    public Account(Person person) {
        this.person = person;
        this.balance = new BigDecimal(0);
        this.deposits = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public void withdrawMoney(BigDecimal moneyToBeWithdrawn) {
        Transaction withdrawMoneyTransaction = new WithdrawMoney(historyOfTransactions, this, moneyToBeWithdrawn);
        withdrawMoneyTransaction.execute();
    }

    public void openDeposit() {
        // TODO...
    }

    public void openLoan() {
        // TODO...
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean canMoneyBeWithdrawn(BigDecimal moneyToBeWithdrawn) {
        return balance.compareTo(moneyToBeWithdrawn) >= 0;
    }
}
