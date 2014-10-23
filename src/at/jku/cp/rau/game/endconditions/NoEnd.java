package at.jku.cp.rau.game.endconditions;

import java.util.List;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Cloud;
import at.jku.cp.rau.game.objects.Unicorn;

final public class NoEnd implements EndCondition {
    @Override
    public boolean hasEnded(IBoard board, List<Cloud> evaporated, List<Unicorn> sailing) {
        return false;
    }

    @Override
    public int getWinner() {
        return -1;
    }

    @Override
    public EndCondition copy() {
        return (EndCondition) this;
    }
}
