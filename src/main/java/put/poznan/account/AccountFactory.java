package put.poznan.account;

import put.poznan.interest.InterestMechanism;

public class AccountFactory {
    public static Account createAccount(String type, Person owner, String accountNumber, InterestMechanism interestMechanism) {
        if (type.equals("standard")) {
            return new StandardAccount(owner, accountNumber, interestMechanism);
        } else if (type.equals("debit")) {
            return new DebitAccount(new StandardAccount(owner, accountNumber, interestMechanism));
        } else {
            throw new IllegalArgumentException("Unknown account type");
        }
    }
}
