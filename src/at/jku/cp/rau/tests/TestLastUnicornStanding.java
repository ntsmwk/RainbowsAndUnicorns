package at.jku.cp.rau.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.endconditions.LastUnicornStanding;
import at.jku.cp.rau.game.objects.Move;

public class TestLastUnicornStanding {
    @Test
    public void player0wins() {

        IBoard board = Board.fromLevelFile("assets/small_two_players.lvl");
        board.setEndCondition(new LastUnicornStanding());

        Move[] sequence = { Move.RIGHT, Move.RIGHT, Move.SPAWN, Move.LEFT, Move.DOWN };

        for (Move m : sequence) {
            board.executeMove(m);
            if (board.isRunning())
                board.executeMove(Move.STAY);
        }

        assertEquals(0, board.getEndCondition().getWinner());
    }

    @Test
    public void nobodyWins() {
        IBoard board = Board.fromLevelFile("assets/small_two_players.lvl");
        board.setEndCondition(new LastUnicornStanding());

        Move[] sequence = { Move.RIGHT, Move.RIGHT, Move.SPAWN };
        for (Move m : sequence) {
            board.executeMove(m);
            if (board.isRunning())
                board.executeMove(Move.STAY);
        }

        assertEquals(-1, board.getEndCondition().getWinner());
    }
}
