package Controller;

import Exceptions.InactivePlayersException;
import Model.*;
import org.json.JSONObject;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Stack;

/**
 * Controller class for making game
 * board for specified number of players
 */
public class BoardMaker {

    public static ClientBoard generateRandomBoard() {
        return null;
    }

    public static ClientBoard generatePresentationBoard() throws InactivePlayersException {
        ClientBoard clientBoard = new ClientBoard();

        clientBoard.setPlayers(generatePresentationPlayers());
        clientBoard.setBankCash(generatePresentationBankCash());
        clientBoard.setDevelopmentCardsOnBoard(generateDevelopmentCardsOnBoard());
        clientBoard.setNobles(generatePresentationNoblesOnBoard());
        return clientBoard;
    }

    private static NoblesOnBoard generatePresentationNoblesOnBoard() {
        NoblesOnBoard noblesOnBoard = new NoblesOnBoard();
        Noble noble1 = new Noble(1, new Cost(3, 1, 5, 0, 0), 2, Paths.get(""));
        Noble noble2 = new Noble(2, new Cost(3, 1, 0, 0, 5), 6, Paths.get(""));
        Noble noble3 = new Noble(3, new Cost(0, 1, 5, 3, 0), 4, Paths.get(""));
        noblesOnBoard.setNobles(new Noble[]{noble1, noble2, noble3});
        return noblesOnBoard;
    }

    private static CardsOnBoard generateDevelopmentCardsOnBoard() {
        CardsOnBoard cardsOnBoard = new CardsOnBoard();
        DevelopmentCard card1 = new DevelopmentCard(1, new Cost(1, 1, 0, 0, 0), 2, Paths.get(""), DevelopmentCard.Level.One, Cost.GemColor.Black);
        DevelopmentCard card2 = new DevelopmentCard(5, new Cost(1, 1, 0, 0, 0), 2, Paths.get(""), DevelopmentCard.Level.One, Cost.GemColor.White);
        DevelopmentCard card3 = new DevelopmentCard(50, new Cost(1, 1, 0, 0, 0), 2, Paths.get(""), DevelopmentCard.Level.Two, Cost.GemColor.Green);
        DevelopmentCard card4 = new DevelopmentCard(55, new Cost(1, 1, 0, 0, 0), 2, Paths.get(""), DevelopmentCard.Level.Two, Cost.GemColor.Black);
        DevelopmentCard card5 = new DevelopmentCard(80, new Cost(1, 1, 0, 0, 0), 2, Paths.get(""), DevelopmentCard.Level.Three, Cost.GemColor.Blue);
        DevelopmentCard card6 = new DevelopmentCard(84, new Cost(1, 1, 0, 0, 0), 2, Paths.get(""), DevelopmentCard.Level.Three, Cost.GemColor.Black);
        cardsOnBoard.setLevel1(new DevelopmentCard[]{card1, card2});
        cardsOnBoard.setLevel2(new DevelopmentCard[]{card3, card4});
        cardsOnBoard.setLevel3(new DevelopmentCard[]{card5, card6});
        return cardsOnBoard;
    }

    private static BankCash generatePresentationBankCash() {
        return new BankCash(5, 8, 5, 8, 5, 3);
    }

    private static HashMap<String, Player> generatePresentationPlayers() {
        Player alice = new Player(true, "Alice", new Cash(2, 1, 1, 3, 2, 0));
        Player bob = new Player(false, "Bob", new Cash(1, 2, 2, 1, 2, 1));
        HashMap<String, Player> hashMap = new HashMap<>();
        hashMap.put("Alice", alice);
        hashMap.put("Bob", bob);
        return hashMap;
    }

    public static ClientBoard generateBoardFromJSON(JSONObject jsonObject) {
        return null;
    }

    public static Stack<DevelopmentCard> generateDevelopmentCardsStack(DevelopmentCard.Level level) {
        return null;
    }
}
