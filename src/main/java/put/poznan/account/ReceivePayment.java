package put.poznan.account;

import put.poznan.reporter.Visitor;
import put.poznan.transaction.HistoryOfTransactions;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;

public class ReceivePayment extends Transaction {
    private final String fromAccountNumber;
    private final Account toAccount;
    private final BigDecimal amount;

    public ReceivePayment(String fromAccountNumber, Account toAccount, BigDecimal amount) {
        super(toAccount.getHistoryOfTransactions());
        this.fromAccountNumber = fromAccountNumber;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    @Override
    protected void executeImplementation() {
        toAccount.setBalance(toAccount.getBalance().add(amount));
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String accept(Visitor visitor) {
        return visitor.visitReceivePayment(this);
    }
}
