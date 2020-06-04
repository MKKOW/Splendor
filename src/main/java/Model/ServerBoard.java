package Model;

import Controller.BoardMaker;
import Exceptions.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

public class ServerBoard extends ClientBoard {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Instance of game board.
     * At any time there should be only one game board for client
     */
    private static ServerBoard instance = null;

    private Rules rules;

    /**
     * Stack of level 1 cards
     */
    private Stack<DevelopmentCard> developmentCardPileLevel1;

    /**
     * Stack of level 2 cards
     */
    private Stack<DevelopmentCard> developmentCardPileLevel2;

    /**
     * Stack of level 3 cards
     */
    private Stack<DevelopmentCard> developmentCardPileLevel3;

    /**
     * Private constructor
     * To get instance use ServerBoard.getInstance()
     * For making server board check BoardMaker class
     */
    private ServerBoard() {
        super();
        developmentCardPileLevel1 = new Stack<>();
        developmentCardPileLevel2 = new Stack<>();
        developmentCardPileLevel3 = new Stack<>();
    }

    public static ServerBoard getInstance() {
        if (instance == null) {
            instance = new ServerBoard();
        }
        return instance;
    }

    public static void main(String[] args) throws AmbiguousNickException, IOException {
        // Make fresh random starting ServerBoard
        ServerBoard serverBoard = BoardMaker.generateRandomServerBoard(new String[]{"Paweł", "Wojtek", "Agnieszka", "Mikołaj"});

        // Set instance of Client Board from ServerBoard and get it
        ClientBoard.setInstanceFromServerBoard(serverBoard);
        ClientBoard clientBoard = ClientBoard.getInstance();

        // Show clientBoard and serverBoard
        System.out.println(serverBoard);
        System.out.println(clientBoard);

        // ClientBoard and ServerBoard are equal!
        System.out.println("clientBoard.equals(serverBoard) = " + clientBoard.equals(serverBoard));
        System.out.println("serverBoard.equals(clientBoard) = " + serverBoard.equals(clientBoard));
    }

    public void refillDevelopmentCards() {
        for (int i = 0; i < Rules.numberOfDevelopmentCardsByLevel; i++) {
            if (developmentCardsOnBoard.level1[i] == null) {
                developmentCardsOnBoard.level1[i] = developmentCardPileLevel1.pop();
            }
            if (developmentCardsOnBoard.level2[i] == null) {
                developmentCardsOnBoard.level2[i] = developmentCardPileLevel2.pop();
            }
            if (developmentCardsOnBoard.level3[i] == null) {
                developmentCardsOnBoard.level3[i] = developmentCardPileLevel3.pop();
            }
        }
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

    public Stack<DevelopmentCard> getDevelopmentCardPileLevel1() {
        return developmentCardPileLevel1;
    }

    public void setDevelopmentCardPileLevel1(Stack<DevelopmentCard> developmentCardPileLevel1) {
        this.developmentCardPileLevel1 = developmentCardPileLevel1;
    }

    public Stack<DevelopmentCard> getDevelopmentCardPileLevel2() {
        return developmentCardPileLevel2;
    }

    public void setDevelopmentCardPileLevel2(Stack<DevelopmentCard> developmentCardPileLevel2) {
        this.developmentCardPileLevel2 = developmentCardPileLevel2;
    }

    public Stack<DevelopmentCard> getDevelopmentCardPileLevel3() {
        return developmentCardPileLevel3;
    }

    public void setDevelopmentCardPileLevel3(Stack<DevelopmentCard> developmentCardPileLevel3) {
        this.developmentCardPileLevel3 = developmentCardPileLevel3;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "ServerBoard{" + "\n" +
                "developmentCardPileLevel1=" + developmentCardPileLevel1 + "\n" +
                ", developmentCardPileLevel2=" + developmentCardPileLevel2 + "\n" +
                ", developmentCardPileLevel3=" + developmentCardPileLevel3 + "\n" +
                ", bankCash=" + bankCash + "\n" +
                ", nobles=" + nobles + "\n" +
                ", players=" + players + "\n" +
                ", activePlayer=" + activePlayer + "\n" +
                ", developmentCardsOnBoard=" + developmentCardsOnBoard + "\n" +
                '}';
    }
}
