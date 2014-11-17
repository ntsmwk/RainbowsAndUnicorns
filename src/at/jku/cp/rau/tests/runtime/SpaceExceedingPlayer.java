package at.jku.cp.rau.tests.runtime;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.runtime.players.Player;
import at.jku.cp.rau.runtime.players.PlayerInfo;

public class SpaceExceedingPlayer implements Player {
    @Override
    public Move getNextMove(PlayerInfo mappings, IBoard board) {
        byte[] tooMuch = new byte[513 * 1024 * 1024];

        for (int i = 0; i < tooMuch.length; i++) {
            tooMuch[i] = (byte) (i % 256);
        }

        return Move.STAY;
    }

}
