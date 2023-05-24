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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReporterTest {
    @Test
    void emptyReportTest() {
        XMLReporter reporter = new XMLReporter();
        String report = reporter.export();

        String expected = """
                <?xml version="1.0" encoding="UTF-8"?>
                <accounts>
                </accounts>""";

        assert expected.equals(report);
    }

    @Test
    void singleAccountReceivePaymentsReportTest() {
        final var bank1 = new Bank("0001");
        final var account1 = new ClassicAccount(new Person("test_name", "test_number", "test@test.com"), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        final var account2 = new ClassicAccount(new Person("test_name", "test_number", "test@test.com"), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
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
        String report = reporter.export(account2);

        final var expected = """
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
                        <payments>
                           <payment>
                               <sender>000100000000</sender>
                               <receiver>000100000001</phoneNumber>
                               <amount>100</email>
                           </payment>
                        </payments>
                    </account>
                </accounts>""";
        assertEquals(report, expected);
    }

    @Test
    void singleAccountMakePaymentsReportTest() {
        final var bank1 = new Bank("0001");
        final var account1 = new ClassicAccount(new Person("test_name", "test_number", "test@test.com"), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
        final var account2 = new ClassicAccount(new Person("test_name", "test_number", "test@test.com"), bank1.getNextId(), Mockito.mock(InterestMechanism.class));
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
        String report = reporter.export(account1);
        final var expected = """
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
                        <payments>
                           <payment>
                               <sender>000100000000</sender>
                               <receiver>000100000001</phoneNumber>
                               <amount>100</email>
                               <status>COMPLETED</status>
                           </payment>
                        </payments>
                    </account>
                </accounts>""";
        assertEquals(report, expected);
    }

    @Test
    void singleAccountReportTest() {
        XMLReporter reporter = new XMLReporter();
        Account account = new ClassicAccount(
                new Person("test_name", "test_number", "test@test.com"), "", Mockito.mock(InterestMechanism.class));

        account.setBalance(new BigDecimal(1000));
        account.openLoan(new BigDecimal(1000), LocalDate.of(2024, 12, 31), new BigDecimal(2111), 3);

        String expected = """
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
                                <startDate>2023-05-22</startDate>
                                <endDate>2024-12-31</endDate>
                            </loan>
                        </loans>
                        <payments>
                        </payments>
                    </account>
                </accounts>""";

        assert expected.equals(reporter.export(account));
    }

    @Test
    void multipleAccountsReportTest() {
        Account account1 = new ClassicAccount(
                new Person("test_name1", "test_number1", "test1@test.com"), "", Mockito.mock(InterestMechanism.class));
        account1.setBalance(new BigDecimal(2000));
        Account account2 = new ClassicAccount(
                new Person("test_name2", "test_number2", "test2@test.com"), "", Mockito.mock(InterestMechanism.class));
        account2.setBalance(new BigDecimal(4000));
        account1.openDeposit(
                new BigDecimal(1000),
                LocalDate.of(2024, 12, 31),
                new BigDecimal(2111),
                3
        );
        account2.openLoan(
                new BigDecimal(1000),
                LocalDate.of(2024, 12, 31),
                new BigDecimal(2111),
                3
        );
        XMLReporter reporter = new XMLReporter();
        String report = reporter.export(account1, account2);

        String expected = """
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
                                <startDate>2023-05-22</startDate>
                                <endDate>2024-12-31</endDate>
                            </deposit>
                        </deposits>
                        <loans>
                        </loans>
                        <payments>
                        </payments>
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
                                <startDate>2023-05-22</startDate>
                                <endDate>2024-12-31</endDate>
                            </loan>
                        </loans>
                        <payments>
                        </payments>
                    </account>
                </accounts>""";

        assert expected.equals(report);
    }
}
