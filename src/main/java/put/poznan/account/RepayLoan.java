package put.poznan.account;

import put.poznan.transaction.Transaction;
import put.poznan.transaction.TransactionType;

import java.math.BigDecimal;

public class RepayLoan extends Transaction {

    private final Account account;
    private final Loan loan;

    public RepayLoan(Account account, Loan loan) {
        super(account.getHistoryOfTransactions());
        this.account = account;
        this.loan = loan;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.REPAY_LOAN;
    }

    @Override
    public String getDescription() {
        return String.format("Transaction to repay loan %s for account %s", loan, account);
    }

    @Override
    protected void executeImplementation() {
        BigDecimal loanAmountAndInterest = loan.getAmount()
                .add(account.getInterestMechanism().calculateInterest(loan.getAmount(), loan.getStartDate(), loan.getEndDate()));
        if (!account.hasFunds(loanAmountAndInterest)) {
            throw new IllegalStateException("Could not repay loan - insufficient funds.");
        }
        BigDecimal newAccountBalance = account.getBalance().subtract(loanAmountAndInterest);
        account.setBalance(newAccountBalance);
        account.removeLoan(loan);
    }
}
