package Model;

import Exceptions.CardNotOnBoardException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Representation of cards on the board
 */
public class CardsOnBoard implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * First level development cards on board
     */
    DevelopmentCard[] level1;

    /**
     * Second level development cards on board
     */
    DevelopmentCard[] level2;

    /**
     * Third level development cards on board
     */
    DevelopmentCard[] level3;

    /**
     * All params constructor
     *
     * @param level1 - first level development cards
     * @param level2 - second level development cards
     * @param level3 - third level development cards
     */
    public CardsOnBoard(DevelopmentCard[] level1, DevelopmentCard[] level2, DevelopmentCard[] level3) {
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
    }


    /**
     * Basic constructor for empty board.
     * All fields all null.
     */
    public CardsOnBoard() {
        this.level1 = new DevelopmentCard[Rules.numberOfDevelopmentCardsByLevel];
        this.level2 = new DevelopmentCard[Rules.numberOfDevelopmentCardsByLevel];
        this.level3 = new DevelopmentCard[Rules.numberOfDevelopmentCardsByLevel];
    }

    /**
     * Get development card by its id
     * and remove it from the board
     *
     * @param cardId - id of a card
     * @return DevelopmentCard on board
     * @throws CardNotOnBoardException - if card not found on board
     */
    @NotNull
    public DevelopmentCard getCardById(int cardId) throws CardNotOnBoardException {
        DevelopmentCard developmentCard = null;
        if (cardId <= Rules.maxDevelopmentCardLevel1Id) {
            for (DevelopmentCard card : level1) {
                if (card != null && card.getId() == cardId) {
                    return card;
                }
            }
        } else if (cardId <= Rules.maxDevelopmentCardLevel2Id) {
            for (DevelopmentCard card : level2) {
                if (card != null && card.getId() == cardId) {
                    return card;
                }
            }
        } else {
            for (DevelopmentCard card : level3) {
                if (card != null && card.getId() == cardId) {
                    return card;
                }
            }
        }
        throw new CardNotOnBoardException("Card of id " + cardId + " is not on board.");
    }


    /**
     * Remove reference to development card by that id from the board
     *
     * @param cardId - id of a card
     * @throws CardNotOnBoardException - if card not found on board
     */
    public void removeCardById(int cardId) throws CardNotOnBoardException {
        if (cardId <= Rules.maxDevelopmentCardLevel1Id) {
            for (int i = 0; i < level1.length; i++) {
                if (level1[i] != null && level1[i].getId() == cardId) {
                    level1[i] = null;
                    return;
                }
            }
        } else if (cardId <= Rules.maxDevelopmentCardLevel2Id) {
            for (int i = 0; i < level2.length; i++) {
                if (level2[i] != null && level2[i].getId() == cardId) {
                    level2[i] = null;
                    return;
                }
            }
        } else {
            for (int i = 0; i < level3.length; i++) {
                if (level3[i] != null && level3[i].getId() == cardId) {
                    level3[i] = null;
                    return;
                }
            }
        }
        throw new CardNotOnBoardException("Card of id " + cardId + " is not on board.");
    }

    /**
     * Get level 1 development cards
     *
     * @return level 1 development cards
     */
    public DevelopmentCard[] getLevel1() {
        return level1;
    }

    /**
     * Set level 1 development cards
     */
    public void setLevel1(DevelopmentCard[] level1) {
        this.level1 = level1;
    }

    /**
     * Get level 2 development cards
     *
     * @return level 2 development cards
     */
    public DevelopmentCard[] getLevel2() {
        return level2;
    }

    /**
     * Set level 2 development cards
     */
    public void setLevel2(DevelopmentCard[] level2) {
        this.level2 = level2;
    }

    /**
     * Get level 3 development cards
     *
     * @return level 3 development cards
     */
    public DevelopmentCard[] getLevel3() {
        return level3;
    }

    /**
     * Set level 3 development cards
     */
    public void setLevel3(DevelopmentCard[] level3) {
        this.level3 = level3;
    }


    /**
     * Check if object if equal to that board of cards.
     * Objects are equal when corresponding arrays have
     * equal cards (checked by id) on equal indexes
     *
     * @param o - object to check
     * @return true if equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardsOnBoard that = (CardsOnBoard) o;
        return Arrays.equals(getLevel1(), that.getLevel1()) &&
                Arrays.equals(getLevel2(), that.getLevel2()) &&
                Arrays.equals(getLevel3(), that.getLevel3());
    }

    /**
     * HashCode of cards on board
     *
     * @return hashcode of cards on board
     */
    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getLevel1());
        result = 31 * result + Arrays.hashCode(getLevel2());
        result = 31 * result + Arrays.hashCode(getLevel3());
        return result;
    }

    /**
     * String representation of cards on boards
     *
     * @return string representing cards
     */
    @Override
    public String toString() {
        return "CardsOnBoard{" +
                "level1=" + Arrays.toString(level1) +
                ", level2=" + Arrays.toString(level2) +
                ", level3=" + Arrays.toString(level3) +
                '}';
    }
}
