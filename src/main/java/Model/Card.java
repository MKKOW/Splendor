package Model;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Abstract base class for cards available in the board
 */
public abstract class Card {

    /**
     * Card id
     */
    protected final int id;

    /**
     * Cost in gems
     */
    protected final Cost cost;

    /**
     * Additional points of prestige it brings to player who owns that card
     */
    protected final int prestige;

    /**
     * Path to source image of that card
     */
    protected final Path imagePath;

    /**
     * All parameter constructor
     *
     * @param id        - id of the card
     * @param cost      - cost of the card
     * @param prestige  - prestige of the card
     * @param imagePath - path to image of the card
     */
    public Card(int id, Cost cost, int prestige, Path imagePath) {
        this.id = id;
        this.cost = cost;
        this.prestige = prestige;
        this.imagePath = imagePath;
    }

    /**
     * Get id of a card
     *
     * @return id of a card
     */
    public int getId() {
        return id;
    }

    /**
     * Get cost in gems of a card
     *
     * @return cost of a card
     */
    public Cost getCost() {
        return cost;
    }

    /**
     * Get prestige it gives to player
     *
     * @return prestige points of a card
     */
    public int getPrestige() {
        return prestige;
    }

    /**
     * Get path to image of a card
     *
     * @return path to image of a card
     */
    public Path getImagePath() {
        return imagePath;
    }

    /**
     * Check if another card is equals to card
     * cards are equals then and only when they
     * have the same id
     *
     * @param o - object to check
     * @return true is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id;
    }

    /**
     * HashCode - card id
     *
     * @return int representing Card object - it's id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
