package put.poznan.reporter;

import put.poznan.account.*;
import put.poznan.transaction.Transaction;

public interface Visitor<T> {
    T visitAccount(Account account);

    T visitLoan(Loan loan);

    T visitDeposit(Deposit deposit);

    T visitPerson(Person person);

    T visitTransaction(Transaction transaction);
}
