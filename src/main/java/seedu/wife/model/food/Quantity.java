package seedu.wife.model.food;

import static java.util.Objects.requireNonNull;
import static seedu.wife.commons.util.AppUtil.checkArgument;

/**
 * Represents a Food's quantity in the fridge.
 * Guarantees: mutable with {@link #updateQuantity(Integer)}; quantity is validated in {@link #isValid(Integer)}
 */
public class Quantity {
    public static final String MESSAGE_CONSTRAINTS = "Quantity must be a value larger than 0";
    private Integer quantity;

    /**
     * Construct a {@code Quantity}
     *
     * @param quantity Number of food item in the fridge.
     */
    public Quantity(Integer quantity) {
        requireNonNull(quantity);
        checkArgument(isValid(quantity), MESSAGE_CONSTRAINTS);
        this.quantity = quantity;
    }

    /**
     * Getter method to get the quantity amount.
     *
     * @return The quantity value.
     */
    public Integer getValue() {
        return this.quantity;
    }

    /**
     * Returns true if the quantity is of value more than 0.
     *
     * @param quantity Number of item user wish to add into the fridge.
     * @return True if quantity is a valid quantity, else False.
     */
    public static boolean isValid(Integer quantity) {
        return quantity > 0;
    }

    /**
     * Updates the quantity to another valid integer.
     *
     * @param newQuantity Value of new quantity.
     */
    public void updateQuantity(Integer newQuantity) {
        checkArgument(isValid(newQuantity), MESSAGE_CONSTRAINTS);
        this.quantity = newQuantity;
    }

    @Override
    public String toString() {
        return this.quantity.toString();
    }
}