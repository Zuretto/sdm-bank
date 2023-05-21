package put.poznan.account;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @ParameterizedTest
    @MethodSource("provideCanMoneyBeWithdrawnTests")
    void testCanMoneyBeWithdrawnMethod(BigDecimal accountsBalance, BigDecimal moneyToBeWithdrawn, boolean expectedResult) {
        // given
        Account account = new Account(Mockito.mock(Person.class));
        account.setBalance(accountsBalance);
        // when
        boolean result = account.hasFunds(moneyToBeWithdrawn);
        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> provideCanMoneyBeWithdrawnTests() {
        return Stream.of(
                Arguments.of(new BigDecimal("100"), new BigDecimal("101"), false),
                Arguments.of(new BigDecimal("100"), new BigDecimal("100"), true),
                Arguments.of(new BigDecimal("100"), new BigDecimal("99"), true)
        );
    }
}