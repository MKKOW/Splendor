package Controller;

import Exceptions.InactivePlayersException;
import Model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

/**
 * Controller class for making game
 * board for specified number of players
 */
public class BoardMaker {

    private BoardMaker() {
    }

    /**
     *
     * @param numberOfPlayers
     * @return
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public static ServerBoard generateRandomServerBoard(int numberOfPlayers) throws IllegalArgumentException, IOException {
        if (numberOfPlayers < 2 || numberOfPlayers > 4) {
            throw new IllegalArgumentException("Number of players cannot be " + numberOfPlayers);
        }

        Rules gameRules = new Rules(numberOfPlayers);
        DeckMaker deckMaker = DeckMaker.getInstance();
        ServerBoard serverBoard = ServerBoard.getInstance();

        serverBoard.setRules(gameRules);
        serverBoard.setBankCash(startingBankCash(numberOfPlayers));

        Stack<DevelopmentCard>[] cardStacks = deckMaker.makeDevelopmentCardsStacks();
        serverBoard.setDevelopmentCardPileLevel1(cardStacks[0]);
        serverBoard.setDevelopmentCardPileLevel2(cardStacks[1]);
        serverBoard.setDevelopmentCardPileLevel3(cardStacks[2]);
        serverBoard.setDevelopmentCardsOnBoard(new CardsOnBoard());
        serverBoard.refillDevelopmentCards();

        Noble[] nobles = deckMaker.makeNobles(gameRules.startingNumberOfNoblesOnBoard);
        serverBoard.setNobles(new NoblesOnBoard(nobles));
        return serverBoard;
    }

    public static ClientBoard generatePresentationBoard() throws InactivePlayersException {
        ServerBoard serverBoard = ServerBoard.getInstance();

        serverBoard.setPlayers(generatePresentationPlayers());
        serverBoard.setBankCash(generatePresentationBankCash());
        serverBoard.setDevelopmentCardsOnBoard(generateDevelopmentCardsOnBoard());
        serverBoard.setNobles(generatePresentationNoblesOnBoard());
        ClientBoard.setInstanceFromServerBoard(serverBoard);
        return ClientBoard.getInstance();
    }

    private static NoblesOnBoard generatePresentationNoblesOnBoard() {
        Noble noble1 = new Noble(1, new Cost(3, 1, 5, 0, 0), 2);
        Noble noble2 = new Noble(2, new Cost(3, 1, 0, 0, 5), 6);
        Noble noble3 = new Noble(3, new Cost(0, 1, 5, 3, 0), 4);
        return new NoblesOnBoard(new Noble[]{noble1, noble2, noble3});
    }

    private static CardsOnBoard generateDevelopmentCardsOnBoard() {
        CardsOnBoard cardsOnBoard = new CardsOnBoard();
        DevelopmentCard card1 = new DevelopmentCard(1, new Cost(1, 1, 0, 0, 0), 2,  DevelopmentCard.Level.One, Cost.GemColor.Black);
        DevelopmentCard card2 = new DevelopmentCard(5, new Cost(1, 1, 0, 0, 0), 2,  DevelopmentCard.Level.One, Cost.GemColor.White);
        DevelopmentCard card3 = new DevelopmentCard(50, new Cost(1, 1, 0, 0, 0), 2,  DevelopmentCard.Level.Two, Cost.GemColor.Green);
        DevelopmentCard card4 = new DevelopmentCard(55, new Cost(1, 1, 0, 0, 0), 2,  DevelopmentCard.Level.Two, Cost.GemColor.Black);
        DevelopmentCard card5 = new DevelopmentCard(80, new Cost(1, 1, 0, 0, 0), 2,  DevelopmentCard.Level.Three, Cost.GemColor.Blue);
        DevelopmentCard card6 = new DevelopmentCard(84, new Cost(1, 1, 0, 0, 0), 2,  DevelopmentCard.Level.Three, Cost.GemColor.Black);
        cardsOnBoard.setLevel1(new DevelopmentCard[]{card1, card2});
        cardsOnBoard.setLevel2(new DevelopmentCard[]{card3, card4});
        cardsOnBoard.setLevel3(new DevelopmentCard[]{card5, card6});
        return cardsOnBoard;
    }

    private static BankCash generatePresentationBankCash() {
        return new BankCash(5, 8, 5, 8, 5, 3);
    }

    /**
     * Create starting cash in bank from number of players
     * @return BankCash object
     */
    public static BankCash startingBankCash(int numberOfPlayers) {
        if (numberOfPlayers == 2) {
            return new BankCash(4, 4, 4, 4, 4, 6);
        }
        if (numberOfPlayers == 3) {
            return new BankCash(5, 5, 5, 5, 5, 6);
        }
        else {
            return new BankCash(6, 6, 6, 6, 6, 6);
        }
    }

    private static HashMap<String, Player> generatePresentationPlayers() {
        Player alice = new Player(true, "Alice");
        Player bob = new Player(false, "Bob");
        HashMap<String, Player> hashMap = new HashMap<>();
        hashMap.put("Alice", alice);
        hashMap.put("Bob", bob);
        return hashMap;
    }
}
