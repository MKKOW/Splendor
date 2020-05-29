package Model;

import Exceptions.IllegalCashAmountException;
import Exceptions.NotEnoughCashException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Representation of player or bank cash in the Model.
 *
 * This class extends Cost adding to it additional yellow
 * (gold) coins that player can use as any other cash token,
 * and bank can serve to players claiming cards.
 */
public class Cash extends Cost {

    /**
     * Number of yellow coins
     */
    protected int yellow;

    /**
     * All parameter constructor
     *
     * @param white  - number of white gems
     * @param green  - number of green gems
     * @param blue   - number of blue gems
     * @param black  - number of black gems
     * @param red    - number of red gems
     * @param yellow - number of yellow gems
     * @throws IllegalArgumentException - thrown when one or more arguments is negative
     */
    public Cash(int white, int green, int blue, int black, int red, int yellow) throws IllegalArgumentException {
        super(white, green, blue, black, red);
        if (yellow < 0) throw new IllegalArgumentException("Yellow tokens cannot be negative");
        this.yellow = yellow;
    }

    /**
     * check if given cash amount is allowed to add to players
     *
     * @param cost - cost to add
     */
    private static void checkAllowedCashAmount(Cost cost) throws IllegalCashAmountException {
        // TODO: do this
    }

    /**
     * Check if cash is allowed to be added, then add that cash
     *
     * @param cash - cash to add
     * @return this for further chaining
     */
    public Cash add(Cash cash) throws IllegalCashAmountException {
        checkAllowedCashAmount(cash);
        super.add(cash);
        yellow += cash.yellow;
        return this;
    }

    /**
     * Subtract cost from this cash without using yellow gems
     *
     * @param cost - cost to subtract
     * @throws NotEnoughCashException - thrown when there is not enough cash to subtract from
     */
    public void sub(Cost cost) throws NotEnoughCashException {
        if (!enough(cost)) {
            throw new NotEnoughCashException(this + " isn't enough to sub from (excluding yellow)" + cost);
        }
        white -= cost.white;
        green -= cost.green;
        blue -= cost.blue;
        black -= cost.black;
        red -= cost.red;
    }

    /**
     * Subtract cost from this cash allowing using yellow gems
     *
     * @param cost      - cost to subtract from
     * @param maxYellow - max number of yellow coins to use
     * @throws IllegalArgumentException - thrown when number of maxYellow to use is bigger than owned yellow coins
     * @throws NotEnoughCashException   - thrown when there is not enough cash to subtract from
     */
    public Cash sub(Cost cost, int maxYellow) throws IllegalArgumentException, NotEnoughCashException {
        if (!enough(cost, maxYellow)) {
            throw new NotEnoughCashException(this + " isn't enough to sub from (including yellow)" + cost);
        }
        white -= cost.white;
        if (white < 0) {
            maxYellow += white;
            yellow += white;
            white = 0;
        }
        green -= cost.green;
        if (green < 0) {
            maxYellow += green;
            yellow += green;
            green = 0;
        }
        blue -= cost.blue;
        if (blue < 0) {
            maxYellow += blue;
            yellow += blue;
            blue = 0;
        }
        black -= cost.black;
        if (black < 0) {
            maxYellow += black;
            yellow += black;
            black = 0;
        }
        red -= cost.red;
        if (red < 0) {
            maxYellow += red;
            yellow += red;
            red = 0;
        }
        assert maxYellow >= 0 : "Error: Bug in Cash class. maxYellow should never be negative at this point";
        return this;
    }

    /**
     * Check if there is enough cash to subtract cost from
     * without using yellow gems
     *
     * @param cost - cost to subtract from
     * @return true if there is, false otherwise
     */
    public boolean enough(@NotNull Cost cost) {
        return white >= cost.white && green >= cost.green && blue >= cost.blue && black >= cost.black;
    }

    /**
     * Check if there is enough cash to subtract cost
     * with yellow gems
     *
     * @param cost      - cost to subtract from
     * @param maxYellow - maximal number of yellow gems to use
     * @return true if enough, false otherwise
     * @throws IllegalArgumentException - thrown when number of maxYellow to use is bigger than owned yellow coins
     */
    public boolean enough(Cost cost, int maxYellow) throws IllegalArgumentException {
        if (maxYellow < 0) {
            throw new IllegalArgumentException("maxYellow cannot be negative");
        }
        if (maxYellow > this.yellow) {
            throw new IllegalArgumentException("Not enough yellow tokens.");
        }
        if (cost.sum() > sum()) {
            return false;
        }
        return (sum() - cost.sum() - yellow + maxYellow) >= 0;
    }

    /**
     * Sum all the gems
     *
     * @return sum of gems in cash
     */
    @Override
    public int sum() {
        return super.sum() + yellow;
    }

    /**
     * Get yellow gems
     *
     * @return yellow gems
     */
    public int getYellow() {
        return yellow;
    }


    /**
     * String representation of an Object
     *
     * @return string representing object
     */
    @Override
    public String toString() {
        return "Cash{" +
                "white=" + white +
                ", green=" + green +
                ", blue=" + blue +
                ", black=" + black +
                ", red=" + red +
                ", yellow=" + yellow +
                '}';
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
        Cash cash = (Cash) o;
        return getWhite() == cash.getWhite() &&
                getGreen() == cash.getGreen() &&
                getBlue() == cash.getBlue() &&
                getBlack() == cash.getBlack() &&
                getRed() == cash.getRed() &&
                getYellow() == cash.getYellow();
    }

    /**
     * HashCode
     *
     * @return hashcode of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getYellow());
    }
}
