package at.jku.cp.rau.game.endconditions;

import java.util.List;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Cloud;
import at.jku.cp.rau.game.objects.Unicorn;

final public class CloudEvaporater implements EndCondition {
    private int winner;

    public CloudEvaporater() {
        winner = -1;
    }

    @Override
    public boolean hasEnded(IBoard board, List<Cloud> evaporated, List<Unicorn> sailing) {
        if (sailing.size() > 0) {
            winner = -1;
            return true;
        }

        if (evaporated.size() > 0) {
            winner = 0;
            return true;
        }

        return false;
    }

    @Override
    public int getWinner() {
        return winner;
    }

    @Override
    public EndCondition copy() {
        return (EndCondition) this;
    }

    @Override
    public String getOutcome() {
        return "CLOUDEVAPORATER";
    }
}
