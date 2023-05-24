package put.poznan.account;

public class AccountFactory {
    public static Account createAccount(String type, Person owner, String accountNumber) {
        if (type.equals("standard")) {
            return new StandardAccount(owner, accountNumber);
        } else if (type.equals("debit")) {
            return new DebitAccount(new StandardAccount(owner, accountNumber));
        } else {
            throw new IllegalArgumentException("Unknown account type");
        }
    }
}
