package put.poznan.account;

import put.poznan.interest.InterestMechanism;
import put.poznan.transaction.Transaction;
import put.poznan.transaction.TransactionType;

public class ChangeInterestMechanism extends Transaction {

    private final Account account;
    private final InterestMechanism interestMechanism;
    public ChangeInterestMechanism(Account account, InterestMechanism interestMechanism) {
        super(account.getHistoryOfTransactions());
        this.account = account;
        this.interestMechanism = interestMechanism;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.CHANGE_INTEREST_MECHANSIM;
    }

    @Override
    public String getDescription() {
        return String.format("Transaction to change interest rate on account: %s to: %s",
                account, interestMechanism);
    }

    @Override
    protected void executeImplementation() {
        account.setInterestMechanism(interestMechanism);
    }
}
