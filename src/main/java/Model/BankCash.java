package Model;

import Exceptions.NotEnoughCashException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Representation of cash in a bank in a Model
 */
public class BankCash extends Cost implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Number of yellow (gold) coins
     */
    private int yellow;

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
    public BankCash(int white, int green, int blue, int black, int red, int yellow) throws IllegalArgumentException {
        super(white, green, blue, black, red);
        this.yellow = yellow;
    }

    /**
     * Empty constructor
     */
    BankCash() {
        super();
        this.yellow = 0;
    }

    /**
     * If it's possible give yellow coin to player
     *
     * @param player - player to give yellow coin to
     *
     * @return true if successfully added, false otherwise
     */
    boolean giveYellow(@NotNull Player player) {
        if (getYellow() > 0) {
            player.addYellow();
            this.yellow -= 1;
            return true;
        }
        return false;
    }

    /**
     * If possible subtract some number of coins from the bank.
     * If it is not possible throw exception
     * @param cash - cash to subtrect from bank
     * @throws NotEnoughCashException - when bank has too little cash
     */
    void subCash(Cash cash) throws NotEnoughCashException {
        if (!enough(cash)) throw new NotEnoughCashException("Not enough cash in bank");
        this.white -= cash.white;
        this.green -= cash.green;
        this.blue -= cash.blue;
        this.black -= cash.black;
        this.red -= cash.red;
        this.yellow -= cash.yellow;
    }

    /**
     * Check if bank has enough cash to subtract from
     * @param cash - cash to subtract
     * @return true if yes, false if no
     */
    boolean enough(Cash cash) {
        return white >= cash.white && green >= cash.green && blue >= cash.blue && black >= cash.black && yellow >= cash.yellow;
    }

    /**
     * Get yellow coins
     *
     * @return integer number  of yellow coins
     */
    public int getYellow() {
        return yellow;
    }

    /**
     * String representation of cash in bank
     *
     * @return string representation of the object
     */
    @Override
    public String toString() {
        return "BankCash{" +
                "white=" + white +
                ", green=" + green +
                ", blue=" + blue +
                ", black=" + black +
                ", red=" + red +
                ", yellow=" + yellow +
                '}';
    }
}
