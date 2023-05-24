package put.poznan.account;

import put.poznan.interest.InterestMechanism;
import put.poznan.reporter.Visitor;
import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AccountDecorator implements Account {
    private final Account wrappedAccount;

    AccountDecorator(Account wrappedAccount) {
        this.wrappedAccount = wrappedAccount;
    }

    @Override
    public void openDeposit(
            BigDecimal amountToBeDeposited,
            LocalDate endDate,
            BigDecimal rateOfInterest,
            int interestPeriod
    ) {
        wrappedAccount.openDeposit(amountToBeDeposited, endDate, rateOfInterest, interestPeriod);
    }

    @Override
    public void withdrawMoney(BigDecimal moneyToBeWithdrawn) {
        wrappedAccount.withdrawMoney(moneyToBeWithdrawn);
    }

    @Override
    public void openLoan(BigDecimal loanAmount, LocalDate endDate, BigDecimal rateOfInterest, int interestPeriod) {
        wrappedAccount.openLoan(loanAmount, endDate, rateOfInterest, interestPeriod);
    }

    @Override
    public void closeDeposit(Deposit deposit) {
        wrappedAccount.closeDeposit(deposit);
    }

    @Override
    public void repayLoan(Loan loan) {
        wrappedAccount.repayLoan(loan);
    }

    @Override
    public void addDeposit(Deposit deposit) {
        wrappedAccount.addDeposit(deposit);
    }

    @Override
    public void addLoan(Loan loan) {
        wrappedAccount.addLoan(loan);
    }

    @Override
    public void removeLoan(Loan loan) {
        wrappedAccount.removeLoan(loan);
    }

    @Override
    public void removeDeposit(Deposit deposit) {
        wrappedAccount.removeDeposit(deposit);
    }

    @Override
    public List<Deposit> getDeposits() {
        return wrappedAccount.getDeposits();
    }

    @Override
    public List<Loan> getLoans() {
        return wrappedAccount.getLoans();
    }

    @Override
    public BigDecimal getBalance() {
        return wrappedAccount.getBalance();
    }

    @Override
    public void setBalance(BigDecimal balance) {
        wrappedAccount.setBalance(balance);
    }

    @Override
    public Person getPerson() {
        return wrappedAccount.getPerson();
    }

    @Override
    public HistoryOfTransactions getHistoryOfTransactions() {
        return wrappedAccount.getHistoryOfTransactions();
    }

    @Override
    public void setPerson(Person person) {
        wrappedAccount.setPerson(person);
    }

    @Override
    public boolean hasFunds(BigDecimal moneyToBeWithdrawn) {
        return wrappedAccount.hasFunds(moneyToBeWithdrawn);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return wrappedAccount.accept(visitor);
    }

    @Override
    public String getAccountNumber() {
        return wrappedAccount.getAccountNumber();
    }

    @Override
    public InterestMechanism getInterestMechanism() {
        return wrappedAccount.getInterestMechanism();
    }

    @Override
    public void setInterestMechanism(InterestMechanism interestMechanism) {
        wrappedAccount.setInterestMechanism(interestMechanism);
    }
}
