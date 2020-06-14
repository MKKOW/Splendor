package Model;

import Exceptions.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Representation of the Player in Model
 */
public class Player implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;
    /**
     * Player's nick
     */
    private String nick;
    /**
     * Hashmap of cards owned by player
     */
    private HashMap<Integer, DevelopmentCard> developmentCards;
    /**
     * Hashmap of nobles owned by player
     */
    private HashMap<Integer, Noble> nobles;
    /**
     * Player's cash
     */
    private Cash cash;
    /**
     * Indicator of activity
     */
    private boolean active;
    /**
     * Currently holden development card
     */
    private DevelopmentCard claimedCard;
    /**
     * Number of correct moves
     */
    private int numberOfMoves;

    /**
     * All parameter constructor
     *
     * @param active           - indicator if player is currently active
     * @param nick             - nick of the player
     * @param developmentCards - developments cards owned
     * @param nobles           - nobles owned
     * @param cash             - player's cash
     * @param claimedCard      - card currently on hand
     */
    public Player(boolean active, String nick, HashMap<Integer, DevelopmentCard> developmentCards, HashMap<Integer, Noble> nobles, Cash cash, DevelopmentCard claimedCard, int numberOfMoves) {
        this.active = active;
        this.nick = nick;
        this.developmentCards = developmentCards;
        this.nobles = nobles;
        this.cash = cash;
        this.claimedCard = claimedCard;
        this.numberOfMoves = numberOfMoves;
    }

    /**
     * Game start constructor.
     * Construct player without cash, developmentCards, Noble cards
     * and without any claimed card
     *
     * @param active - indicator if player is currently active
     * @param nick   - nick of the player
     */
    public Player(boolean active, String nick) {
        this.active = active;
        this.nick = nick;
        this.developmentCards = new HashMap<>();
        this.nobles = new HashMap<>();
        this.cash = new Cash(0, 0, 0, 0, 0, 0);
        this.claimedCard = null;
        this.numberOfMoves = 0;
    }

    /**
     * Add cash to player
     * and then check if he has too much cash, if so throw exception
     *
     * @param cashToAdd - amount of cash to add
     */
    public void addCash(Cash cashToAdd) throws IllegalCashAmountException {
        try {
            cash.addCash(cashToAdd);
        } catch (IllegalCashAmountException e) {
            throw new IllegalCashAmountException(nick + " cannot get that cash amount (against the rules)");
        }
    }


    /**
     * Subtract cost from player's cash allowing usage of yellow cards
     * if player do not have enough gems of color
     *
     * @param costToSub - cost to subtract (development card cost)
     * @throws NotEnoughCashException - if player doesn't have enough cash to subtract from even using all his yellow gems
     */
    public int subCost(Cost costToSub) throws NotEnoughCashException {
        try {
            return cash.subCost(costToSub.lessBy(getTotalDiscount()));
        } catch (NotEnoughCashException e) {
            throw new NotEnoughCashException(nick + " has not enough cash", e);
        }
    }


    public void subCash(Cash cash) throws NotEnoughCashException {
        this.cash.subCash(cash);
    }


    /**
     * Claim development card
     *
     * @param cardToClaim - development card to claim
     * @throws TooManyClaimsException - if player is already claiming some card
     */
    public void claimDevelopmentCard(@NotNull DevelopmentCard cardToClaim) throws TooManyClaimsException {
        if (claimedCard != null)
            throw new TooManyClaimsException("Player" + nick + " already holds a development card " + claimedCard);
        claimedCard = cardToClaim;
    }

    /**
     * Add development card to player's cards
     *
     * @param cardToAdd - development card to add
     */
    public void addDevelopmentCard(@NotNull DevelopmentCard cardToAdd) {
        developmentCards.put(cardToAdd.getId(), cardToAdd);
    }

    /**
     * Add claimed development card to player's cards
     */
    public void addClaimedDevelopmentCard() throws NothingClaimedException {
        if (claimedCard == null)
            throw new NothingClaimedException(nick + " is not claiming any card!");
        developmentCards.put(claimedCard.getId(), claimedCard);
    }

    public void removeClaim() throws NothingClaimedException {
        if (claimedCard == null)
            throw new NothingClaimedException(nick + " is not claiming any card!");
        claimedCard = null;
    }

    /**
     * Add noble to player's cards
     *
     * @param nobleToAdd - noble card to add
     * @throws NotEnoughDiscountException - if player do not have enough discount to get that noble
     */
    public void addNoble(@NotNull Noble nobleToAdd) throws NotEnoughDiscountException {
        if (!nobleToAdd.canVisit(this))
            throw new NotEnoughDiscountException("Player " + nick + " has too little discount (" + getTotalDiscount() + ") to get noble " + nobleToAdd);
        nobles.put(nobleToAdd.getId(), nobleToAdd);
    }

    /**
     * Calculate and return total discount of player's development cards
     *
     * @return Cost object reflecting discount in cost
     */
    public Cost getTotalDiscount() {
        Cost totalDiscount = new Cost(0, 0, 0, 0, 0);
        for (DevelopmentCard developmentCard : developmentCards.values()) {
            totalDiscount.add(developmentCard.getDiscount());
        }
        return totalDiscount;
    }

    /**
     * Calculate and return total prestige that owned cards give to player
     *
     * @return integer sum of cards' prestige
     */
    public int getTotalPrestige() {
        int sum = 0;
        for (DevelopmentCard developmentCard : developmentCards.values()) {
            sum += developmentCard.getPrestige();
        }
        for (Noble noble : nobles.values()) {
            sum += noble.getPrestige();
        }
        return sum;
    }

    /**
     * Check if player is over allowed cash limit
     *
     * @return true if yes, false otherwise
     */
    public boolean isOverCashLimit() {
        return cash.sum() > Rules.maxPlayerCash;
    }

    /**
     * Set player as active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get indication of active player
     *
     * @return flag indicating active player
     */
    public boolean isActive() {
        return active;
    }


    /**
     * Increment (add 1) to number of moves
     */
    public void incrementNumberOfMoves() {
        this.numberOfMoves += 1;
    }

    /**
     * Get player's nick
     *
     * @return player's nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * Get HashMap of player's development cards
     *
     * @return hashmap of player's development cards
     */
    public HashMap<Integer, DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    /**
     * Get Hashmap of player's nobles
     *
     * @return
     */
    public HashMap<Integer, Noble> getNobles() {
        return nobles;
    }

    public Cash getCash() {
        return cash;
    }

    public DevelopmentCard getClaimedCard() {
        return claimedCard;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setDevelopmentCards(HashMap<Integer, DevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public void setNobles(HashMap<Integer, Noble> nobles) {
        this.nobles = nobles;
    }

    public void setCash(Cash cash) {
        this.cash = cash;
    }

    public void setClaimedCard(DevelopmentCard claimedCard) {
        this.claimedCard = claimedCard;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void setNumberOfMoves(int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    /**
     * Add yellow coin to players cash
     */
    void addYellow() {
        cash.yellow += 1;
    }

    /**
     * Check if Player is equal to another.
     * Players are equal when and only when they have the same nick.
     *
     * @param o - object to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return nick.equals(player.nick);
    }

    /**
     * HashCode
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(nick);
    }

    @Override
    public String toString() {
        return "Player{" +
                "nick='" + nick + '\'' +
                ", developmentCards=" + developmentCards +
                ", nobles=" + nobles +
                ", cash=" + cash +
                ", active=" + active +
                ", claimedCard=" + claimedCard +
                '}';
    }
}
