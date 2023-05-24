package put.poznan.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.interest.InterestMechanism;
import put.poznan.transaction.FailedTransaction;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OpenDepositTest {

    @Test
    void shouldOpenDeposit() {
        // given
        Account account = new StandardAccount(Mockito.mock(Person.class), "", Mockito.mock(InterestMechanism.class));
        account.setBalance(new BigDecimal("1000"));
        Transaction transaction = new OpenDeposit(
                account,
                new BigDecimal("100"),
                LocalDate.of(2024, 12, 31)
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
                        deposit -> assertThat(deposit.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31))
                );
    }

    @Test
    void shouldNotOpenDeposit() {
        Account account = new StandardAccount(Mockito.mock(Person.class), "", Mockito.mock(InterestMechanism.class));
        account.setBalance(new BigDecimal("1000"));
        Transaction transaction = new OpenDeposit(
                account,
                new BigDecimal("1001"),
                LocalDate.of(2024, 12, 31)
        );

        assertThrows(IllegalStateException.class, transaction::execute);
        assertThat(account.getHistoryOfTransactions().getTransactions().get(0))
                .isInstanceOf(FailedTransaction.class)
                .satisfies(failedTransaction -> assertThat(((FailedTransaction)failedTransaction).getCause())
                        .isInstanceOf(IllegalStateException.class))
                .extracting(failedTransaction -> ((FailedTransaction)failedTransaction).getFailedTransaction())
                .isSameAs(transaction);
    }

}