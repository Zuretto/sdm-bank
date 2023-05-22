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
        Account classicAccount = new ClassicAccount(Mockito.mock(Person.class));
        Account debitAccount = new DebitAccount(classicAccount);

        classicAccount.setBalance(accountsBalance);
        debitAccount.setBalance(accountsBalance);
        // when
        boolean classicAccountResult = classicAccount.hasFunds(moneyToBeWithdrawn);
        boolean debitAccountResult = debitAccount.hasFunds(moneyToBeWithdrawn);
        // then
        assertThat(classicAccountResult).isEqualTo(expectedResult);
        assertThat(debitAccountResult).isEqualTo(true);
    }

    public static Stream<Arguments> provideCanMoneyBeWithdrawnTests() {
        return Stream.of(
                Arguments.of(new BigDecimal("100"), new BigDecimal("101"), false),
                Arguments.of(new BigDecimal("100"), new BigDecimal("100"), true),
                Arguments.of(new BigDecimal("100"), new BigDecimal("99"), true)
        );
    }
}