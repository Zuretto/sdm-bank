package put.poznan.interest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ThresholdInterest implements InterestMechanism {

    private final BigDecimal rateBelowThreshold;
    private final BigDecimal rateOverThreshold;
    private final BigDecimal threshold;
    private final int interestPeriod;

    public ThresholdInterest(BigDecimal rateBelowThreshold, BigDecimal rateOverThreshold, BigDecimal threshold, int interestPeriod) {
        this.rateBelowThreshold = rateBelowThreshold;
        this.rateOverThreshold = rateOverThreshold;
        this.threshold = threshold;
        this.interestPeriod = interestPeriod;
    }

    @Override
    public BigDecimal calculateInterest(BigDecimal amount, LocalDate from, LocalDate to) {
        final long monthsBetween = ChronoUnit.MONTHS.between(from, to);
        BigDecimal interest;
        if (amount.compareTo(threshold) < 0)
            interest = amount.multiply(rateBelowThreshold);
        else
            interest = threshold
                    .multiply(rateBelowThreshold)
                    .add(amount.subtract(threshold).multiply(rateOverThreshold));
        return interest.multiply(new BigDecimal(monthsBetween / interestPeriod));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThresholdInterest that)) return false;
        return interestPeriod == that.interestPeriod
                && Objects.equals(rateBelowThreshold, that.rateBelowThreshold)
                && Objects.equals(rateOverThreshold, that.rateOverThreshold)
                && Objects.equals(threshold, that.threshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rateBelowThreshold, rateOverThreshold, threshold, interestPeriod);
    }
}
