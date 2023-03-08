package seedu.wife.model.food;

import static java.util.Objects.requireNonNull;
import static seedu.wife.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;

/**
 * Represents a Food's expiry date.
 * Guarantees: Immutable; expiry date is validated in {@link #isValidDate(LocalDate)}
 */
public class ExpiryDate {
    public static final String MESSAGE_CONSTRAINTS = "The food you wish to add is already expired.";
    public static final String FORMAT_MESSAGE_CONSTRAINTS = "Format of your date is incorrect. "
            + "Please insert using the format DD-MM-YYYY";
    public static final String VALIDATION_REGEX = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
    private final LocalDate expiryDate;

    /**
     * Constructs an {@code ExpiryDate}
     *
     * @param expiryDate Expiry date of the food item wish to add in the fridge.
     */
    public ExpiryDate(LocalDate expiryDate) {
        requireNonNull(expiryDate);
        checkArgument(isValidDate(expiryDate), MESSAGE_CONSTRAINTS);
        this.expiryDate = expiryDate;
    }

    /**
     * Construct an {@code ExpiryDate} from String
     *
     * @param date Expiry date of the food item wish to add in the fridge.
     */
    public ExpiryDate(String date) {
        requireNonNull(date);
        checkArgument(isValid(date), FORMAT_MESSAGE_CONSTRAINTS);
        this.expiryDate = LocalDate.parse(date);
    }

    public static boolean isValid(String date) {
        return date.matches(VALIDATION_REGEX);
    }

    /**
     * Validate if the expiry date inserted is after the current date.
     *
     * @param expiryDate Expiry date entered by the user.
     * @return True if the expiry date is after the current date, else False.
     */
    public static boolean isValidDate(LocalDate expiryDate) {
        LocalDate dateNow = LocalDate.now();
        return expiryDate.isAfter(dateNow);
    }

    @Override
    public boolean equals(Object otherDate) {
        return otherDate == this
                || (otherDate instanceof ExpiryDate
                && this.expiryDate.equals((ExpiryDate) otherDate));
    }

    @Override
    public String toString() {
        return this.expiryDate.toString();
    }
}