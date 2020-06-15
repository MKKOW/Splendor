package Model;

import Exceptions.CardNotOnBoardException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Representation of nobles currently present on the board in Model
 */
public class NoblesOnBoard implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Array of nobles currently in the board
     */
    Noble[] nobles;

    /**
     * Constructor from array of nobles
     * @param nobles - array of Noble object
     */
    public NoblesOnBoard(Noble[] nobles) {
        this.nobles = nobles;
    }

    /**
     * Get noble by its id
     * If noble is not on board throw an exception
     *
     * @param nobleId - id of noble
     * @return Noble object
     * @throws CardNotOnBoardException - if noble with given id is not on board
     */
    @NotNull
    public Noble getNobleById(int nobleId) throws CardNotOnBoardException {
        for (Noble noble : nobles) {
            if (noble.getId() == nobleId) return noble;
        }
        throw new CardNotOnBoardException("Noble of id " + nobleId + " isn't on board");
    }

    /**
     * Get array of nobles on the board
     * @return array of Noble objects
     */
    public Noble[] getNobles() {
        return nobles;
    }

    /**
     * Set nobles on the board
     * @param nobles - array of Noble objects
     */
    public void setNobles(Noble[] nobles) {
        this.nobles = nobles;
    }

    /**
     * String representation of noble
     * @return string representing NobleOnBoard class
     */
    @Override
    public String toString() {
        return "NoblesOnBoard{" +
                "nobles=" + Arrays.toString(nobles) +
                '}';
    }

    /**
     * Check if any noble on the board can visit player
     *
     * @param player - player to check
     *
     * @return true if can, false otherwise
     */
    public boolean canVisitPlayer(Player player) {
        for (Noble noble : nobles) {
            if (noble.canVisit(player)) return true;
        }
        return false;
    }

    public List<Noble> getAvailableNobles(Player player) {
        LinkedList<Noble> availableNobles = new LinkedList<>();
        for (Noble noble : nobles) {
            if (noble.canVisit(player)) {
                availableNobles.add(noble);
            }
        }
        return availableNobles;
    }

    public void removeNobleById(int nobleId){
        for (int i=0;i<nobles.length;i++) {
            if (nobles[i].getId() == nobleId) {
                nobles[i] = null;
                for(int j=i+1;j<nobles.length;j++){
                    nobles[j-1]=nobles[j];
                }
                Noble[] tmp = new Noble[nobles.length-1];
                for (int j=0;j<tmp.length;j++){
                    tmp[j] = nobles[j];
                }
                nobles = tmp;
                return;
            }
        }
    }
}
