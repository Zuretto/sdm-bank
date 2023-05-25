package put.poznan.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import put.poznan.interest.InterestMechanism;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountFactoryTest {
    @Test
    public void testCreateAccountMethod() {
        Person person = new Person("Jan", "Kowalski", "test@email.com");

        Account standardAccount = AccountFactory.createAccount("standard", person, "123", Mockito.mock(InterestMechanism.class));
        Account debitAccount = AccountFactory.createAccount("debit", person, "123", Mockito.mock(InterestMechanism.class));

        assert standardAccount instanceof StandardAccount;
        assert debitAccount instanceof DebitAccount;
        assertThrows(
                RuntimeException.class,
                () -> AccountFactory.createAccount("unknown", person, "123", Mockito.mock(InterestMechanism.class))
        );
    }
}
