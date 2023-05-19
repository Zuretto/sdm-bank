package put.poznan.account;

import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;

public class Account {

    private final HistoryOfTransactions historyOfTransactions = new HistoryOfTransactions();

    private BigDecimal balance;

    private Person person;

    public Account(Person person) {
        this.person = person;
        this.balance = new BigDecimal(0);
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
}
