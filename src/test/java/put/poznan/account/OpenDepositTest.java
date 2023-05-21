package put.poznan.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class OpenDepositTest {

    @Test
    void shouldOpenDeposit() {
        // given
        Account account = new Account(Mockito.mock(Person.class));
        account.setBalance(new BigDecimal("1000"));
        Transaction transaction = new OpenDeposit(
                account,
                new BigDecimal("100"),
                LocalDate.of(2024, 12, 31),
                new BigDecimal("0.02"),
                3
        );
        // when
        transaction.execute();
        // then
        assertThat(account)
                .extracting(Account::getDeposits)
                .satisfies(deposits -> assertThat(deposits).hasSize(1))
                .extracting(deposits -> deposits.get(0))
                .satisfies(
                        deposit -> assertThat(deposit.getAmount()).isEqualTo(new BigDecimal("100")),
                        deposit -> assertThat(deposit.getAccount()).isSameAs(account),
                        deposit -> assertThat(deposit.getStartDate()).isEqualTo(LocalDate.now()),
                        deposit -> assertThat(deposit.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31)),
                        deposit -> assertThat(deposit.getInterestRate())
                                .satisfies(
                                        interestRate -> assertThat(interestRate.getInterestPeriod())
                                                .isEqualTo(3),
                                        interestRate -> assertThat(interestRate.getInterestRate())
                                                .isEqualTo(new BigDecimal("0.02"))));
    }

    // no exception test

}