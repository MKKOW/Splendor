package Model;

import Exceptions.CardNotOnBoardException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class NoblesOnBoard {

    /**
     * Serial version for serialization purposes
     */
    protected static final long serialVersionUID = 789L;

    Noble[] nobles;


    public NoblesOnBoard(Noble[] nobles) {
        this.nobles = nobles;
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
