package put.poznan.account;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.interest.LinearInterest;
import put.poznan.interest.ThresholdInterest;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeInterestMechanismTest {

    @Test
    void shouldChangeInterestMechanismToDifferentMechanism() {
        // given
        Account account = new ClassicAccount(Mockito.mock(Person.class), "", new LinearInterest(BigDecimal.ONE, 10));

        Transaction changeInterestMechanism = new ChangeInterestMechanism(account, new ThresholdInterest(
                BigDecimal.valueOf(0.03),
                BigDecimal.valueOf(0.08),
                BigDecimal.valueOf(20000),
                3));
        // when
        changeInterestMechanism.execute();
        // then
        assertThat(account.getInterestMechanism()).isInstanceOf(ThresholdInterest.class);
        assertThat(account.getHistoryOfTransactions().getTransactions()).containsExactly(changeInterestMechanism);
    }

    @Test
    void shouldChangeInterestMechanismToSameMechanismWithDifferentParameters() {
        // given
        Account classicAccount = new ClassicAccount(Mockito.mock(Person.class), "", new LinearInterest(BigDecimal.valueOf(0.03), 3));
        Account debitAccount = new DebitAccount(classicAccount);

        Transaction changeInterestMechanism = new ChangeInterestMechanism(debitAccount, new LinearInterest(BigDecimal.valueOf(0.05), 6));
        // when
        changeInterestMechanism.execute();
        // then
        assertThat(debitAccount.getInterestMechanism()).isInstanceOf(LinearInterest.class);
        assertThat(debitAccount.getHistoryOfTransactions().getTransactions()).containsExactly(changeInterestMechanism);
        assertThat((LinearInterest) debitAccount.getInterestMechanism()).satisfies(
                mechanism -> assertEquals(mechanism.getInterestRate(), BigDecimal.valueOf(0.05)),
                mechanism -> assertEquals(mechanism.getInterestPeriod(),6)
        );
    }
}