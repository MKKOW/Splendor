package Model;

import Controller.BoardMaker;
import Exceptions.*;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Representation of game board in the Model,
 * this version is for client it lacks information
 * that client should not have.
 */
public class ClientBoard implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Instance of game board
     */
    private static ClientBoard instance = null;

    /**
     * Bank - Place from players to get their cash and pay for cards
     */
    private BankCash bankCash;

    /**
     * Array of nobles on the board
     */
    private NoblesOnBoard nobles;

    /**
     * Hashmap of players by their nick
     */
    private HashMap<String, Player> players;

    /**
     * Currently active player
     */
    private Player activePlayer;

    /**
     * Development cards currently on boards
     */
    private CardsOnBoard developmentCardsOnBoard;


    /**
     * Make null game board
     * All fields should be set before
     * start of the game by setters
     */
    public ClientBoard() {
        bankCash = null;
        nobles = null;
        players = null;
        activePlayer = null;
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
     * Sets another game board as instance
     *
     * @param instance game board to set
     */
    public static void setInstance(ClientBoard instance) {
        ClientBoard.instance = instance;
    }

    /**
     * Check if some player won and return his nick
     * Player win when he have . If there are many players
     * who meet that condition, the winner is player with the
     * most prestige points. If there is more than one, return
     * player with the most points.
     *
     * @return string - nick of winner or null if there is no winner yet
     */
    @Nullable
    public String checkWin() {
        String winner = null;
        int biggestPrestige = 0;
        for (Player player : players.values()) {
            if (player.getTotalPrestige() > biggestPrestige && player.getTotalPrestige() >= Rules.prestigeWinCondition) {
                winner = player.getNick();
            }
        }
        return winner;
    }


    /**
     * Active player buys the development card from the board
     *
     * @param cardId - id of the card
     * @throws CardNotOnBoardException  - if card is not on board
     * @throws NotEnoughCashException   - if player do not have enough cash to buy card
     * @throws IllegalArgumentException - if id is wrong
     */
    void buyCard(int cardId) throws CardNotOnBoardException, NotEnoughCashException, IllegalArgumentException {
        if (cardId < 0 || cardId > Rules.maxDevelopmentCardLevel3Id)
            throw new IllegalArgumentException("Development card of id " + cardId + " does not exist");

        DevelopmentCard developmentCard = developmentCardsOnBoard.getCardById(cardId);
        activePlayer.subCost(developmentCard.getCost());
        activePlayer.addDevelopmentCard(developmentCard);
    }


    /**
     * Active player buys the development card from the hand
     *
     * @throws NotEnoughCashException - if player do not have enough cash
     */
    void buyClaimedCard() throws NotEnoughCashException {
        activePlayer.subCost(activePlayer.getClaimedCard().getCost());
        activePlayer.addClaimedDevelopmentCard();
    }


    public static void main(String[] args) throws InactivePlayersException, NotEnoughCashException, CardNotOnBoardException {
        ClientBoard.setInstance(BoardMaker.generatePresentationBoard());
        ClientBoard clientBoard = ClientBoard.getInstance();

        System.out.println("SHOW STARTING BOARD");
        System.out.println(clientBoard + "\n");

        System.out.println("ADD CARD TO ALICE");
        clientBoard.buyCard(1);
        System.out.println(clientBoard + "\n");


        System.out.println("TRY TO BUY CARD NOT ON THE TABLE");
        try {
            clientBoard.buyCard(43);
        } catch (CardNotOnBoardException e) {
            System.out.println(e.toString() + "\n");
        }
        System.out.println(clientBoard + "\n");


        System.out.println("SWITCH ACTIVE PLAYER TO BOB");
        clientBoard.setActivePlayer("Bob");
        System.out.println(clientBoard + "\n");
    }

    /**
     * Active player receives cash from bank
     *
     * @param white  - number of white coins
     * @param green  - number of green coins
     * @param blue   - number of blue coins
     * @param black  - number of black coins
     * @param red    - number of red coins
     * @param yellow - number of yellow coins
     * @throws TooMuchCashException       - if after that player has too much cash
     * @throws NotEnoughCashException     - if there isn't enough cash in the bank
     * @throws IllegalCashAmountException - if cash amount the player gets is against the rules
     */
    void giveCash(int white, int green, int blue, int black, int red, int yellow) throws TooMuchCashException, NotEnoughCashException, IllegalCashAmountException {
        Cash cash = new Cash(white, green, blue, black, red, yellow);
        bankCash.sub(cash);
        activePlayer.addCash(cash);
    }

    /**
     * Active player gets the noble
     *
     * @param nobleId - id of the noble
     * @throws CardNotOnBoardException    - if noble card isn't on board
     * @throws NotEnoughDiscountException - if player don't have enough discount to get that noble
     */
    void getNoble(int nobleId) throws CardNotOnBoardException, NotEnoughDiscountException {
        if (nobleId < 0 || nobleId > Rules.maxNobleId)
            throw new IllegalArgumentException("Noble card of id " + nobleId + " does not exist");

        Noble noble = nobles.getNobleById(nobleId);
        activePlayer.addNoble(noble);
    }

    /**
     * Active player claims development card on board
     * and if possible receives yellow token
     *
     * @param cardId - id of the card
     * @throws IllegalArgumentException - if id is wrong
     * @throws CardNotOnBoardException  - if development card isn't on board
     * @throws TooManyClaimsException   - if player already claims another card
     * @throws TooMuchCashException     - if after adding yellow coin, player has too mach cash
     */
    void claimCard(int cardId) throws IllegalArgumentException, CardNotOnBoardException, TooManyClaimsException, TooMuchCashException {
        if (cardId < 0 || cardId > Rules.maxDevelopmentCardLevel3Id)
            throw new IllegalArgumentException("Development card of id " + cardId + " does not exist");

        DevelopmentCard cardToClaim = developmentCardsOnBoard.getCardById(cardId);
        activePlayer.claimDevelopmentCard(cardToClaim);
        bankCash.giveYellow(activePlayer);
    }

    /**
     * Set or override cash in bank
     *
     * @param bankCash - cash in bank to set
     */
    public void setBankCash(BankCash bankCash) {
        this.bankCash = bankCash;
    }

    /**
     * Set or override nobles on board
     *
     * @param nobles - nobles to set
     */
    public void setNobles(NoblesOnBoard nobles) {
        this.nobles = nobles;
    }

    /**
     * Set player as active
     * All operations are done on active player
     *
     * @param nick - nick of player
     */
    public void setActivePlayer(String nick) {
        activePlayer.setNotActive();
        activePlayer = players.get(nick);
        activePlayer.setActive();
    }

    /**
     * Set or override players on board
     *
     * @param cardsOnBoard - cards on board to set
     */
    public void setDevelopmentCardsOnBoard(CardsOnBoard cardsOnBoard) {
        this.developmentCardsOnBoard = cardsOnBoard;
    }

    /**
     * Set or override players on board
     *
     * @param players - new players on the board
     */
    public void setPlayers(HashMap<String, Player> players) throws InactivePlayersException {
        this.players = players;
        for (Player player : players.values()) {
            if (player.isActive()) activePlayer = player;
        }
        if (activePlayer == null) throw new InactivePlayersException("All players in game are inactive");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientBoard that = (ClientBoard) o;
        return Objects.equals(bankCash, that.bankCash) &&
                Objects.equals(nobles, that.nobles) &&
                Objects.equals(players, that.players) &&
                Objects.equals(activePlayer, that.activePlayer) &&
                Objects.equals(developmentCardsOnBoard, that.developmentCardsOnBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankCash, nobles, players, activePlayer, developmentCardsOnBoard);
    }

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
