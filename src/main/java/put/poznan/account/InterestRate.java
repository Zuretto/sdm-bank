package put.poznan.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class InterestRate {

    private final BigDecimal rate;

    /**
     * Period (in months) that the
     */
    private final int interestPeriod;

    public InterestRate(BigDecimal interestRate, int interestPeriod) {
        this.rate = interestRate;
        this.interestPeriod = interestPeriod;
    }

    public BigDecimal getInterestRate() {
        return rate;
    }

    public int getInterestPeriod() {
        return interestPeriod;
    }

    /**
     * Calculates linear interest based on given amount, from- and to dates, rate and interest period (kept as class values).
     *
     * @param amount amount on which the interest is calculated
     * @param from   date from which the interest is started
     * @param to     date to which the interest should be calculated
     * @return calculated interest
     */
    public BigDecimal calculateInterest(final BigDecimal amount,
                                        final LocalDate from,
                                        final LocalDate to) {
        final int monthsBetween = from.until(to).getMonths();
        return amount
                .multiply(rate)
                .multiply(new BigDecimal(monthsBetween / interestPeriod));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InterestRate that)) return false;
        return interestPeriod == that.interestPeriod && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rate, interestPeriod);
    }
}
