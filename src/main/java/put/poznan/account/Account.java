package put.poznan.account;

import put.poznan.interest.InterestMechanism;
import put.poznan.reporter.Visitor;
import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface Account {
    void withdrawMoney(BigDecimal moneyToBeWithdrawn);

    void openDeposit(
            BigDecimal amountToBeDeposited,
            LocalDate endDate,
            BigDecimal rateOfInterest,
            int interestPeriod
    );

    void openLoan(
            BigDecimal loanAmount,
            LocalDate endDate,
            BigDecimal rateOfInterest,
            int interestPeriod
    );

    void closeDeposit(Deposit deposit);

    void repayLoan(Loan loan);

    void addDeposit(Deposit deposit);

    void addLoan(Loan loan);

    void removeLoan(Loan loan);

    void removeDeposit(Deposit deposit);

    List<Deposit> getDeposits();

    List<Loan> getLoans();

    BigDecimal getBalance();

    void setBalance(BigDecimal balance);

    Person getPerson();

    HistoryOfTransactions getHistoryOfTransactions();

    void setPerson(Person person);

    boolean hasFunds(BigDecimal moneyToBeWithdrawn);

    <T> T accept(Visitor<T> visitor);

    String getAccountNumber();

    InterestMechanism getInterestMechanism();

    void setInterestMechanism(InterestMechanism interestMechanism);
}