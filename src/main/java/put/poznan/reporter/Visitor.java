package put.poznan.reporter;

import put.poznan.account.*;

public interface Visitor {
    String visitAccount(Account account);

    String visitLoan(Loan loan);

    String visitDeposit(Deposit deposit);

    String visitPerson(Person person);

    String visitMakePayment(MakePayment payment);

    String visitReceivePayment(ReceivePayment payment);
}
