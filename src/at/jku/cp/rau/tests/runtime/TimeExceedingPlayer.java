package at.jku.cp.rau.tests.runtime;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.runtime.players.Player;
import at.jku.cp.rau.runtime.players.PlayerInfo;

public class TimeExceedingPlayer implements Player {
    @Override
    public Move getNextMove(PlayerInfo mappings, IBoard board) {
        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Move.STAY;
    }

}
