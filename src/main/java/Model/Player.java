package Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Player implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;
    /**
     * Indicator of active player
     */
    boolean active;
    /**
     * Player's nick
     */
    private String nick;
    /**
     * Hashmap of cards owned by player
     */
    private HashMap<Integer, Card> cards;
    /**
     * Hashmap of nobles owned by player
     */
    private HashMap<Integer, Noble> nobles;
    /**
     * Player's cash
     */
    private Cash cash;

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

}
