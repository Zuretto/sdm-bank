package put.poznan.account;

import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CloseDeposit extends Transaction {

    private final Deposit deposit;
    private final Account account;

    public CloseDeposit(Account account,
                        Deposit deposit) {
        super(account.getHistoryOfTransactions());
        this.account = account;
        this.deposit = deposit;
    }

    @Override
    protected void executeImplementation() {
        final BigDecimal newAccountAmount;
        if (deposit.getEndDate().isBefore(LocalDate.now())) {
            BigDecimal interest = deposit.getInterestRate().calculateInterest(deposit.getAmount(),
                    deposit.getStartDate(),
                    deposit.getEndDate());
            newAccountAmount = account.getBalance()
                    .add(deposit.getAmount())
                    .add(interest);
        } else {
            newAccountAmount = account.getBalance().add(deposit.getAmount());
        }
        account.setBalance(newAccountAmount);
        account.removeDeposit(deposit);
    }
}
