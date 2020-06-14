package Model;

import Exceptions.IllegalCashAmountException;
import Exceptions.NotEnoughCashException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representation of player's cash in the Model.
 *
 * This class extends Cost adding to it additional yellow
 * (gold) coins that player can use as any other cash token
 */
public class Cash extends Cost implements Serializable {

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
     * Empty constructor
     */
    Cash() {
        super();
        this.yellow = 0;
    }

    /**
     * Check if given cash amount is allowed to add to players
     *
     * @param cost - cost to add
     */
    private static boolean checkGetAmount(Cost cost){
        if (cost.sum() == 0) return false; // why would you do that?
        // All fields must be less than 3
        if (cost.any(3)) return false;
        // Check if player do not get more than 3 coins
        if (cost.sum() > 3) return false;
        // if any coin in cost is one, player cannot get more than one coin of each
        else if (cost.any(1) && (cost.white > 1 || cost.green > 1 || cost.blue > 1 || cost.black > 1 || cost.red > 1)) {
            return false;
        }
        // else if at least one coin is 2, there cannot be any more coins
        else return !cost.any(2) || cost.sum() == 2;
        // if passed all tests
    }

    /**
     * Check if cash is allowed to be added, then add that cash
     *
     * @param cash - cash to add
     * @return this for further chaining
     */
    Cash addCash(Cash cash) throws IllegalCashAmountException {
        if (!checkGetAmount(cash)) {
            throw new IllegalCashAmountException("Player cannot get " + cash + " cash in one turn.");
        }
        super.add(cash);
        yellow += cash.yellow;
        return this;
    }


    /**
     * Add the cash even if player is not allowed to have that many cash
     *
     * @param cash - cash to add
     * @return this for further chaining
     */
    Cash addCashForce(Cash cash) {
        super.add(cash);
        yellow += cash.yellow;
        return this;
    }

    /**
     * Subtract cost from this cash allowing using yellow gems
     *
     * @param cost      - cost to subtract from
     * @throws NotEnoughCashException   - thrown when there is not enough cash to subtract from even using all yellow gems
     */
    public Cash subCost(Cost cost) throws IllegalArgumentException, NotEnoughCashException {
        if (!enough(cost)) {
            throw new NotEnoughCashException(this + " isn't enough to sub from " + cost);
        }
        Cash usedCoins = new Cash();
        if (white - cost.white < 0) {
            usedCoins.white += white;
            usedCoins.yellow += Math.abs(getWhite() - cost.getWhite());
            yellow -= Math.abs(getWhite() - cost.getWhite());
            white = 0;
        }
        else {
            // we have enough cash to cover in gems
            white -= cost.white;
            usedCoins.white += cost.white;
        }

        if (green - cost.green < 0) {
            usedCoins.green += green;
            usedCoins.yellow += Math.abs(green - cost.green);
            yellow -= Math.abs(green - cost.green);
            green = 0;
        }
        else {
            // we have enough cash to cover in gems
            green -= cost.green;
            usedCoins.green += cost.green;
        }

        if (blue - cost.blue < 0) {
            usedCoins.blue += blue;
            usedCoins.yellow += Math.abs(blue - cost.blue);
            yellow -= Math.abs(blue - cost.blue);
            blue = 0;
        }
        else {
            // we have enough cash to cover in gems
            blue -= cost.blue;
            usedCoins.blue += cost.blue;
        }

        if (black - cost.black < 0) {
            usedCoins.black += black;
            usedCoins.yellow += Math.abs(black - cost.black);
            yellow -= Math.abs(black - cost.black);
            black = 0;
        }
        else {
            // we have enough cash to cover in gems
            black -= cost.black;
            usedCoins.black += cost.black;
        }

        if (red - cost.red < 0) {
            usedCoins.red += red;
            usedCoins.yellow += Math.abs(red - cost.red);
            yellow -= Math.abs(red - cost.red);
            red = 0;
        }
        else {
            // we have enough cash to cover in gems
            red -= cost.red;
            usedCoins.red += cost.red;
        }
        return usedCoins;
    }

    /**
     * Check if there is enough cash to subtract cost from
     * without using yellow gems
     *
     * @param cost - cost to subtract from
     * @return true if there is, false otherwise
     */
    public boolean enough(@NotNull Cost cost) {
        int result = 0;
        if (white - cost.white < 0) {
            result += white - cost.white;
        }
        if (green - cost.green < 0) {
            result += green - cost.green;
        }
        if (blue  - cost.blue < 0) {
            result += blue - cost.blue;
        }
        if (black  - cost.black < 0) {
            result += black - cost.black;
        }
        if (red - cost.red < 0) {
            result += red - cost.red;
        }
        return Math.abs(result) <= yellow;
    }

    /**
     * Sum white, green, blue, black, red and yellow gems
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

    /**
     * Subtract cash.
     * In this mode all fields of cost must be equal or
     * less than this fields.
     * @param cash
     */
    public void subCash(Cash cash) throws NotEnoughCashException {
        if (!enough(cash) || this.yellow < cash.yellow) {
            throw new NotEnoughCashException("Not enough cash to subtract " + cash + " from " + this);
        }
        this.white -= cash.white;
        this.green -= cash.green;
        this.black -= cash.black;
        this.blue -= cash.blue;
        this.red -= cash.red;
        this.yellow -= cash.yellow;
    }
}
