package put.poznan.interest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.account.Account;
import put.poznan.account.Person;
import put.poznan.account.StandardAccount;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinearInterestTest {

    @Test
    void calculateInterestTest() {
        // given
        Account account = new StandardAccount(Mockito.mock(Person.class), "", new LinearInterest(BigDecimal.valueOf(0.03), 3));

        // when
        BigDecimal result = account.getInterestMechanism().calculateInterest(
                BigDecimal.valueOf(10_000),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1));

        // then
        assertEquals(0, result.compareTo(BigDecimal.valueOf(1200)));
    }
}