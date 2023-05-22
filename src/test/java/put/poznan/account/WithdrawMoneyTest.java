package put.poznan.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.transaction.FailedTransaction;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WithdrawMoneyTest {

    @Test
    void shouldWithdrawMoney() {
        // given
        ClassicAccount account = new ClassicAccount(Mockito.mock(Person.class), "");
        account.setBalance(new BigDecimal("100"));
        Transaction transaction = new WithdrawMoney(account, new BigDecimal("99"));
        // when
        transaction.execute();
        // then
        assertThat(account.getBalance())
                .isEqualTo(new BigDecimal("1"));
        assertThat(account.getHistoryOfTransactions().getTransactions()).containsExactly(transaction);
        assertThat(transaction.isExecuted()).isTrue();
    }

    @Test
    void shouldWithdrawMoneyDebitAccount() {
        Account account = new DebitAccount(new ClassicAccount(Mockito.mock(Person.class), ""));
        account.setBalance(new BigDecimal("100"));
        Transaction transaction = new WithdrawMoney(account, new BigDecimal("101"));

        transaction.execute();

        assertThat(account.getBalance())
                .isEqualTo(new BigDecimal("-1"));
        assertThat(account.getHistoryOfTransactions().getTransactions()).containsExactly(transaction);
        assertThat(transaction.isExecuted()).isTrue();
    }

    @Test
    void shouldNotWithdrawMoney() {
        // given
        ClassicAccount account = new ClassicAccount(Mockito.mock(Person.class), "");
        account.setBalance(new BigDecimal("100"));
        Transaction transaction = new WithdrawMoney(account, new BigDecimal("101"));
        // when
        assertThatThrownBy(transaction::execute)
                .isInstanceOf(IllegalStateException.class);
        // then
        assertThat(account.getBalance())
                .isEqualTo(new BigDecimal("100"));
        assertThat(account.getHistoryOfTransactions().getTransactions().get(0))
                .isInstanceOf(FailedTransaction.class)
                .satisfies(failedTransaction -> assertThat(((FailedTransaction)failedTransaction).getCause())
                        .isInstanceOf(IllegalStateException.class))
                .extracting(failedTransaction -> ((FailedTransaction)failedTransaction).getFailedTransaction())
                .isSameAs(transaction);
        assertThat(transaction.isExecuted()).isTrue();
    }
}