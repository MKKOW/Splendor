package Model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representation of Noble card in the Model
 */
public class Noble extends Card implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;


    /**
     * All field constructor
     *
     * @param id       - noble id
     * @param cost     - noble cost in gems
     * @param prestige - noble prestige
     */
    public Noble(int id, Cost cost, int prestige) {
        super(id, cost, prestige);
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
                '}';
    }
}
