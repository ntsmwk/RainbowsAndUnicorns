package at.jku.cp.rau.game.endconditions;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Cloud;
import at.jku.cp.rau.game.objects.Unicorn;

final public class LastUnicornStanding implements EndCondition, Serializable {
    private static final long serialVersionUID = 1L;

    private int winner;

    public LastUnicornStanding() {
        winner = -1;
    }

    public LastUnicornStanding(LastUnicornStanding lus) {
        this.winner = lus.winner;
    }

    @Override
    public boolean hasEnded(IBoard board, List<Cloud> evaporated, List<Unicorn> sailing) {
        // has a unicorn gone sailing?
        if (sailing.size() > 0) {
            Set<Integer> uids = new HashSet<>();
            for (Unicorn u : board.getUnicorns()) {
                uids.add(u.id);
            }

            Set<Integer> sids = new HashSet<>();
            for (Unicorn u : sailing) {
                sids.add(u.id);
            }

            uids.removeAll(sids);
            if (uids.size() == 1) {
                winner = (int) uids.toArray()[0];
            }
            return true;
        }
        return false;
    }

    @Override
    public int getWinner() {
        return winner;
    }

    @Override
    public String getOutcome() {
        return "KOWIN";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + winner;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LastUnicornStanding other = (LastUnicornStanding) obj;
        if (winner != other.winner)
            return false;
        return true;
    }

    @Override
    public EndCondition copy() {
        return (EndCondition) new LastUnicornStanding(this);
    }
}
