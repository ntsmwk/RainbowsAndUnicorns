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
import at.jku.cp.rau.game.functions.ExplicitCost;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.algorithms.UCS;
import at.jku.cp.rau.search.nodes.PNode;
import at.jku.cp.rau.search.predicates.PositionReached;
import at.jku.cp.rau.utils.PathUtils;
import at.jku.cp.rau.utils.TestUtils;

@RunWith(Parameterized.class)
public class TestExercise1UCS {
    @Parameters
    public static Collection<Object[]> generateParams() {
        List<Object[]> params = new ArrayList<Object[]>();

        for (int i = 0; i < Config.N_TESTS; i++) {
            params.add(new Object[] { String.format("assets/assignment1/L%d/level", i),
                    String.format("assets/assignment1/L%d/costs", i),
                    String.format("assets/assignment1/L%d/ucs.path", i) });
        }

        return params;
    }

    private String levelName;
    private String pathName;
    private Function<PNode> costs;

    public TestExercise1UCS(String levelName, String costName, String pathName) throws IOException {
        this.levelName = levelName;
        this.pathName = pathName;

        List<String> _costs = Files.readAllLines(Paths.get(costName), StandardCharsets.UTF_8);

        this.costs = new ExplicitCost<PNode>(_costs);
    }

    @Test
    public void pathToLocationWithUCSOnLevel() {
        pathToLocationWithSearchOnLevel(new UCS<PNode>(costs));
    }

    private void pathToLocationWithSearchOnLevel(Search<PNode> searcher) {
        IBoard board = Board.fromLevelFile(levelName);
        Unicorn player = board.getCurrentUnicorn();

        V start = player.pos;
        final Marker end = board.getMarkers().get(0);
        List<PNode> expectedPath = PathUtils.vToPNodes(PathUtils.fromFile(pathName), board);

        List<PNode> actualPath = searcher.search(new PNode(board, start), new PositionReached(end.pos));

        TestUtils.assertListEquals(expectedPath, actualPath);
    }
}