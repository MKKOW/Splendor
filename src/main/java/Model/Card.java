package Model;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Representation of single card in the Model
 */
public class Card implements Serializable, Cloneable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Card id
     */
    private final int id;

    /**
     * Cost in gems
     */
    private final Cost cost;

    /**
     * Discounts in gems it brings to player who owns that card
     */
    private final Cost discount;

    /**
     * Additional points of prestige it brings to player who owns that card
     */
    private final int prestige;

    /**
     * Level of a card (1, 2 or 3)
     */
    private final int level;

    /**
     * Path to source image of that card
     */
    private final Path imagePath;

    /**
     * All parameter constructor
     *
     * @param id        - id of a card
     * @param cost      - cost of a card
     * @param discount  - discount it brings to player
     * @param prestige  - prestige it brings to player
     * @param level     - level of a card
     * @param imagePath - path to image of that card
     */
    public Card(int id, Cost cost, Cost discount, int prestige, int level, Path imagePath) {
        this.id = id;
        this.cost = cost;
        this.discount = discount;
        this.prestige = prestige;
        this.level = level;
        this.imagePath = imagePath;
    }

    /**
     * All parameter constructor
     *
     * @param id        - id of a card
     * @param cost      - cost of a card
     * @param color     - color of gem
     * @param prestige  - prestige it brings to player
     * @param level     - level of a card
     * @param imagePath - path to image of that card
     */
    public Card(int id, Cost cost, Cost.GemColor color, int prestige, int level, Path imagePath) {
        this.id = id;
        this.cost = cost;
        this.discount = Cost.fromGemColor(color);
        this.prestige = prestige;
        this.level = level;
        this.imagePath = imagePath;
    }


    /**
     * Copy constructor
     *
     * @param other - other card object
     */
    public Card(Card other) {
        this.id = other.id;
        this.cost = other.cost;
        this.discount = other.discount;
        this.prestige = other.prestige;
        this.level = other.level;
        this.imagePath = other.imagePath;
    }


    /**
     * Get id
     *
     * @return id of a card
     */
    public int getId() {
        return id;
    }

    /**
     * Get cost
     *
     * @return cost of card
     */
    public Cost getCost() {
        return cost;
    }

    /**
     * Get discount
     *
     * @return discount of card
     */
    public Cost getDiscount() {
        return discount;
    }

    /**
     * Get prestige
     *
     * @return prestinge of card
     */
    public int getPrestige() {
        return prestige;
    }

    /**
     * Get level
     *
     * @return level of card
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get Path
     *
     * @return path to image of card
     */
    public Path getImagePath() {
        return imagePath;
    }

    /**
     * Check if card is equal to another.
     * Cards are equal when and only when they have the same id.
     *
     * @param o - object to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getId() == card.getId();
    }

    /**
     * Calculate hashCode of card.
     * HashCode is just its id.
     *
     * @return id of a card
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * Get string representation
     *
     * @return string representation of an Object
     */
    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cost=" + cost +
                ", discount=" + discount +
                ", prestige=" + prestige +
                ", level=" + level +
                ", imagePath=" + imagePath +
                '}';
    }

}
