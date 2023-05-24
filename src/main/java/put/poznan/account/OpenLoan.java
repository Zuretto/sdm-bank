package put.poznan.account;

import put.poznan.transaction.Transaction;
import put.poznan.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OpenLoan extends Transaction {

    private final Account account;
    private final BigDecimal loanAmount;
    private final LocalDate endDate;

    public OpenLoan(Account account,
                    BigDecimal loanAmount,
                    LocalDate endDate) {
        super(account.getHistoryOfTransactions());
        this.account = account;
        this.loanAmount = loanAmount;
        this.endDate = endDate;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.OPEN_LOAN;
    }

    @Override
    public String getDescription() {
        return String.format(
                "Transaction to open new loan for an account: %s with amount: %s and end date: %s",
                account, loanAmount, endDate
        );
    }

    @Override
    protected void executeImplementation() {
        final BigDecimal newAccountBalance = account.getBalance().add(loanAmount);
        account.setBalance(newAccountBalance);
        Loan loan = new Loan(LocalDate.now(), endDate, account, loanAmount);
        account.addLoan(loan);
    }
}
