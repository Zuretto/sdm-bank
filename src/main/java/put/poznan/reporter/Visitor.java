package put.poznan.reporter;

import put.poznan.account.*;
import put.poznan.transaction.Transaction;

public interface Visitor {
    String visitAccount(Account account);

    String visitLoan(Loan loan);

    String visitDeposit(Deposit deposit);

    String visitPerson(Person person);

    String visitTransaction(Transaction transaction);
}
