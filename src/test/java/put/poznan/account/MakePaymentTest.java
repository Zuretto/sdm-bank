package put.poznan.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.Bank;
import put.poznan.interbank.InterbankPaymentAgency;
import put.poznan.interbank.PaymentStatus;
import put.poznan.interest.InterestMechanism;
import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MakePaymentTest {

    @Test
    void shouldTransferMoneyInterBank() {
        final var bank1 = new Bank("0001");
        final var bank2 = new Bank("0002");
        final var account1 = new StandardAccount(Mockito.mock(Person.class), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        final var account2 = new StandardAccount(Mockito.mock(Person.class), bank2.getNextId(), Mockito.mock(InterestMechanism.class));
        account1.setBalance(new BigDecimal("100"));
        bank1.addAccount(account1);
        bank2.addAccount(account2);
        final var interbankPaymentAgency = new InterbankPaymentAgency();
        interbankPaymentAgency.addBank(bank1);
        interbankPaymentAgency.addBank(bank2);

        final var transaction = new MakePayment(bank1, account1, new BigDecimal("100"), account2.getAccountNumber(), interbankPaymentAgency);
        transaction.execute();
        interbankPaymentAgency.processPayments();

        assertThat(account1)
                .extracting(Account::getBalance)
                .isEqualTo(new BigDecimal("0"));
        assertThat(account2)
                .extracting(Account::getBalance)
                .isEqualTo(new BigDecimal("100"));

        assertThat(account1)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(MakePayment.class);
                    assertThat((MakePayment) transactions.get(0)).extracting(MakePayment::getStatus).isEqualTo(PaymentStatus.COMPLETED);
                    return true;
                });
        assertThat(account2)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(ReceivePayment.class);
                    return true;
                });
    }

    @Test
    void interbankShouldBePendingBeforeProcessingPayments() {
        final var bank1 = new Bank("0001");
        final var bank2 = new Bank("0002");
        final var account1 = new StandardAccount(Mockito.mock(Person.class), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        final var account2 = new StandardAccount(Mockito.mock(Person.class), bank2.getNextId(), Mockito.mock(InterestMechanism.class));
        account1.setBalance(new BigDecimal("100"));
        bank1.addAccount(account1);
        bank2.addAccount(account2);
        final var interbankPaymentAgency = new InterbankPaymentAgency();
        interbankPaymentAgency.addBank(bank1);
        interbankPaymentAgency.addBank(bank2);

        final var transaction = new MakePayment(bank1, account1, new BigDecimal("100"), account2.getAccountNumber(), interbankPaymentAgency);
        transaction.execute();

        assertThat(account1)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(MakePayment.class);
                    assertThat((MakePayment) transactions.get(0)).extracting(MakePayment::getStatus).isEqualTo(PaymentStatus.PENDING);
                    return true;
                });
    }

    @Test
    void shouldTransferMoneyInnerBank() {
        final var bank1 = new Bank("0001");
        final var account1 = new StandardAccount(Mockito.mock(Person.class), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        final var account2 = new StandardAccount(Mockito.mock(Person.class), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        account1.setBalance(new BigDecimal("100"));
        bank1.addAccount(account1);
        bank1.addAccount(account2);

        final var transaction = new MakePayment(bank1, account1, new BigDecimal("100"), account2.getAccountNumber(), null);
        transaction.execute();

        assertThat(account1)
                .extracting(Account::getBalance)
                .isEqualTo(new BigDecimal("0"));
        assertThat(account2)
                .extracting(Account::getBalance)
                .isEqualTo(new BigDecimal("100"));

        assertThat(account1)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(MakePayment.class);
                    assertThat((MakePayment) transactions.get(0)).extracting(MakePayment::getStatus).isEqualTo(PaymentStatus.COMPLETED);
                    return true;
                });
        assertThat(account2)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(ReceivePayment.class);
                    return true;
                });
    }

    @Test
    void whenWrongAccountNumberSetShouldSetInvalidAccountNumberStatus() {
        final var bank1 = new Bank("0001");
        final var bank2 = new Bank("0002");
        final var account1 = new StandardAccount(Mockito.mock(Person.class), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        account1.setBalance(new BigDecimal("100"));
        bank1.addAccount(account1);
        final var interbankPaymentAgency = new InterbankPaymentAgency();
        interbankPaymentAgency.addBank(bank1);
        interbankPaymentAgency.addBank(bank2);

        final var transaction = new MakePayment(bank1, account1, new BigDecimal("100"), "00021", interbankPaymentAgency);
        transaction.execute();
        interbankPaymentAgency.processPayments();

        assertThat(account1)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(MakePayment.class);
                    assertThat((MakePayment) transactions.get(0)).extracting(MakePayment::getStatus).isEqualTo(PaymentStatus.ACCOUNT_NOT_FOUND);
                    return true;
                });
    }

    @Test
    void whenBankDoesNotExistShouldSetInvalidAccountNumberStatus() {
        final var bank1 = new Bank("0001");
        final var account1 = new StandardAccount(Mockito.mock(Person.class), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        account1.setBalance(new BigDecimal("100"));
        bank1.addAccount(account1);
        final var interbankPaymentAgency = new InterbankPaymentAgency();
        interbankPaymentAgency.addBank(bank1);

        final var transaction = new MakePayment(bank1, account1, new BigDecimal("100"), "00021", interbankPaymentAgency);
        transaction.execute();
        interbankPaymentAgency.processPayments();

        assertThat(account1)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(MakePayment.class);
                    assertThat((MakePayment) transactions.get(0)).extracting(MakePayment::getStatus).isEqualTo(PaymentStatus.ACCOUNT_NOT_FOUND);
                    return true;
                });
    }

    @Test
    void whenTransferAmountIsTooBigThenSetStatusTooInsufficientFund() {
        final var bank1 = new Bank("0001");
        final var account1 = new StandardAccount(Mockito.mock(Person.class), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        final var account2 = new StandardAccount(Mockito.mock(Person.class), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        bank1.addAccount(account1);
        bank1.addAccount(account2);

        final var transaction = new MakePayment(bank1, account1, new BigDecimal("100"), account2.getAccountNumber(), null);
        transaction.execute();

        assertThat(account1)
                .extracting(Account::getBalance)
                .isEqualTo(new BigDecimal("0"));
        assertThat(account2)
                .extracting(Account::getBalance)
                .isEqualTo(new BigDecimal("0"));

        assertThat(account1)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(MakePayment.class);
                    assertThat((MakePayment) transactions.get(0)).extracting(MakePayment::getStatus).isEqualTo(PaymentStatus.INSUFFICIENT_FUND);
                    return true;
                });
        assertThat(account2)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(0);
                    return true;
                });
    }
}