package put.poznan.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.interest.InterestMechanism;
import put.poznan.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class OpenLoanTest {

    @Test
    void shouldOpenLoan() {
        // given
        Account account = new StandardAccount(Mockito.mock(Person.class), "");
        account.setBalance(new BigDecimal("1000"));
        Transaction transaction = new OpenLoan(
                account,
                new BigDecimal("100"),
                LocalDate.of(2024, 12, 31)
        );
        // when
        transaction.execute();
        // then
        assertThat(account)
                .extracting(Account::getLoans)
                .satisfies(loans -> assertThat(loans).hasSize(1))
                .extracting(loans -> loans.get(0))
                .satisfies(
                        loan -> assertThat(loan.getAmount()).isEqualTo(new BigDecimal("100")),
                        loan -> assertThat(loan.getAccount()).isSameAs(account),
                        loan -> assertThat(loan.getStartDate()).isEqualTo(LocalDate.now()),
                        loan -> assertThat(loan.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31))
                );
    }
}