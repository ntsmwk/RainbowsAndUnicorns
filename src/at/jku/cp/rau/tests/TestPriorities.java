package at.jku.cp.rau.tests;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.Seed;
import at.jku.cp.rau.game.objects.V;

public class TestPriorities {

    @Test
    public void ticks() {
        List<String> fixture = new ArrayList<>();

        fixture.add("####");
        fixture.add("#pp#");
        fixture.add("####");

        IBoard actual = Board.fromLevelRepresentation(fixture);

        assertEquals(0, actual.getTick());

        actual.executeMove(Move.STAY);

        assertEquals(1, actual.getTick());
    }

    @Test
    public void movement() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        List<String> fixture = new ArrayList<>();

        fixture.add("####");
        fixture.add("#pp#");
        fixture.add("####");

        IBoard actualBoard = Board.fromLevelRepresentation(fixture);

        // tick 0
        // left unicorn (id:0) wants to spawn a seed
        // right unicorn (id:1) wants to move left

        actualBoard.executeMove(Move.SPAWN);
        assertEquals(1, actualBoard.getTick());

        actualBoard.executeMove(Move.LEFT);
        assertEquals(2, actualBoard.getTick());

        List<String> _expectedBoard = new ArrayList<>();

        _expectedBoard.add("####");
        _expectedBoard.add("#pp#");
        _expectedBoard.add("####");

        Board expectedBoard = Board.fromLevelRepresentation(_expectedBoard);
        expectedBoard.getSeeds().add(new Seed(new V(1, 1), Seed.DEFAULT_FUSE - 2, Seed.DEFAULT_RANGE));
        Field field = Board.class.getDeclaredField("tick");
        field.setAccessible(true);
        field.set(expectedBoard, 2);

        assertEquals(expectedBoard, actualBoard);
    }

}
