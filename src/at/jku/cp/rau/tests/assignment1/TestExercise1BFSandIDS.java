package at.jku.cp.rau.tests.assignment1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.algorithms.BFS;
import at.jku.cp.rau.search.algorithms.IDS;
import at.jku.cp.rau.search.nodes.BNode;
import at.jku.cp.rau.search.nodes.PNode;
import at.jku.cp.rau.search.predicates.PositionReached;
import at.jku.cp.rau.search.predicates.Predicate;
import at.jku.cp.rau.utils.PathUtils;
import at.jku.cp.rau.utils.TestUtils;

@RunWith(Parameterized.class)
public class TestExercise1BFSandIDS {
    @Parameters
    public static Collection<Object[]> generateParams() {
        List<Object[]> params = new ArrayList<Object[]>();

        for (int i = 0; i < Config.N_TESTS; i++) {
            params.add(new Object[] { String.format("assets/assignment1/L%d/level", i),
                    String.format("assets/assignment1/L%d/bfs.path", i) });
        }

        return params;
    }

    private String levelName;
    private String pathName;

    public TestExercise1BFSandIDS(String levelName, String pathName) {
        this.levelName = levelName;
        this.pathName = pathName;
    }

    @Test
    public void pathToLocationWithBFSOnLevel() {
        pathToLocationWithShortestWaySearchOnLevel(new BFS<PNode>());
    }

    @Test
    public void pathToLocationWithBFSOnBoardStates() {
        pathToLocationWithShortestWaySearchOnBoardStates(new BFS<BNode>());
    }

    @Test
    public void pathToLocationWithIDSOnLevel() {
        pathToLocationWithShortestWaySearchOnLevel(new IDS<PNode>(1000));
    }

    private void pathToLocationWithShortestWaySearchOnLevel(Search<PNode> searcher) {
        IBoard board = Board.fromLevelFile(levelName);
        Unicorn player = board.getCurrentUnicorn();

        V start = player.pos;
        final Marker end = board.getMarkers().get(0);

        List<PNode> expectedPath = PathUtils.vToPNodes(PathUtils.fromFile(pathName), board);

        List<PNode> actualPath = searcher.search(new PNode(board, start), new PositionReached(end.pos));

        TestUtils.assertListEquals(expectedPath, actualPath);
    }

    private void pathToLocationWithShortestWaySearchOnBoardStates(Search<BNode> searcher) {
        Board board = Board.fromLevelFile(levelName);

        IBoard start = board.copy();
        final Marker end = board.getMarkers().get(0);

        Predicate<BNode> endReached = new Predicate<BNode>() {
            @Override
            public boolean isTrueFor(BNode current) {
                return current.board.isRunning() && current.board.getCurrentUnicorn().pos.equals(end.pos);
            }
        };

        List<V> expectedPath = PathUtils.fromFile(pathName);
        List<Move> expectedMoveSequence = PathUtils.vToMoves(expectedPath);
        List<BNode> expectedBoardStates = PathUtils.moveToBNodes(expectedMoveSequence, board);

        List<BNode> actualBoardStates = searcher.search(new BNode(start), endReached);

        TestUtils.assertListEquals(expectedBoardStates, actualBoardStates);
    }
}
