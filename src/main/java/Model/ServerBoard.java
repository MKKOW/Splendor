package Model;

import java.util.Stack;

public class ServerBoard extends ClientBoard {

    /**
     * Stack of level 1 cards
     */
    private Stack<DevelopmentCard> developmentCardPileLevel1;

    /**
     * Stack of level 2 cards
     */
    private Stack<DevelopmentCard> developmentCardPileLevel2;

    /**
     * Stack of level 3 cards
     */
    private Stack<DevelopmentCard> developmentCardPileLevel3;

    private ServerBoard() {
        super();
    }

    void refillDevelopmentCards() {

    }

}
