package put.poznan.transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryOfTransactions {

    private final List<Transaction> transactions = new ArrayList<>();

    public HistoryOfTransactions() {
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void addTransaction(Transaction transaction) {
        if (!transaction.isExecuted()) {
            throw new IllegalStateException("Transaction was not executed and should not be part of History.");
        }
        transactions.add(transaction);
    }
}
