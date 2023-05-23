package put.poznan.transaction;

import java.time.LocalDate;

public abstract class Transaction {

    private boolean wasExecuteCalled = false;
    private final HistoryOfTransactions historyToWriteInto;
    protected LocalDate dateOfExecution = null;

    protected Transaction(HistoryOfTransactions historyOfTransactions) {
        this.historyToWriteInto = historyOfTransactions;
    }

    /**
     * Execute method.
     * Checks if the execute method has been called on the object. If was, throws {@link IllegalStateException}.
     */
    public final void execute() {
        if (wasExecuteCalled) {
            throw new IllegalStateException(String.format("%s Transaction was already executed.", getClass().getName()));
        }
        try {
            this.executeImplementation();
        } catch (Exception exception) {
            wasExecuteCalled = true;
            dateOfExecution = LocalDate.now();
            historyToWriteInto.addTransaction(
                    new FailedTransaction(historyToWriteInto, this, exception, dateOfExecution));
            throw exception;
        }
        wasExecuteCalled = true;
        dateOfExecution = LocalDate.now();
        historyToWriteInto.addTransaction(this);
    }

    /**
     * Transaction type. To be derived by subclass.
     * @return transaction type
     */
    public abstract TransactionType getTransactionType();

    /**
     * Description of the transaction. To be derived by subclass.
     * @return description
     */
    public abstract String getDescription();

    /**
     * Implementation of execute method. To be derived by subclass.
     */
    protected abstract void executeImplementation();

    /**
     * returns true if transaction was executed, false otherwise.
     * @return boolean
     */
    public boolean isExecuted() {
        return wasExecuteCalled;
    }

    /**
     * Returns date of transaction's execution.
     * @throws IllegalStateException if the transaction was not executed.
     * @return date of execution.
     */
    public LocalDate getDateOfExecution() {
        if (dateOfExecution == null) {
            throw new IllegalStateException("The transaction was not executed.");
        }
        return dateOfExecution;
    }
}
