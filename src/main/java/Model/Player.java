package Model;

import Exceptions.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Representation of the Player in Model
 */
public class Player implements Serializable {


    /**
     * Player's nick
     */
    private final String nick;

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;
    /**
     * Hashmap of cards owned by player
     */
    private final HashMap<Integer, DevelopmentCard> developmentCards;
    /**
     * Hashmap of nobles owned by player
     */
    private final HashMap<Integer, Noble> nobles;
    /**
     * Player's cash
     */
    private final Cash cash;
    /**
     * Indicator of activity
     */
    private boolean active;
    /**
     * Currently holden development card
     */
    private DevelopmentCard claimedCard;

    /**
     * All parameter constructor
     *
     * @param active           - indicator if player is currently active
     * @param nick             - nick of the player
     * @param developmentCards - developments cards owned
     * @param nobles           - nobles owned
     * @param cash             - player's cash
     * @param claimedCard      -
     */
    public Player(boolean active, String nick, HashMap<Integer, DevelopmentCard> developmentCards, HashMap<Integer, Noble> nobles, Cash cash, DevelopmentCard claimedCard) {
        this.active = active;
        this.nick = nick;
        this.developmentCards = developmentCards;
        this.nobles = nobles;
        this.cash = cash;
        this.claimedCard = claimedCard;
    }

    /**
     * Game start constructor
     *
     * @param active       - indicator if player is currently active
     * @param nick         - nick of the player
     * @param startingCash - player's starting cash
     */
    public Player(boolean active, String nick, Cash startingCash) {
        this.active = active;
        this.nick = nick;
        this.developmentCards = new HashMap<>();
        this.nobles = new HashMap<>();
        this.cash = startingCash;
        this.claimedCard = null;
    }

    /**
     * Add cash to player
     *
     * @param cashToAdd - amount of cash to add
     * @throws TooMuchCashException - if sum of player's cash if grater than he is allowed to have
     */
    public void addCash(Cash cashToAdd) throws TooMuchCashException, IllegalCashAmountException {
        cash.add(cashToAdd);
        if (cash.sum() > Rules.MaxPlayerCash) throw new TooMuchCashException("Player " + nick + "has to much cash");
    }

    /**
     * Subtract cost from player's cash without using yellow gems
     *
     * @param costToSub - cost to subtract
     * @throws NotEnoughCashException - if player doesn't have enough cash to subtract from
     */
    public void subCost(Cost costToSub) throws NotEnoughCashException {
        cash.sub(costToSub);
    }

    /**
     * Subtract cost from player's cash allowing to use maxYellow yellow gems
     *
     * @param costToSub - cost to subtract
     * @param maxYellow - max number of yellow gems to use
     * @throws NotEnoughCashException   - if player doesn't have enough cash to subtract from (including yellow gems)
     * @throws IllegalArgumentException - if maxYellow is negative or more than player has yellow gems
     */
    public void subCost(Cost costToSub, int maxYellow) throws NotEnoughCashException, IllegalArgumentException {
        cash.sub(costToSub, maxYellow);
    }

    /**
     * Claim development card
     *
     * @param cardToClaim - development card to claim
     * @throws TooManyClaimsException - if player is already claiming some card
     */
    public void claimDevelopmentCard(DevelopmentCard cardToClaim) throws TooManyClaimsException {
        if (claimedCard != null)
            throw new TooManyClaimsException("Player" + nick + " already holds a development card " + claimedCard);
        claimedCard = cardToClaim;
    }

    /**
     * Remove card from claimed cards
     *
     * @throws NothingClaimedException - if nothing is claimed
     */
    public void removeClaimedDevelopmentCard() throws NothingClaimedException {
        if (claimedCard == null)
            throw new NothingClaimedException("Player" + nick + " is currently not holding any development card.");
        claimedCard = null;
    }

    /**
     * Add development card to player's cards
     *
     * @param cardToAdd - development card to add
     */
    public void addDevelopmentCard(DevelopmentCard cardToAdd) {
        developmentCards.put(cardToAdd.getId(), cardToAdd);
    }

    /**
     * Add claimed development card to player's cards
     */
    public void addClaimedDevelopmentCard() {
        developmentCards.put(claimedCard.getId(), claimedCard);
        claimedCard = null;
    }

    /**
     * Add noble to player's cards
     *
     * @param nobleToAdd - noble card to add
     * @throws NotEnoughDiscountException - if player do not have enough discount to get that noble
     */
    public void addNoble(Noble nobleToAdd) throws NotEnoughDiscountException {
        if (!(getTotalDiscount().enough(nobleToAdd.getCost())))
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
     * Set player as active
     */
    public void setActive() {
        active = true;
    }

    /**
     * Unset player as active
     */
    public void setNotActive() {
        active = false;
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
