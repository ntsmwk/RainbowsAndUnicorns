package at.jku.cp.rau.runtime.players.random;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.runtime.players.Player;
import at.jku.cp.rau.runtime.players.PlayerInfo;

/**
 * this player has a list of all moves, except the SPAWN move, and chooses a
 * random one from this list
 * 
 * it does a random walk, so to say ...
 * 
 * @author rz
 * 
 */
public class RandomWalkPlayer implements Player {
    private Move[] moves = new Move[] { Move.STAY, Move.UP, Move.DOWN, Move.LEFT, Move.RIGHT };

    @Override
    public Move getNextMove(PlayerInfo mappings, IBoard board) {
        // choose a random move, don't spawn seeds, don't care whether we want
        // to execute a
        // move that wouldn't work, given the board state
        return moves[mappings.random.nextInt(moves.length)];
    }

}
