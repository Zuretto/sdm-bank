package put.poznan.transaction;

public abstract class Transaction {

    private boolean wasExecuteCalled = false;

    /**
     * Execute method.
     * Checks if the execute method has been called on the object. If was, throws {@link IllegalStateException}.
     */
    public final void execute() {
        if (wasExecuteCalled) {
            throw new IllegalStateException(String.format("%s Transaction was already executed.", getClass().getName()));
        }
        this.executeImplementation();
        wasExecuteCalled = true;
    }

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
}
