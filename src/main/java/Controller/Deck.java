package Controller;

import Model.DevelopmentCard;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for operations on deck of DevelopmentCard(s),
 * shuffling, converting to stack etc.
 */
public class Deck {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Array of developmentCards
     */
    private final DevelopmentCard[] developmentCards;

    /**
     * Constructor from array of developmentCards
     *
     * @param developmentCards - array of developmentCards
     */
    public Deck(DevelopmentCard[] developmentCards) {
        this.developmentCards = developmentCards;
    }

    /**
     * Shuffle deck of developmentCards
     */
    public void shuffle() {
        Collections.shuffle(Arrays.asList(developmentCards), ThreadLocalRandom.current());
    }

    /**
     * Return stack of developmentCards from the deck
     * Cards are added from first index to last,
     * so the first element on stack would be the last
     * element in deck
     *
     * @return stack of developmentCards from the deck
     */
    public Stack<DevelopmentCard> toStack() {
        Stack<DevelopmentCard> developmentCardStack = new Stack<>();
        for (DevelopmentCard developmentCard : developmentCards) {
            developmentCardStack.push(developmentCard);
        }
        return developmentCardStack;
    }
}
