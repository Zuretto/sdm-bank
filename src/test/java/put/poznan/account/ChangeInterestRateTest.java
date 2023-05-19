package put.poznan.account;


import org.junit.jupiter.api.Test;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ChangeInterestRateTest {

    @Test
    void shouldChangeInterestRateForDeposit() {
        // given
        Deposit deposit = new Deposit(new InterestRate(BigDecimal.ONE, 10), LocalDate.of(2020, 1, 1));
        InterestRate newInterestRate = new InterestRate(BigDecimal.TEN, 20);
        Transaction changeInterestRate = ChangeInterestRate.createChangeInterestRateTransaction(deposit, newInterestRate);
        // when
        changeInterestRate.execute();
        // then
        assertThat(deposit.getInterestRate()).isSameAs(newInterestRate);
        assertThat(deposit.getHistoryOfTransactions().getTransactions()).containsExactly(changeInterestRate);
    }

    @Test
    void shouldChangeInterestRateForLoan() {
        // given
        Loan loan = new Loan(new InterestRate(BigDecimal.ONE, 10), LocalDate.of(2020, 1, 1));
        InterestRate newInterestRate = new InterestRate(BigDecimal.TEN, 20);
        Transaction changeInterestRate = ChangeInterestRate.createChangeInterestRateTransaction(loan, newInterestRate);
        // when
        changeInterestRate.execute();
        // then
        assertThat(loan.getInterestRate()).isSameAs(newInterestRate);
        assertThat(loan.getHistoryOfTransactions().getTransactions()).containsExactly(changeInterestRate);
    }
}