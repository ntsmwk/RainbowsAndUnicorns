package at.jku.cp.rau.tests.assignment1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.functions.EuclideanDistance;
import at.jku.cp.rau.game.functions.ExplicitCost;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.game.functions.ManhattanDistance;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.algorithms.ASTAR;
import at.jku.cp.rau.search.nodes.PNode;
import at.jku.cp.rau.search.predicates.PositionReached;
import at.jku.cp.rau.utils.PathUtils;
import at.jku.cp.rau.utils.TestUtils;

@RunWith(Parameterized.class)
public class TestExercise1ASTAR {
    @Parameters
    public static Collection<Object[]> generateParams() {
        List<Object[]> params = new ArrayList<Object[]>();

        for (int i = 0; i < Config.N_TESTS; i++) {
            params.add(new Object[] { String.format("assets/assignment1/L%d/level", i),
                    String.format("assets/assignment1/L%d/costs", i),
                    String.format("assets/assignment1/L%d/astar_mh.path", i) });

            params.add(new Object[] { String.format("assets/assignment1/L%d/level", i),
                    String.format("assets/assignment1/L%d/costs", i),
                    String.format("assets/assignment1/L%d/astar_ec.path", i) });
        }

        return params;
    }

    private List<PNode> expectedPath;
    private Function<PNode> heuristic;
    private Function<PNode> costs;
    private IBoard board;

    public TestExercise1ASTAR(String levelName, String costName, String pathName) throws IOException {
        board = Board.fromLevelFile(levelName);
        expectedPath = PathUtils.vToPNodes(PathUtils.fromFile(pathName), board);

        List<String> _costs = Files.readAllLines(Paths.get(costName), StandardCharsets.UTF_8);

        this.costs = new ExplicitCost<PNode>(_costs);

        if (pathName.contains("_mh")) {
            this.heuristic = new ManhattanDistance<PNode>(expectedPath.get(expectedPath.size() - 1).getPos());
        } else if (pathName.contains("_ec")) {
            this.heuristic = new EuclideanDistance<PNode>(expectedPath.get(expectedPath.size() - 1).getPos());
        }
    }

    @Test
    public void pathToLocationWithASTAROnLevel() {
        Search<PNode> searcher = new ASTAR<PNode>(costs, heuristic);

        Unicorn player = board.getCurrentUnicorn();

        V start = player.pos;
        final Marker end = board.getMarkers().get(0);

        List<PNode> actualPath = searcher.search(new PNode(board, start), new PositionReached(end.pos));

        TestUtils.assertListEquals(expectedPath, actualPath);
    }
}