package put.poznan.reporter;

import put.poznan.account.Account;
import put.poznan.account.Deposit;
import put.poznan.account.Loan;
import put.poznan.account.Person;

public interface Visitor {
    String visitAccount(Account account);

    String visitLoan(Loan loan);

    String visitDeposit(Deposit deposit);

    String visitPerson(Person person);
}
