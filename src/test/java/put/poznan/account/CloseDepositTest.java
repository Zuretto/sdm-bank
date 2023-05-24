package put.poznan.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.interest.InterestMechanism;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CloseDepositTest {

    @Test
    void shouldCloseDeposit() {
        // given
        LocalDate mockedStartDate = LocalDate.of(2020, 1, 1);
        LocalDate mockedEndDate = LocalDate.of(2022, 1, 1);

        BigDecimal mockedBeginningBalance = new BigDecimal("20000");
        BigDecimal mockedDepositAmount = new BigDecimal("10000");
        BigDecimal mockedCalculatedInterest = new BigDecimal("350");
        BigDecimal expectedBalance = new BigDecimal("30350"); // beginning balance + deposit amount + calculated interest

        InterestMechanism interestMechanism = Mockito.mock(InterestMechanism.class);
        Mockito.when(interestMechanism.calculateInterest(mockedDepositAmount, mockedStartDate, mockedEndDate))
                .thenReturn(mockedCalculatedInterest);
        Account mockedAccount = new ClassicAccount(Mockito.mock(Person.class), "", interestMechanism);
        Deposit deposit = new Deposit(mockedStartDate, mockedEndDate, mockedAccount, mockedDepositAmount);
        mockedAccount.setBalance(mockedBeginningBalance);
        mockedAccount.addDeposit(deposit);
        // when
        Transaction transaction = new CloseDeposit(mockedAccount, deposit);
        transaction.execute();
        // then
        assertThat(mockedAccount).satisfies(
                account -> assertThat(account.getBalance()).isEqualTo(expectedBalance),
                account -> assertThat(account.getDeposits()).isEmpty(),
                account -> assertThat(account.getHistoryOfTransactions().getTransactions()).containsExactly(transaction)
        );
    }

    @Test
    void shouldCloseDepositPrematurely() {
        // given
        LocalDate mockedStartDate = LocalDate.of(2020, 1, 1);
        LocalDate mockedEndDate = LocalDate.now().plusDays(1);

        BigDecimal mockedBeginningBalance = new BigDecimal("20000");
        BigDecimal mockedDepositAmount = new BigDecimal("10000");
        BigDecimal mockedCalculatedInterest = new BigDecimal("350");
        BigDecimal expectedBalance = new BigDecimal("30000"); // beginning balance + deposit amount

        InterestMechanism interestMechanism = Mockito.mock(InterestMechanism.class);
        Mockito.when(interestMechanism.calculateInterest(mockedDepositAmount, mockedStartDate, mockedEndDate))
                .thenReturn(mockedCalculatedInterest);
        Account mockedAccount = new ClassicAccount(Mockito.mock(Person.class), "", interestMechanism);
        Deposit deposit = new Deposit(mockedStartDate, mockedEndDate, mockedAccount, mockedDepositAmount);
        mockedAccount.setBalance(mockedBeginningBalance);
        mockedAccount.addDeposit(deposit);
        // when
        Transaction transaction = new CloseDeposit(mockedAccount, deposit);
        transaction.execute();
        // then
        assertThat(mockedAccount).satisfies(
                account -> assertThat(account.getBalance()).isEqualTo(expectedBalance),
                account -> assertThat(account.getDeposits()).isEmpty(),
                account -> assertThat(account.getHistoryOfTransactions().getTransactions()).containsExactly(transaction)
        );
    }
}