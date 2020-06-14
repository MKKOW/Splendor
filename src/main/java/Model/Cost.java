package Model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Cost of card and nobles objects in the Model
 */
public class Cost implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;
    /**
     * Cost in white (diamond) gems
     */
    protected int white;
    /**
     * Cost in green (emerald) gems
     */
    protected int green;
    /**
     * Cost in blue (sapphire) gems
     */
    protected int blue;
    /**
     * Cost in black (onyx) gems
     */
    protected int black;
    /**
     * Cost in red (ruby) gems
     */
    protected int red;

    /**
     * Lower cost by discount up to 0 gems
     * @param discount - discount to apply
     * @return Cost object with discount
     */
    public Cost lessBy(Cost discount) {
        return new Cost(
                Math.max(white - discount.white, 0),
                Math.max(green - discount.green, 0),
                Math.max(blue - discount.black, 0),
                Math.max(black - discount.black, 0),
                Math.max(red - discount.red, 0));
    }

    /**
     * Enumerator corresponding to gem colors
     */
    public enum GemColor {
        White,
        Green,
        Blue,
        Black,
        Red
    }


    /**
     * All parameter constructor
     *
     * @param white - cost of white gems
     * @param green - cost of green gems
     * @param blue  - cost of blue gems
     * @param black - cost of black gems
     * @param red   - cost of red gems
     * @throws IllegalArgumentException - thrown then cost is negative
     */
    public Cost(int white, int green, int blue, int black, int red) throws IllegalArgumentException {
        if (white < 0 || green < 0 || blue < 0 || black < 0 || red < 0) {
            throw new IllegalArgumentException("Cost argument cannot be less than 0");
        }
        this.white = white;
        this.green = green;
        this.blue = blue;
        this.black = black;
        this.red = red;
    }

    /**
     * Basic, empty constructor.
     * Make cost object with all fields set to 0
     */
    Cost() {
        this.white = 0;
        this.green = 0;
        this.blue = 0;
        this.black = 0;
        this.red = 0;
    }

    /**
     * Check if there is enough cash to subtract cost from
     * without using yellow gems
     *
     * @param cost - cost to subtract from
     * @return true if there is, false otherwise
     */
    boolean enough(@NotNull Cost cost) {
        return white >= cost.white && green >= cost.green && blue >= cost.blue && black >= cost.black;
    }

    /**
     * Convert GemColor to Cost object
     *
     * @param discountColor - color of gem giving discount
     * @return Cost object corresponding to discount
     */
    @NotNull
    static Cost fromGemColor(@NotNull GemColor discountColor) {
        Cost cost = null;
        switch (discountColor) {
            case White:
                cost = new Cost(1, 0, 0, 0, 0);
                break;
            case Green:
                cost = new Cost(0, 1, 0, 0, 0);
                break;
            case Blue:
                cost = new Cost(0, 0, 1, 0, 0);
                break;
            case Black:
                cost = new Cost(0, 0, 0, 1, 0);
                break;
            case Red:
                cost = new Cost(0, 0, 0, 0, 1);
                break;
        }
        return cost;
    }

    /**
     * Add another cost to this cost
     *
     * @param other - other cost to add
     */
    Cost add(@NotNull Cost other) {
        white += other.white;
        green += other.green;
        blue += other.blue;
        black += other.black;
        red += other.red;
        return this;
    }

    
    /**
     * Sum white, green, blue, black and red gems
     *
     * @return int - sum of gems
     */
    int sum() {
        return white + green + blue + black + red;
    }

    /**
     * Get number of white gems
     *
     * @return white gems
     */
    public int getWhite() {
        return white;
    }

    /**
     * Get number of green gems
     *
     * @return green gems
     */
    public int getGreen() {
        return green;
    }

    /**
     * Get number of blue gems
     *
     * @return blue gems
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Get number of black gems
     *
     * @return black gems
     */
    public int getBlack() {
        return black;
    }

    /**
     * Get number of red gems
     *
     * @return red gems
     */
    public int getRed() {
        return red;
    }

    /**
     * Check if cost is equal to another.
     * Cost are equal then corresponding gems are equals
     *
     * @param o - object to test equality to
     * @return true if are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cost cost = (Cost) o;
        return getWhite() == cost.getWhite() &&
                getGreen() == cost.getGreen() &&
                getBlue() == cost.getBlue() &&
                getBlack() == cost.getBlack() &&
                getRed() == cost.getRed();
    }

    /**
     * Check if any field contains specified amount
     * (only on needed)
     * @param amount - integer to check
     * @return true if contains, false otherwise
     */
    public boolean any(int amount) {
        return white == amount || green == amount || blue == amount || black == amount || red == amount;
    }

    /**
     * HashCode
     *
     * @return hashcode of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getWhite(), getGreen(), getBlue(), getBlack(), getRed());
    }

    /**
     * String representation of the object
     *
     * @return string representation of the object
     */
    @Override
    public String toString() {
        return "Model.Cash{" +
                "white=" + white +
                ", green=" + green +
                ", blue=" + blue +
                ", black=" + black +
                ", red=" + red +
                '}';
    }
}
