package put.poznan.account;

import put.poznan.reporter.Visitor;
import put.poznan.transaction.HistoryOfTransactions;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassicAccount implements Account {

    private final HistoryOfTransactions historyOfTransactions = new HistoryOfTransactions();

    private BigDecimal balance;
    private Person person;
    private final List<Deposit> deposits;
    private final List<Loan> loans;

    public ClassicAccount(Person person) {
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

    /**
     * Closes given deposit.
     * @param deposit deposit to be closed.
     */
    public void closeDeposit(Deposit deposit) {
        Transaction transaction = new CloseDeposit(this, deposit);
        transaction.execute();
    }

    /**
     * Repays loan.
     * @param loan loan to be repaid.
     */
    public void repayLoan(Loan loan) {
        Transaction transaction = new RepayLoan(this, loan);
        transaction.execute();
    }

    public void addDeposit(Deposit deposit) {
        this.deposits.add(deposit);
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
    }

    public void removeLoan(Loan loan) {
        this.loans.remove(loan);
    }

    public void removeDeposit(Deposit deposit) {
        this.deposits.remove(deposit);
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

    public HistoryOfTransactions getHistoryOfTransactions() {
        return historyOfTransactions;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // method created so that it may be overridden in DebitAccount and everything should work OK with the transactions.
    public boolean hasFunds(BigDecimal moneyToBeWithdrawn) {
        return balance.compareTo(moneyToBeWithdrawn) >= 0;
    }

    public String accept(Visitor visitor) {
        return visitor.visitAccount(this);
    }
}
