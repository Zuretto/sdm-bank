package put.poznan.interest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.account.Account;
import put.poznan.account.ClassicAccount;
import put.poznan.account.Person;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ThresholdInterestTest {

    @Test
    void calculateInterestBelowThresholdTest() {
        // given
        Account account = new ClassicAccount(Mockito.mock(Person.class), "", new ThresholdInterest(
                BigDecimal.valueOf(0.03),
                BigDecimal.valueOf(0.08),
                BigDecimal.valueOf(8_000),
                6));

        // when
        BigDecimal result = account.getInterestMechanism().calculateInterest(
                BigDecimal.valueOf(5_000),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1));

        // then
        assertEquals(0, result.compareTo(BigDecimal.valueOf(300)));
    }

    @Test
    void calculateInterestOverThresholdTest() {
        // given
        Account account = new ClassicAccount(Mockito.mock(Person.class), "", new ThresholdInterest(
                BigDecimal.valueOf(0.03),
                BigDecimal.valueOf(0.08),
                BigDecimal.valueOf(8_000),
                6));

        // when
        BigDecimal result = account.getInterestMechanism().calculateInterest(
                BigDecimal.valueOf(10_000),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1));

        // then
        assertEquals(0, result.compareTo(BigDecimal.valueOf(800)));
    }
}