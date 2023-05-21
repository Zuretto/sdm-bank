package put.poznan.account;

import put.poznan.transaction.Transaction;

public class ChangeInterestRate {

    private ChangeInterestRate() {
    }

    public static Transaction createChangeInterestRateTransaction(Deposit deposit, InterestRate newInterestRate) {
        return new ChangeDepositInterestRate(newInterestRate, deposit);
    }

    public static Transaction createChangeInterestRateTransaction(Loan loan, InterestRate newInterestRate) {
        return new ChangeLoanInterestRate(newInterestRate, loan);
    }

    private static class ChangeDepositInterestRate extends Transaction {

        private final Deposit deposit;
        private final InterestRate newInterestRate;

        ChangeDepositInterestRate(InterestRate newInterestRate, Deposit deposit) {
            super(deposit.getHistoryOfTransactions());
            this.deposit = deposit;
            this.newInterestRate = newInterestRate;
        }

        @Override
        protected void executeImplementation() {
            deposit.setInterestRate(newInterestRate);
        }
    }

    private static class ChangeLoanInterestRate extends Transaction {

        private final Loan loan;
        private final InterestRate newInterestRate;

        ChangeLoanInterestRate(InterestRate newInterestRate, Loan loan) {
            super(loan.getHistoryOfTransactions());
            this.loan = loan;
            this.newInterestRate = newInterestRate;
        }

        @Override
        protected void executeImplementation() {
            loan.setInterestRate(newInterestRate);
        }
    }
}
