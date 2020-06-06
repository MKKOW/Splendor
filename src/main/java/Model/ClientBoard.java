package Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Representation of game board in the Model,
 * this version is for client it has all information
 * available for client and nothing more.
 */
public class ClientBoard implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Instance of game board.
     * At any time there should be only one game board for client
     */
    private static ClientBoard instance = null;

    /**
     * Bank - Place from players to get their cash and pay for cards
     */
    protected BankCash bankCash;

    /**
     * Array of nobles on the board
     */
    protected NoblesOnBoard nobles;

    /**
     * Hashmap of players by their nick
     */
    protected HashMap<String, Player> players;


    /**
     * Currently active player
     */
    protected Player activePlayer;

    /**
     * Development cards currently on boards
     */
    protected CardsOnBoard developmentCardsOnBoard;

    /**
     * Make null game board
     * All fields should be set before
     * start of the game by setters
     * <p>
     * Do NOT USE this constructor!
     * Use ClientBoard.getInstance() to get instance
     * of the board and ensure there is only one at any time
     * This constructor is public because ClientBoard is base class.
     */
    public ClientBoard() {
        bankCash = null;
        nobles = null;
        players = null;
        activePlayer = null;
    }

    /**
     * Sets another game board as instance
     * Use it to set ClientBoard after server
     * approve all changes and player ends round
     *
     * @param instance game board to set
     */
    public static void setInstance(ClientBoard instance) {
        ClientBoard.instance = instance;
    }

    public static void setInstanceFromServerBoard(ServerBoard instance) {
        ClientBoard board = ClientBoard.getInstance();
        board.bankCash = instance.getBankCash();
        board.activePlayer = instance.getActivePlayer();
        board.developmentCardsOnBoard = instance.developmentCardsOnBoard;
        board.players = instance.getPlayers();
        board.nobles = instance.getNobles();
    }

    /**
     * Get bank
     *
     * @return bank object - contains cash available in bank
     */
    public BankCash getBankCash() {
        return bankCash;
    }

    /**
     * Get nobles
     *
     * @return nobles object - contains noble on board
     */
    public NoblesOnBoard getNobles() {
        return nobles;
    }
    public void setActivePlayer(String nick) {
        this.activePlayer = players.get(nick);
    }


    /**
     * Get players
     *
     * @return hashmap of Player objects (key is player's nick)
     */
    public HashMap<String, Player> getPlayers() {
        return players;
    }

    /**
     * Get instance of game board
     *
     * @return instance of ClientBoard
     */
    public static ClientBoard getInstance() {
        if (instance != null) return instance;
        instance = new ClientBoard();
        return instance;
    }

    /**
     * Get reference to currently active player
     *
     * @return Player object
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Get current cards on board
     *
     * @return cards on board
     */
    public CardsOnBoard getDevelopmentCardsOnBoard() {
        return developmentCardsOnBoard;
    }

    /**
     * Check if boards are equal.
     * Boards are equal when they have the same
     * cards on the same places, and the same
     * players that have the same cards on the same places.
     *
     * @param o - object to check
     * @return true if equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        ClientBoard that = (ClientBoard) o;
        return Objects.equals(bankCash, that.bankCash) &&
                Objects.equals(nobles, that.nobles) &&
                Objects.equals(players, that.players) &&
                Objects.equals(activePlayer, that.activePlayer) &&
                Objects.equals(developmentCardsOnBoard, that.developmentCardsOnBoard);
    }

    /**
     * Get hashCode
     *
     * @return hashCode of ClientBoard object
     */
    @Override
    public int hashCode() {
        return Objects.hash(bankCash, nobles, players, activePlayer, developmentCardsOnBoard);
    }

    /**
     * String representing object
     *
     * @return string of ClientBoard
     */
    @Override
    public String toString() {
        return "ClientBoard{" + "\n" +
                "  bankCash=" + bankCash + "\n" +
                ", nobles=" + nobles + "\n" +
                ", players=" + players + "\n" +
                ", activePlayer=" + activePlayer + "\n" +
                ", developmentCardsOnBoard=" + developmentCardsOnBoard +
                '}';
    }
}
