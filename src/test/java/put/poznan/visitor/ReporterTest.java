package put.poznan.visitor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.Bank;
import put.poznan.account.*;
import put.poznan.interest.InterestMechanism;
import put.poznan.reporter.XMLReporter;
import put.poznan.transaction.HistoryOfTransactions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReporterTest {
    @Test
    void emptyReportTest() {
        XMLReporter reporter = new XMLReporter();
        String report = reporter.exportAccounts(new ArrayList<>());

        String expected = """
                <?xml version="1.0" encoding="UTF-8"?>
                <accounts>
                </accounts>""";

        assert expected.equals(report);
    }

    @Test
    void singleAccountReceivePaymentsReportTest() {
        final var bank1 = new Bank("0001");
        final var account1 = new StandardAccount(new Person("test_name", "test_number", "test@test.com"), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        final var account2 = new StandardAccount(new Person("test_name", "test_number", "test@test.com"), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
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
                    return true;
                });
        assertThat(account2)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(ReceivePayment.class);
                    return true;
                });


        XMLReporter reporter = new XMLReporter();
        String report = reporter.exportAccounts(List.of(account2));

        final var expected = String.format("""
                <?xml version="1.0" encoding="UTF-8"?>
                <accounts>
                    <account>
                        <balance>100</balance>
                        <owner>
                            <name>test_name</name>
                            <phoneNumber>test_number</phoneNumber>
                            <email>test@test.com</email>
                        </owner>
                        <deposits>
                        </deposits>
                        <loans>
                        </loans>
                        <transactions>
                            <transaction>
                                <transactionType>RECEIVE_PAYMENT</transactionType>
                                <description>Transaction to receive payment for from account number: 000100000000 to account: %s with amount: 100</description>
                                <dateOfExecution>%s</dateOfExecution>
                            </transaction>
                        </transactions>
                    </account>
                </accounts>""", account2, LocalDate.now());
        assertEquals(expected, report);
    }

    @Test
    void singleAccountMakePaymentsReportTest() {
        final var bank1 = new Bank("0001");
        final var account1 = new StandardAccount(new Person("test_name", "test_number", "test@test.com"), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        final var account2 = new StandardAccount(new Person("test_name", "test_number", "test@test.com"), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
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
                    return true;
                });
        assertThat(account2)
                .extracting(Account::getHistoryOfTransactions)
                .extracting(HistoryOfTransactions::getTransactions).matches(transactions -> {
                    assertThat(transactions).hasSize(1);
                    assertThat(transactions.get(0)).isInstanceOf(ReceivePayment.class);
                    return true;
                });


        XMLReporter reporter = new XMLReporter();
        String report = reporter.exportAccounts(List.of(account1));
        final var expected = String.format("""
                <?xml version="1.0" encoding="UTF-8"?>
                <accounts>
                    <account>
                        <balance>0</balance>
                        <owner>
                            <name>test_name</name>
                            <phoneNumber>test_number</phoneNumber>
                            <email>test@test.com</email>
                        </owner>
                        <deposits>
                        </deposits>
                        <loans>
                        </loans>
                        <transactions>
                            <transaction>
                                <transactionType>MAKE_PAYMENT</transactionType>
                                <description>Transaction to make payment from sender: %s to receiver: 000100000001 with amount: 100</description>
                                <dateOfExecution>%s</dateOfExecution>
                            </transaction>
                        </transactions>
                    </account>
                </accounts>""", account1, LocalDate.now());
        assertEquals(expected, report);
    }

    @Test
    void singleAccountReportTest() {
        XMLReporter reporter = new XMLReporter();
        Account account = new StandardAccount(
                new Person("test_name", "test_number", "test@test.com"), "", Mockito.mock(InterestMechanism.class));

        account.setBalance(new BigDecimal(1000));
        account.openLoan(new BigDecimal(1000), LocalDate.now().plusYears(1), new BigDecimal(2111), 3);

        String expected = String.format("""
                <?xml version="1.0" encoding="UTF-8"?>
                <accounts>
                    <account>
                        <balance>2000</balance>
                        <owner>
                            <name>test_name</name>
                            <phoneNumber>test_number</phoneNumber>
                            <email>test@test.com</email>
                        </owner>
                        <deposits>
                        </deposits>
                        <loans>
                            <loan>
                                <amount>1000</amount>
                                <startDate>%s</startDate>
                                <endDate>%s</endDate>
                            </loan>
                        </loans>
                        <transactions>
                        </transactions>
                    </account>
                </accounts>""", LocalDate.now(), LocalDate.now().plusYears(1));

        assert expected.equals(reporter.exportAccounts(List.of(account)));
    }

    @Test
    void multipleAccountsReportTest() {
        LocalDate today = LocalDate.now();
        LocalDate inOneYear = today.plusYears(1);
        Account account1 = new StandardAccount(
                new Person("test_name1", "test_number1", "test1@test.com"), "", Mockito.mock(InterestMechanism.class));
        account1.setBalance(new BigDecimal(2000));
        Account account2 = new StandardAccount(
                new Person("test_name2", "test_number2", "test2@test.com"), "", Mockito.mock(InterestMechanism.class));
        account2.setBalance(new BigDecimal(4000));
        account1.openDeposit(
                new BigDecimal(1000),
                inOneYear,
                new BigDecimal(2111),
                3
        );
        account2.openLoan(
                new BigDecimal(1000),
                inOneYear,
                new BigDecimal(2111),
                3
        );
        XMLReporter reporter = new XMLReporter();
        String report = reporter.exportAccounts(List.of(account1, account2));

        String expected = String.format("""
                <?xml version="1.0" encoding="UTF-8"?>
                <accounts>
                    <account>
                        <balance>1000</balance>
                        <owner>
                            <name>test_name1</name>
                            <phoneNumber>test_number1</phoneNumber>
                            <email>test1@test.com</email>
                        </owner>
                        <deposits>
                            <deposit>
                                <amount>1000</amount>
                                <startDate>%s</startDate>
                                <endDate>%s</endDate>
                            </deposit>
                        </deposits>
                        <loans>
                        </loans>
                        <transactions>
                        </transactions>
                    </account>
                    <account>
                        <balance>5000</balance>
                        <owner>
                            <name>test_name2</name>
                            <phoneNumber>test_number2</phoneNumber>
                            <email>test2@test.com</email>
                        </owner>
                        <deposits>
                        </deposits>
                        <loans>
                            <loan>
                                <amount>1000</amount>
                                <startDate>%s</startDate>
                                <endDate>%s</endDate>
                            </loan>
                        </loans>
                        <transactions>
                        </transactions>
                    </account>
                </accounts>""",
                today,
                inOneYear,
                today,
                inOneYear);

        assert expected.equals(report);
    }

    @Test
    void multipleTransactionsReportTest() {
        LocalDate today = LocalDate.now();
        LocalDate inOneYear = today.plusYears(1);
        Account account = new StandardAccount(
                new Person("test_name1", "test_number1", "test1@test.com"), "", Mockito.mock(InterestMechanism.class));
        account.setBalance(new BigDecimal(2000));
        new OpenDeposit(account,
                new BigDecimal(200),
                inOneYear).execute();
        Deposit deposit = account.getDeposits().get(0);
        new CloseDeposit(
                account,
                deposit
        ).execute();
        XMLReporter reporter = new XMLReporter();
        String report = reporter.exportTransactions(account.getHistoryOfTransactions().getTransactions());

        String expected = String.format("""
                <?xml version="1.0" encoding="UTF-8"?>
                <transactions>
                            <transaction>
                                <transactionType>OPEN_DEPOSIT</transactionType>
                                <description>Transaction to open deposit for account: %s, amount: 200 and end date: %s</description>
                                <dateOfExecution>%s</dateOfExecution>
                            </transaction>
                            <transaction>
                                <transactionType>CLOSE_DEPOSIT</transactionType>
                                <description>Transaction to close deposit. Deposit: %s, account: %s</description>
                                <dateOfExecution>%s</dateOfExecution>
                            </transaction>
                </transactions>""",
                account,
                inOneYear,
                today,
                deposit,
                account,
                today);
        assertThat(report).isEqualTo(expected);
    }

}
