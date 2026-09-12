package pt.codeChallenge.payments.suppliers;

import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pt.codeChallenge.payments.enums.AmountRange;
import pt.codeChallenge.payments.enums.FeePeriod;
import pt.codeChallenge.payments.enums.TransactionFees;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

import static pt.codeChallenge.payments.enums.AmountRange.*;
import static pt.codeChallenge.payments.enums.FeePeriod.*;
import static pt.codeChallenge.payments.enums.TransactionFees.*;

@AllArgsConstructor
@Component
public class FeeSupplier {

    private static final HolidayManager HOLIDAY_MANAGER = HolidayManager.getInstance(ManagerParameters.create("PT"));

    public TransactionFees calculateFee(LocalDate scheduledDate, BigDecimal amount) {

        LocalDate today = LocalDate.now();

        long daysRemaining = today.datesUntil(scheduledDate).filter(this::isWorkDay).count();

        FeePeriod period = getPeriod(daysRemaining);
        return switch (getAmountRange(amount)) {
            case UP_TO_1000 -> {
                if (period == SAME_DAY) {
                    yield FEE_A;
                }
                throw new IllegalArgumentException("Transactions up to €1000 must be scheduled for today");
            }
            case FROM_1001_TO_2000 -> {
                if (period == TO_10_DAYS) {
                    yield FEE_B;
                }
                throw new IllegalArgumentException("Transactions between €1001 and €2000 must be scheduled between 1 and 10 days");
            }
            case OVER_2000 -> switch (period) {
                case TO_20_DAYS -> FEE_C_11_20;
                case TO_30_DAYS -> FEE_C_21_30;
                case TO_40_DAYS -> FEE_C_31_40;
                case OVER_40_DAYS -> FEE_C_40;
                default ->
                        throw new IllegalArgumentException("Transactions over €2000 must be scheduled at least 11 days ahead");
            };
        };
    }


    private boolean isWorkDay(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY && !HOLIDAY_MANAGER.isHoliday(date);
    }

    private FeePeriod getPeriod(long daysRemaining) {
        if (daysRemaining == 0) {
            return SAME_DAY;
        }
        if (daysRemaining <= 10) {
            return TO_10_DAYS;
        }
        if (daysRemaining <= 20) {
            return TO_20_DAYS;
        }
        if (daysRemaining <= 30) {
            return TO_30_DAYS;
        }
        if (daysRemaining <= 40) {
            return TO_40_DAYS;
        }

        return OVER_40_DAYS;
    }

    private AmountRange getAmountRange(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("1000.00")) <= 0) {
            return AmountRange.UP_TO_1000;
        }
        if (amount.compareTo(new BigDecimal("2000.00")) <= 0) {
            return AmountRange.FROM_1001_TO_2000;
        }
        return AmountRange.OVER_2000;
    }
}
