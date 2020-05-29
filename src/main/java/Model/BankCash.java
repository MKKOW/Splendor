package Model;

import Exceptions.IllegalCashAmountException;
import Exceptions.TooMuchCashException;
import org.jetbrains.annotations.NotNull;

public class BankCash extends Cost {

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
     * If it's possible give yellow coin to player
     *
     * @param player - player to give yellow coin to
     * @throws TooMuchCashException - if player has too much cash
     */
    public void giveYellow(@NotNull Player player) throws TooMuchCashException {
        if (getYellow() > 0) {
            try {
                player.addCash(new Cash(0, 0, 0, 0, 0, 1));
            } catch (IllegalCashAmountException ignored) {
            } // never thrown
            this.yellow -= 1;
        }
    }

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

    // TODO: do this, only placeholder
    public void sub(Cash cash) {
        white -= cash.white;
        green -= cash.green;
        blue -= cash.blue;
        black -= cash.black;
        red -= cash.red;
        yellow -= cash.yellow;
    }
}
