package put.poznan.interest;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InterestMechanism {

    BigDecimal calculateInterest(final BigDecimal amount,
                                 final LocalDate from,
                                 final LocalDate to);
}
