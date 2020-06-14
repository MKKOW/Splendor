package Model;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Rules implements Serializable {

    /**
     * Number of each level development cards in the board
     */
    public static final int numberOfDevelopmentCardsByLevel = 4;
    /**
     * Maximal id for Level 1 development card
     */
    public static final int maxDevelopmentCardLevel1Id = 40;
    /**
     * Max cash that player is allowed to have
     */
    public static final int maxPlayerCash = 10;
    /**
     * Maximal id for Level 2 development card
     */
    public static final int maxDevelopmentCardLevel2Id = 70;
    /**
     * Amount of prestige needed for player to win
     */
    public static final int prestigeWinCondition = 15;
    /**
     * Maximal id for Level 3 development card
     */
    public static final int maxDevelopmentCardLevel3Id = 90;
    /**
     * Path to resource with cards information
     */
    public static final Path cardsFilePath = Paths.get("cards.json");
    /**
     * Path to resource with noble information
     */
    public static final Path nobleFilePath = Paths.get("nobles.json");
    /**
     * Total number of cards in the card file
     */
    public static final int numberOfDevelopmentCardsInFile = 90;
    /**
     * Total number of nobles in the noble file
     */
    public static final int numberOfNoblesInFile = 10;
    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;
    /**
     * Maximal id for noble card
     */
    public static int maxNobleId = 10;
    /**
     * Number of starting nobles on board
     */
    public final int startingNumberOfNoblesOnBoard;
    /**
     * Number of players int the game
     */
    public final int numberOfPlayers;

    /**
     * Constructor from numberOfPlayers
     * Create board rules for specific number of players
     * @param numberOfPlayers - integer number of players from 2 to 4 including both ends
     * @throws IllegalArgumentException - if number of players is incorrect
     */
    public Rules(int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 4) {
            throw new IllegalArgumentException("Number of players cannot be " + numberOfPlayers);
        }
        this.numberOfPlayers = numberOfPlayers;
        this.startingNumberOfNoblesOnBoard = numberOfPlayers + 1;
    }
}
