package Model;

import java.io.Serializable;

/**
 * Representation of development card in Model
 */
public class DevelopmentCard extends Card implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;
    /**
     * Level of a card
     */
    private final Level level;
    /**
     * Discount it brings to the player
     */
    private final Cost discount;

    /**
     * All parameters constructor (from enum level)
     *
     * @param id       - id of the card [1, 90]
     * @param cost     - cost of the card
     * @param prestige - prestige to player
     * @param level    - level of the card - 1, 2 or 3
     * @param discount - discount to player
     */
    public DevelopmentCard(int id, Cost cost, int prestige, Level level, Cost discount) {
        super(id, cost, prestige);
        this.level = level;
        this.discount = discount;
    }

    /**
     * All parameters constructor (from enum level)
     *
     * @param id            - id of the card [1, 90]
     * @param cost          - cost of the card
     * @param prestige      - prestige to player
     * @param level         - level of the card - 1, 2 or 3
     * @param discountColor - color of discount coin
     */
    public DevelopmentCard(int id, Cost cost, int prestige, Level level, Cost.GemColor discountColor) {
        super(id, cost, prestige);
        this.level = level;
        this.discount = Cost.fromGemColor(discountColor);
    }

    /**
     * Get level
     *
     * @return development card level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Get discount
     *
     * @return development card discount
     */
    public Cost getDiscount() {
        return discount;
    }

    /**
     * Text representation of the card
     *
     * @return string representing development card
     */
    @Override
    public String toString() {
        return "DevelopmentCard{" +
                "id=" + id +
                ", cost=" + cost +
                ", prestige=" + prestige +
                ", level=" + level +
                ", discount=" + discount +
                '}';
    }

    /**
     * Enumerator holding all possible levels of a card
     */
    public enum Level {
        One,
        Two,
        Three;

        /**
         * Convert int to Level
         *
         * @param levelInt - level as integer
         * @return level as enumerator
         * @throws IllegalArgumentException - when levelInt is not 1, 2 or 3
         */
        public static Level fromInt(int levelInt) throws IllegalArgumentException {
            switch (levelInt) {
                case 1:
                    return Level.One;
                case 2:
                    return Level.Two;
                case 3:
                    return Level.Three;
                default:
                    throw new IllegalArgumentException("DevelopmentCard cannot be level " + levelInt);
            }
        }
    }
}
