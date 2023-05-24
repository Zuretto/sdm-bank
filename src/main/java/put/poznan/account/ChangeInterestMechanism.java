package put.poznan.account;

import put.poznan.interest.InterestMechanism;
import put.poznan.transaction.Transaction;

public class ChangeInterestMechanism extends Transaction {

    private final Account account;
    private final InterestMechanism interestMechanism;
    public ChangeInterestMechanism(Account account, InterestMechanism interestMechanism) {
        super(account.getHistoryOfTransactions());
        this.account = account;
        this.interestMechanism = interestMechanism;
    }

    @Override
    protected void executeImplementation() {
        account.setInterestMechanism(interestMechanism);
    }
}
