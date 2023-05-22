package put.poznan.account;

import java.math.BigDecimal;

public class DebitAccount extends AccountDecorator {
    DebitAccount(Account wrappedAccount) {
        super(wrappedAccount);
    }

    @Override
    public boolean hasFunds(BigDecimal moneyToBeWithdrawn) {
        return true;
    }
}
