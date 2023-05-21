package put.poznan.visitor;

import org.junit.jupiter.api.Test;
import put.poznan.account.Account;
import put.poznan.account.ClassicAccount;
import put.poznan.account.Person;
import put.poznan.reporter.XMLReporter;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    void singleAccountReportTest() {
        XMLReporter reporter = new XMLReporter();
        Account account = new ClassicAccount(
                new Person("test_name", "test_number", "test@test.com"), "");

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
                    </account>
                </accounts>""";

        assert expected.equals(reporter.export(account));
    }

    @Test
    void multipleAccountsReportTest() {
        Account account1 = new ClassicAccount(
                new Person("test_name1", "test_number1", "test1@test.com"), "");
        account1.setBalance(new BigDecimal(2000));
        Account account2 = new ClassicAccount(
                new Person("test_name2", "test_number2", "test2@test.com"), "");
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
                    </account>
                </accounts>""";

        assert expected.equals(report);
    }
}
