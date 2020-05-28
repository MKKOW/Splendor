package Model;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Representation of Noble card in the Model
 */
public class Noble implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Noble id
     */
    private final int id;

    /**
     * Cost of noble to come to the player
     */
    private final Cost cost;

    /**
     * Prestige noble gives to player
     */
    private final int prestige;


    /**
     * Path to image of the noble
     */
    private final Path imagePath;

    /**
     * All field constructor
     *
     * @param id        - noble id
     * @param cost      - noble cost in gems
     * @param prestige  - noble prestige
     * @param imagePath - path to image
     */
    public Noble(int id, Cost cost, int prestige, Path imagePath) {
        this.id = id;
        this.cost = cost;
        this.prestige = prestige;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public Cost getCost() {
        return cost;
    }

    public int getPrestige() {
        return prestige;
    }

    public Path getImagePath() {
        return imagePath;
    }

    /**
     * Check if noble is equal to another.
     * Nobles are equal when and only when they have the same id.
     *
     * @param o - object to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Noble noble = (Noble) o;
        return getId() == noble.getId();
    }

    /**
     * hashCode
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * String representation of Noble
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Noble{" +
                "id=" + id +
                ", cost=" + cost +
                ", prestige=" + prestige +
                ", imagePath=" + imagePath +
                '}';
    }
}
