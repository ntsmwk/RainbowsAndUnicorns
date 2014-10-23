package at.jku.cp.rau.tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.BoardWithHistory;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.endconditions.PointCollecting;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Move;

public class TestPointCollecting {
    @Test
    public void doesNotCrashIfNoMarkers() {
        IBoard board = Board.fromLevelRepresentation(Arrays.asList("####", "#p.#", "####"));

        PointCollecting fc = new PointCollecting();
        board.setEndCondition(fc);

        board.executeMove(Move.RIGHT);
        board.executeMove(Move.LEFT);

        assertEquals(0, fc.getScore(0));
    }

    @Test
    public void oneUnicornOneFlag() {
        IBoard board = Board.fromLevelRepresentation(Arrays.asList("####", "#pm#", "####"));

        PointCollecting fc = new PointCollecting();
        board.setEndCondition(fc);

        assertEquals(-1, board.getMarkers().get(0).lastVisitedBy);

        board.executeMove(Move.RIGHT);

        assertEquals(0, board.getMarkers().get(0).lastVisitedBy);

        assertEquals(1, fc.getScore(0));
    }

    @Test
    public void twoUnicornsOneFlag() {
        IBoard board = Board.fromLevelRepresentation(Arrays.asList("#####", "#pmp#", "#####"));
        PointCollecting fc = new PointCollecting();
        board.setEndCondition(fc);

        assertEquals(-1, board.getMarkers().get(0).lastVisitedBy);

        // player 0
        board.executeMove(Move.RIGHT);
        assertEquals(0, board.getMarkers().get(0).lastVisitedBy);

        // player 1
        board.executeMove(Move.STAY);
        assertEquals(0, board.getMarkers().get(0).lastVisitedBy);

        // player 0
        board.executeMove(Move.LEFT);
        assertEquals(0, board.getMarkers().get(0).lastVisitedBy);

        // player 1
        board.executeMove(Move.LEFT);
        assertEquals(1, board.getMarkers().get(0).lastVisitedBy);

        assertEquals(0, board.getEndCondition().getWinner());

        assertEquals(3, fc.getScore(0));
        assertEquals(1, fc.getScore(1));
    }

    @Test
    public void twoUnicornsOneFlagDraw() {
        IBoard board = Board.fromLevelRepresentation(Arrays.asList("#####", "#pmp#", "#####"));
        PointCollecting fc = new PointCollecting();
        board.setEndCondition(fc);

        assertEquals(-1, board.getMarkers().get(0).lastVisitedBy);

        // player 0
        board.executeMove(Move.RIGHT);
        assertEquals(0, board.getMarkers().get(0).lastVisitedBy);

        // player 1
        board.executeMove(Move.STAY);
        assertEquals(0, board.getMarkers().get(0).lastVisitedBy);

        // player 0
        board.executeMove(Move.LEFT);
        assertEquals(0, board.getMarkers().get(0).lastVisitedBy);

        // player 1
        board.executeMove(Move.LEFT);
        assertEquals(1, board.getMarkers().get(0).lastVisitedBy);

        // player 0
        board.executeMove(Move.LEFT);

        // player 1
        board.executeMove(Move.STAY);

        assertEquals(-1, board.getEndCondition().getWinner());

        assertEquals(3, fc.getScore(0));
        assertEquals(3, fc.getScore(1));
    }

    @Test
    public void twoUnicornsOnOneFlagSimultaenously() {
        BoardWithHistory board = new BoardWithHistory(Board.fromLevelRepresentation(Arrays.asList("#####", "#pmp#",
                "#####")));

        PointCollecting fc = new PointCollecting();
        board.setEndCondition(fc);

        assertEquals(Marker.LAST_VISITED_BY_DEFAULT, board.getMarkers().get(0).lastVisitedBy);

        // player 0
        board.executeMove(Move.RIGHT);
        assertEquals(0, board.getMarkers().get(0).lastVisitedBy);

        // player 1 -- as soon as this player steps on the marker, the marker is
        // neutral again
        board.executeMove(Move.LEFT);
        assertEquals(Marker.LAST_VISITED_BY_DEFAULT, board.getMarkers().get(0).lastVisitedBy);

        board.executeMove(Move.STAY);
        board.executeMove(Move.STAY);
        board.executeMove(Move.STAY);
        board.executeMove(Move.STAY);

        assertEquals(0, board.getEndCondition().getWinner());

        assertEquals(1, fc.getScore(0));
        assertEquals(0, fc.getScore(1));
    }

}
