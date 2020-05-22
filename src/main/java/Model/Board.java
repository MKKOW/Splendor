package Model;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Stack;

/**
 * Representation of game board in the Model
 */
public class Board implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Starting number of noble cards in the board
     */
    private static final int startingNumberOfNobles = 4;

    /**
     * Starting number of each level cards in the board
     */
    private static final int startingNumberOfCardsByLevel = 6;

    /**
     * Place from players get their cash and pay for cards
     */
    private Cash bank;

    /**
     * Array of nobles on the board
     */
    private Noble[] nobles;

    /**
     * Stack of level 1 cards
     */
    private Stack<Card> cardPile1;

    /**
     * Stack of level 2 cards
     */
    private Stack<Card> cardPile2;

    /**
     * Stack of level 3 cards
     */
    private Stack<Card> cardPile3;

    /**
     * Hashmap of players by their nick
     */
    private HashMap<String, Player> players;

    /**
     * 3x6 Array of cards on the board
     */
    private Card[][] cardsOnBoard;

    @Nullable
    public Player checkWin() {
        return null;
    }

}
