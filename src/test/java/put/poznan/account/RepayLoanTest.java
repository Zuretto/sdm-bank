package put.poznan.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.interest.InterestMechanism;
import put.poznan.transaction.FailedTransaction;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RepayLoanTest {

    @Test
    void shouldRepayLoan() {
        // given
        LocalDate mockedStartDate = LocalDate.of(2020, 1, 1);
        LocalDate mockedEndDate = LocalDate.of(2022, 1, 1);

        BigDecimal mockedBeginningBalance = new BigDecimal("20000");
        BigDecimal mockedLoanAmount = new BigDecimal("10000");
        BigDecimal mockedCalculatedInterest = new BigDecimal("350");
        BigDecimal expectedBalance = new BigDecimal("9650"); // beginning balance - loan amount - calculated interest

        InterestMechanism interestMechanism = Mockito.mock(InterestMechanism.class);
        Mockito.when(interestMechanism.calculateInterest(mockedLoanAmount, mockedStartDate, mockedEndDate))
                .thenReturn(mockedCalculatedInterest);
        Account mockedAccount = new StandardAccount(Mockito.mock(Person.class), "", interestMechanism);
        Loan loan = new Loan(mockedStartDate, mockedEndDate, mockedAccount, mockedLoanAmount);
        mockedAccount.setBalance(mockedBeginningBalance);
        mockedAccount.addLoan(loan);
        // when
        Transaction transaction = new RepayLoan(mockedAccount, loan);
        transaction.execute();
        // then
        assertThat(mockedAccount).satisfies(
                account -> assertThat(account.getBalance()).isEqualTo(expectedBalance),
                account -> assertThat(account.getLoans()).isEmpty(),
                account -> assertThat(account.getHistoryOfTransactions().getTransactions()).containsExactly(transaction)
        );
    }

    @Test
    void shouldFailRepayingLoan() {
        // given
        LocalDate mockedStartDate = LocalDate.of(2020, 1, 1);
        LocalDate mockedEndDate = LocalDate.of(2022, 1, 1);

        BigDecimal mockedBeginningBalance = new BigDecimal("9649");
        BigDecimal mockedLoanAmount = new BigDecimal("10000");
        BigDecimal mockedCalculatedInterest = new BigDecimal("350");

        InterestMechanism interestMechanism = Mockito.mock(InterestMechanism.class);
        Mockito.when(interestMechanism.calculateInterest(mockedLoanAmount, mockedStartDate, mockedEndDate))
                .thenReturn(mockedCalculatedInterest);
        Account mockedAccount = new StandardAccount(Mockito.mock(Person.class), "", interestMechanism);
        Loan loan = new Loan(mockedStartDate, mockedEndDate, mockedAccount, mockedLoanAmount);
        mockedAccount.setBalance(mockedBeginningBalance);
        mockedAccount.addLoan(loan);
        // when
        Transaction transaction = new RepayLoan(mockedAccount, loan);
        assertThatThrownBy(transaction::execute)
                .isInstanceOf(IllegalStateException.class);
        // then
        assertThat(mockedAccount).satisfies(
                account -> assertThat(account.getBalance()).isEqualTo(mockedBeginningBalance),
                account -> assertThat(account.getLoans()).containsExactly(loan),
                account -> assertThat(account.getHistoryOfTransactions().getTransactions())
                        .hasSize(1),
                account -> assertThat(account.getHistoryOfTransactions().getTransactions().get(0))
                        .extracting(failedTransaction -> (FailedTransaction)failedTransaction)
                        .satisfies(
                                failedTransaction -> assertThat(failedTransaction.getCause())
                                        .isInstanceOf(IllegalStateException.class),
                                failedTransaction -> assertThat(failedTransaction.getFailedTransaction())
                                        .isSameAs(transaction)
                        ));
    }
}