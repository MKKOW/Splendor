package Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for operations on deck of Card(s),
 * shuffling, converting to stack etc.
 */
public class Deck implements Serializable {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    /**
     * Array of cards
     */
    private Card[] cards;

    /**
     * Constructor from array of cards
     *
     * @param cards - array of cards
     */
    public Deck(Card[] cards) {
        this.cards = cards;
    }

    /**
     * Shuffle deck of cards
     */
    public void shuffle() {
        Collections.shuffle(Arrays.asList(cards), ThreadLocalRandom.current());
    }

    /**
     * Return stack of cards from the deck
     * Cards are added from first index to last,
     * so the first element on stack would be the last
     * element in deck
     *
     * @return stack of cards from the deck
     */
    public Stack<Card> toStack() {
        Stack<Card> cardStack = new Stack<>();
        for (Card card : cards) {
            cardStack.push(card);
        }
        return cardStack;
    }
}
