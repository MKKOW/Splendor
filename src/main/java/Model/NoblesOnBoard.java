package Model;

import Exceptions.CardNotOnBoardException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class NoblesOnBoard {

    Noble[] nobles;


    public NoblesOnBoard(Noble[] nobles) {
        this.nobles = nobles;
    }

    public NoblesOnBoard() {
        nobles = new Noble[Rules.maxNobleId];
    }

    @NotNull
    public Noble getNobleById(int nobleId) throws CardNotOnBoardException {
        for (Noble noble : nobles) {
            if (noble.getId() == nobleId) return noble;
        }
        throw new CardNotOnBoardException("Noble of id " + nobleId + " isn't on board");
    }

    public Noble[] getNobles() {
        return nobles;
    }

    public void setNobles(Noble[] nobles) {
        this.nobles = nobles;
    }

    @Override
    public String toString() {
        return "NoblesOnBoard{" +
                "nobles=" + Arrays.toString(nobles) +
                '}';
    }
}
