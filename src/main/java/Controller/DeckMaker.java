package Controller;

import Model.DevelopmentCard;
import Model.Noble;
import Model.Rules;

import java.io.IOException;
import java.util.*;

/**
 * Class for operations on deck of DevelopmentCard(s),
 * shuffling, converting to stack etc.
 * Part of making game board
 */
public class DeckMaker {


    private static DeckMaker instance = null;

    private static void shuffleArray(DevelopmentCard[] array) {
        List<DevelopmentCard> list = new ArrayList<>(Arrays.asList(array));
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
    }

    private static void shuffleArray(Noble[] array) {
        List<Noble> list = new ArrayList<>(Arrays.asList(array));
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
    }

    public static DeckMaker getInstance() {
        if (instance == null) {
            instance = new DeckMaker();
        }
        return instance;
    }

    /**
     * Quick demo, load cards from cards.json, shuffle them and split into 3 stack by level
     */
    public static void main(String[] args) {
        DeckMaker maker = DeckMaker.getInstance();
        try {
            Stack<DevelopmentCard>[] cards = maker.makeDevelopmentCardsStacks();
            System.out.println("LEVEL 1 \n" + cards[0]);
            System.out.println("LEVEL 2 \n" + cards[1]);
            System.out.println("LEVEL 3 \n" + cards[2]);
        } catch (IOException e) {
            System.out.println("Missing cards file in " + Rules.nobleFilePath);
        }
    }

    public Stack<DevelopmentCard>[] makeDevelopmentCardsStacks() throws IOException {
        JSONLoader loader = JSONLoader.getInstance();
        DevelopmentCard[][] cards = splitCards(loader.loadCards());
        shuffleArray(cards[0]);
        shuffleArray(cards[1]);
        shuffleArray(cards[2]);
        return toStack(cards);
    }

    private Stack<DevelopmentCard>[] toStack(DevelopmentCard[][] cards) {
        Stack<DevelopmentCard>[] stacks = new Stack[3];
        for (int i = 0; i < 3; i++) {
            stacks[i] = new Stack<>();
            stacks[i].addAll(Arrays.asList(cards[i]));
        }
        return stacks;
    }

    private DevelopmentCard[][] splitCards(DevelopmentCard[] cards) {
        DevelopmentCard[][] sCards = new DevelopmentCard[3][];
        sCards[0] = new DevelopmentCard[Rules.maxDevelopmentCardLevel1Id];
        sCards[1] = new DevelopmentCard[Rules.maxDevelopmentCardLevel2Id - Rules.maxDevelopmentCardLevel1Id];
        sCards[2] = new DevelopmentCard[Rules.maxDevelopmentCardLevel3Id - Rules.maxDevelopmentCardLevel2Id];

        for (int i = 0; i < Rules.maxDevelopmentCardLevel3Id; i++) {
            if (i < Rules.maxDevelopmentCardLevel1Id) {
                sCards[0][i] = cards[i];
            } else if (i < Rules.maxDevelopmentCardLevel2Id) {
                sCards[1][i - Rules.maxDevelopmentCardLevel1Id] = cards[i];
            } else {
                sCards[2][i - Rules.maxDevelopmentCardLevel2Id] = cards[i];
            }
        }
        return sCards;
    }

    public Noble[] makeNobles(int startingNumberOfNoblesOnBoard) throws IOException {
        JSONLoader loader = JSONLoader.getInstance();
        Noble[] nobles = loader.loadNobles();
        shuffleArray(nobles);
        return Arrays.copyOfRange(nobles, 0, startingNumberOfNoblesOnBoard);
    }
}
