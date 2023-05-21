package put.poznan.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HistoryOfTransactionsTest {

    HistoryOfTransactions cut;

    @BeforeEach
    void setUp() {
        cut = new HistoryOfTransactions();
    }

    @Test
    void shouldAddExecutedTransaction() {
        // given
        Transaction transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.isExecuted()).thenReturn(true);
        // when
        cut.addTransaction(transaction);
        // then
        assertThat(cut.getTransactions())
                .containsExactly(transaction);
    }

    @Test
    void shouldNotAddTransactionIfWasNotExecuted() {
        // given
        Transaction transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.isExecuted()).thenReturn(false);
        // when & then
        assertThatThrownBy(() -> cut.addTransaction(transaction))
                .isInstanceOf(IllegalStateException.class);
    }
}