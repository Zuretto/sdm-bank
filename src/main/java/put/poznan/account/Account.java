package put.poznan.account;

import put.poznan.transaction.HistoryOfTransactions;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
        Transaction withdrawMoneyTransaction = new WithdrawMoney(this, moneyToBeWithdrawn);
        withdrawMoneyTransaction.execute();
    }

    /**
     * Opening a deposit takes part of the money from balance and puts it in the deposit.
     *
     * @param amountToBeDeposited amount of money to be deposited
     * @param endDate             end date of the deposit
     * @param rateOfInterest      interest rate of the deposit
     * @param interestPeriod      interest period of the deposit
     */
    public void openDeposit(BigDecimal amountToBeDeposited,
                            LocalDate endDate,
                            BigDecimal rateOfInterest,
                            int interestPeriod) {
        Transaction transaction = new OpenDeposit(
                this,
                amountToBeDeposited,
                endDate,
                rateOfInterest,
                interestPeriod
        );
        transaction.execute();
    }

    /**
     * Opening a loan adds money to the balance.
     *
     * @param loanAmount amount that is to be loaned
     * @param endDate             end date of the deposit
     * @param rateOfInterest      interest rate of the deposit
     * @param interestPeriod      interest period of the deposit
     */
    public void openLoan(BigDecimal loanAmount,
                         LocalDate endDate,
                         BigDecimal rateOfInterest,
                         int interestPeriod) {
        Transaction transaction = new OpenLoan(
                this,
                loanAmount,
                endDate,
                rateOfInterest,
                interestPeriod
        );
        transaction.execute();
    }

    void addDeposit(Deposit deposit) {
        this.deposits.add(deposit);
    }

    void addLoan(Loan loan) {
        this.loans.add(loan);
    }

    public List<Deposit> getDeposits() {
        return Collections.unmodifiableList(deposits);
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
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

    HistoryOfTransactions getHistoryOfTransactions() {
        return historyOfTransactions;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // method created so that it may be overridden in DebitAccount and everything should work OK with the transactions.
    public boolean hasFunds(BigDecimal moneyToBeWithdrawn) {
        return balance.compareTo(moneyToBeWithdrawn) >= 0;
    }
}
