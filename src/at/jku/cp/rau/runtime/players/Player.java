package at.jku.cp.rau.runtime.players;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;

public interface Player {
    public Move getNextMove(PlayerInfo mappings, IBoard board);
}
