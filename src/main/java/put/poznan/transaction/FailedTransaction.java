package put.poznan.transaction;

public class FailedTransaction extends Transaction {

    private final Transaction failedTransaction;
    private final Throwable cause;

    protected FailedTransaction(HistoryOfTransactions historyOfTransactions,
                                Transaction failedTransaction,
                                Throwable cause) {
        super(historyOfTransactions);
        this.failedTransaction = failedTransaction;
        this.cause = cause;
    }

    @Override
    protected void executeImplementation() {
        throw new UnsupportedOperationException("Failed transaction can not be executed");
    }

    /**
     * Failed Transaction is executed by default
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
