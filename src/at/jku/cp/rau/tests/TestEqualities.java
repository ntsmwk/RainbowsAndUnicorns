package at.jku.cp.rau.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Cloud;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.Path;
import at.jku.cp.rau.game.objects.Rainbow;
import at.jku.cp.rau.game.objects.Seed;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.game.objects.Wall;
import at.jku.cp.rau.search.nodes.BNode;

public class TestEqualities {
    @Test
    public void vs() {
        assertEquals(new V(0, 12), new V(0, 12));
        assertNotEquals(new V(12, 12), new V(12, 13));
    }

    @Test
    public void clouds() {
        assertEquals(new Cloud(new V(0, 1)), new Cloud(new V(0, 1)));
        assertNotEquals(new Cloud(new V(1, 1)), new Cloud(new V(0, 0)));
    }

    @Test
    public void rainbows() {
        assertEquals(new Rainbow(new V(0, 1), 10), new Rainbow(new V(0, 1), 10));
        assertNotEquals(new Rainbow(new V(1, 1), 10), new Rainbow(new V(0, 1), 10));
        assertNotEquals(new Rainbow(new V(0, 1), 11), new Rainbow(new V(0, 1), 10));
    }

    @Test
    public void unicorns() {
        assertEquals(new Unicorn(new V(0, 1), 0), new Unicorn(new V(0, 1), 0));
        assertNotEquals(new Unicorn(new V(1, 1), 0), new Unicorn(new V(0, 1), 0));
        assertNotEquals(new Unicorn(new V(0, 1), 1), new Unicorn(new V(0, 1), 0));
    }

    @Test
    public void seeds() {
        assertEquals(new Seed(new V(0, 1), 0, 3, 5), new Seed(new V(0, 1), 0, 3, 5));
        assertNotEquals(new Seed(new V(1, 1), 0, 3, 5), new Seed(new V(0, 1), 0, 3, 5));
        assertNotEquals(new Seed(new V(0, 1), 0, 4, 5), new Seed(new V(0, 1), 0, 3, 5));
        assertNotEquals(new Seed(new V(0, 1), 0, 3, 6), new Seed(new V(0, 1), 0, 3, 5));
    }

    @Test
    public void walls() {
        assertEquals(new Wall(new V(39, 30)), new Wall(new V(39, 30)));
        assertNotEquals(new Wall(new V(39, 29)), new Wall(new V(0, 0)));
    }

    @Test
    public void paths() {
        assertEquals(new Path(new V(39, 30)), new Path(new V(39, 30)));
        assertNotEquals(new Path(new V(39, 29)), new Path(new V(0, 0)));
    }

    @Test
    public void boards() {
        assertEquals(new Board(), new Board());

        Board a = Board.fromLevelFile(Constants.ASSET_PATH + "/default.lvl");
        Board b = Board.fromLevelFile(Constants.ASSET_PATH + "/default.lvl");

        assertEquals(a, b);

        for (IBoard board : Arrays.asList(a, b)) {
            board.executeMove(Move.DOWN);
            board.executeMove(Move.UP);
        }

        assertEquals(a, b);

        IBoard c = Board.fromLevelFile(Constants.ASSET_PATH + "/default.lvl");
        Board d = Board.fromLevelFile(Constants.ASSET_PATH + "/default.lvl");

        // Unicorn player0 = c.getMyUnicorn(0);
        c.executeMove(Move.DOWN);

        assertNotEquals(c, d);

        IBoard e = d.copy();

        assertEquals(d, e);
    }

    @Test
    public void boards2() {
        IBoard a = Board.fromLevelFile(Constants.ASSET_PATH + "/default.lvl");
        a.getMarkers().add(new Marker(new V(1, 1), -1));
        a.getSeeds().add(new Seed(new V(7, 1), 0, 2, 2));
        a.getRainbows().add(new Rainbow(new V(6, 1), 2));

        IBoard b = a.deepCopy();

        assertEquals(a, b);
    }

    @Test
    public void boardsWithoutTicks() {
        Board a = Board.fromLevelFile(Constants.ASSET_PATH + "/default.lvl");

        IBoard b = a.copy();
        b.executeMove(Move.STAY);

        assertEquals(a, b);
    }

    @Test
    public void boardsBackAndForth() {
        Board a = Board.fromLevelFile(Constants.ASSET_PATH + "/default.lvl");

        IBoard b = a.copy();
        b.executeMove(Move.RIGHT);
        b.executeMove(Move.STAY);
        b.executeMove(Move.LEFT);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void boardNodes() {
        List<String> lvl = Arrays.asList("#####", "#p..#", "#...#", "#..p#", "#####");
        BNode a = new BNode(Move.STAY, Board.fromLevelRepresentation(lvl));
        BNode b = new BNode(Move.STAY, a.board);
        b.board.executeMove(Move.RIGHT);
        b.board.executeMove(Move.STAY);
        b.board.executeMove(Move.LEFT);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
