package put.poznan.transaction;

import java.time.LocalDate;

public class FailedTransaction extends Transaction {

    private final Transaction failedTransaction;
    private final Throwable cause;

    protected FailedTransaction(HistoryOfTransactions historyOfTransactions,
                                Transaction failedTransaction,
                                Throwable cause,
                                LocalDate dateOfExecution) {
        super(historyOfTransactions);
        this.failedTransaction = failedTransaction;
        this.cause = cause;
        this.dateOfExecution = dateOfExecution;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.FAILED;
    }

    @Override
    public String getDescription() {
        return String.format("Failed transaction with type: '%s', description: '%s' and cause: '%s'",
                failedTransaction.getTransactionType(),
                failedTransaction.getDescription(),
                cause);
    }

    @Override
    protected void executeImplementation() {
        throw new UnsupportedOperationException("Failed transaction can not be executed");
    }

    /**
     * Failed Transaction is executed by default
     *
     * @return true
     */
    @Override
    public boolean isExecuted() {
        return true;
    }

    public Transaction getFailedTransaction() {
        return failedTransaction;
    }

    public Throwable getCause() {
        return cause;
    }
}
