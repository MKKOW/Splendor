package Model;

import Controller.BoardMaker;
import Exceptions.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.Server;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ServerBoard extends ClientBoard implements Serializable{

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;
    /**
     * Instance of game board.
     * At any time there should be only one game board for client
     */
    private static ServerBoard instance = null;
    /**
     * Rules for that game
     */
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
    public ServerBoard() {
        super();
        developmentCardPileLevel1 = new Stack<>();
        developmentCardPileLevel2 = new Stack<>();
        developmentCardPileLevel3 = new Stack<>();
    }

    /**
     * Get instance of ServerBoard
     * Use this to ensure there is only one ServerBoard
     *
     * @return instance of ServerBoard
     */
    public static ServerBoard getInstance() {
        if (instance == null) {
            instance = new ServerBoard();
        }
        return instance;
    }

    /**
     * Check if some player won and return his nick.
     * Player wins when he have 15 or more point and all players
     * moved the same number of times.
     *
     * If there are more than one players who meet that condition,
     * the winner is player with the most prestige points.
     *
     * This check should be done BEFORE changing active player turn,
     * otherwise it will not work properly
     *
     * @return string - nick of winner or null if there is no winner yet
     *
     * TODO: What happends when two players have the same score and are over 15 points?
     */
    @Nullable
    public String checkWin() {
        // Check if all players did same amount of moves
        for (Player player : players.values()) {
            if (player.getNumberOfMoves() != activePlayer.getNumberOfMoves() - 1) {
                return null;
            }
        }

        String winner = null;
        int biggestPrestige = 0;
        // Check which player is above threshold and has biggest score
        for (Player player : players.values()) {
            if (player.getTotalPrestige() > biggestPrestige && player.getTotalPrestige() >= Rules.prestigeWinCondition) {
                winner = player.getNick();
            }
        }
        return winner;
    }

    /**
     * Refill development cards on board if needed,
     * if all cards on the board already this does nothing
     */
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
     * Active player buys the development card from the board
     *
     * @param cardId - id of the card
     * @throws CardNotOnBoardException  - if card is not on board
     * @throws NotEnoughCashException   - if player do not have enough cash to buy card
     * @throws IllegalArgumentException - if id is wrong
     */
    public void buyCard(int cardId) throws CardNotOnBoardException, NotEnoughCashException, IllegalArgumentException {
        if (cardId < 1 || cardId > Rules.maxDevelopmentCardLevel3Id)
            throw new IllegalArgumentException("Development card of id " + cardId + " does not exist");

        DevelopmentCard developmentCard = developmentCardsOnBoard.getCardById(cardId);
        int usedYellow = activePlayer.subCost(developmentCard.getCost());
        activePlayer.addDevelopmentCard(developmentCard);
        bankCash.add(developmentCard.getCost().lessBy(activePlayer.getTotalDiscount()));
        bankCash.addYellow(usedYellow);

        // Clean up
        developmentCardsOnBoard.removeCardById(cardId);
        refillDevelopmentCards();
    }

    /**
     * Active player buys the development card from the hand.
     * This function adds development card from active players hand to his
     * owned cards leaving his hand null.
     *
     * @throws NotEnoughCashException - if player do not have enough cash
     * @throws NothingClaimedException - if player fo not claims any card
     */
    public void buyClaimedCard() throws NotEnoughCashException, NothingClaimedException {
        DevelopmentCard claimedCard = activePlayer.getClaimedCard();
        if (claimedCard == null) throw new NothingClaimedException("Player " + activePlayer.getNick() + " is not holding any card");

        int usedYellow = activePlayer.subCost(claimedCard.getCost());
        bankCash.add(activePlayer.getClaimedCard().getCost().lessBy(activePlayer.getTotalDiscount()));
        activePlayer.addClaimedDevelopmentCard();
        bankCash.addYellow(usedYellow);
        //Clean up
        activePlayer.removeClaim();
    }

    /**
     * Active player receives cash from bank.
     * This function subtracts amount of cash from the bank and give it to player
     *
     * Important!
     * If there is enough cash in the bank
     *
     * @param white  - number of white coins
     * @param green  - number of green coins
     * @param blue   - number of blue coins
     * @param black  - number of black coins
     * @param red    - number of red coins
     * @throws NotEnoughCashException     - if there isn't enough cash in the bank
     * @throws IllegalCashAmountException - if cash amount the player gets is against the rules
     */
    public void giveCash(int white, int green, int blue, int black, int red) throws NotEnoughCashException, IllegalCashAmountException {
        Cash cash = new Cash(white, green, blue, black, red, 0);
        bankCash.subCash(cash);
        activePlayer.addCash(cash);
    }

    public void returnCash(int white, int green, int blue, int black, int red) throws NotEnoughCashException {
        Cash cash = new Cash(white, green, blue, black, red, 0);
        bankCash.add(cash);
        activePlayer.subCash(cash);
    }
    /**
     * Active player gets the noble
     *
     * @param nobleId - id of the noble
     * @throws CardNotOnBoardException    - if noble card isn't on board
     * @throws NotEnoughDiscountException - if player don't have enough discount to get that noble
     */
    public void giveNoble(int nobleId) throws CardNotOnBoardException, NotEnoughDiscountException {
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
     */
    public void claimCard(int cardId) throws IllegalArgumentException, CardNotOnBoardException, TooManyClaimsException {
        if (cardId < 0 || cardId > Rules.maxDevelopmentCardLevel3Id)
            throw new IllegalArgumentException("Development card of id " + cardId + " does not exist");

        DevelopmentCard cardToClaim = developmentCardsOnBoard.getCardById(cardId);
        activePlayer.claimDevelopmentCard(cardToClaim);
        bankCash.giveYellow(activePlayer);
        // Clean up
        developmentCardsOnBoard.removeCardById(cardId);
        refillDevelopmentCards();
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
     * Try to set active player if it is possible.
     * It's not possible if active player has too much cash
     * or one more nobles can visit him. If so the appropriate
     * exception will be thrown
     *
     * @param nick - nick of player
     * @throws TooMuchCashException - if active player has too much cash
     * @throws NobleNotSelectedException - if active player can be visited by noble on the board
     */
    public void setActivePlayer(String nick) throws TooMuchCashException, NobleNotSelectedException {
        Player newActivePlayer = players.get(nick);
        if (newActivePlayer == null)
            throw new PlayerNotExistException("Player " + nick + " do not exists!");

        // If there is not any active player just set it
        if (activePlayer == null) {
            changeActivePlayer(newActivePlayer);
            return;
        }

        // --Player cannot be null at this point-- //
        if (activePlayer.isOverCashLimit()) {
            throw new TooMuchCashException(activePlayer.getNick() + " has too much cash (" + activePlayer.getCash() + ")");
        }
        if (nobles.canVisitPlayer(activePlayer)) {
            throw new NobleNotSelectedException(activePlayer.getNick() + " can be visited by one or more nobles.");
        }
        changeActivePlayer(newActivePlayer);
    }

    /**
     * Try to change active player if it is possible.
     * It's not possible if active player has too much cash
     * or one more nobles can visit him. If so the appropriate
     * exception will be thrown
     */
    private void changeActivePlayer(@NotNull Player player)  {
        if (activePlayer != null) {
            activePlayer.setActive(false);
            activePlayer.incrementNumberOfMoves();
        }

        // change active player, set his flag to true and increment his moves
        activePlayer = player;
        activePlayer.setActive(true);
    }

    /**
     * Add player to game
     *
     * @param nick - players nick
     * @throws TooManyPlayersException - if there is max number of players already
     * @throws AmbiguousNickException - if there is player with that nick
     */
    public void addPlayer(String nick) throws TooManyPlayersException, AmbiguousNickException {
        if (players.size() == rules.numberOfPlayers) throw new TooManyPlayersException("There are already " + rules.numberOfPlayers + " players.");
        if (players.get(nick) != null) throw new AmbiguousNickException("Player " + nick + " already exists");
        players.put(nick, new Player(false, nick));
    }


    /**
     * Save state of the board to given path
     *
     * @param path - path to file to save board to
     * @throws IOException - if there is some problem with the file
     */
    public void saveBoard(Path path) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(path.toString());
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
    }

    /**
     * Restore state of the board from serialized object
     *
     * @param path - path to file
     * @throws IOException - if file doesn't exist or insufficient permissions
     * @throws ClassNotFoundException - dont know
     */
    public static ServerBoard restoreBoard(Path path) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(path.toString());
        ObjectInputStream in = new ObjectInputStream(fileIn);
        ServerBoard.setInstance((ServerBoard) in.readObject());
        in.close();
        fileIn.close();
        return instance;
    }

    /**
     * Return array of IDs of nobles that can visit player
     *
     * @return array of nobles (may be empty)
     */
    public int[] getAvailableNoblesIDs() {
        List<Noble> availableNobles = this.nobles.getAvailableNobles(activePlayer);
        if (availableNobles.isEmpty()) return new int[] {};

        int[] availableNoblesIDs = new int[availableNobles.size()];
        int i = 0;
        for (Noble noble : availableNobles) {
            availableNoblesIDs[i++] = noble.getId();
        }
        return availableNoblesIDs;
    }

    /**
     * Set or override players on board
     *
     * @param cardsOnBoard - cards on board to set
     */
    public void setDevelopmentCardsOnBoard(CardsOnBoard cardsOnBoard) {
        this.developmentCardsOnBoard = cardsOnBoard;
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

    /**
     * Set or override players on board
     *
     * @param players - new players on the board
     */
    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    /**
     * Set server board instance to another
     *
     * @param instance - new instance of ServerBoard
     */
    public static void setInstance(ServerBoard instance) {
        ServerBoard.instance = instance;
    }

    /**
     * String representing game board
     *
     * @return string representing gameboard
     */
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
