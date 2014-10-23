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
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.algorithms.DLDFS;
import at.jku.cp.rau.search.nodes.PNode;
import at.jku.cp.rau.search.predicates.PositionReached;
import at.jku.cp.rau.utils.PathUtils;
import at.jku.cp.rau.utils.TestUtils;

@RunWith(Parameterized.class)
public class TestExercise1DFS
{
	@Parameters
	public static Collection<Object[]> generateParams()
	{
		List<Object[]> params = new ArrayList<Object[]>();

		for (int i = 0; i < Config.N_TESTS; i++)
		{
			params.add(new Object[] {
					String.format("assets/assignment1/L%d/level", i),
					String.format("assets/assignment1/L%d/dfs.path", i)
			});
		}

		return params;
	}

	private String levelName;
	private String pathName;

	public TestExercise1DFS(String levelName, String pathName)
	{
		this.levelName = levelName;
		this.pathName = pathName;
	}

	@Test
	public void pathToLocationWithDFSOnLevel()
	{
		Search<PNode> searcher = new DLDFS<PNode>(-1);

		IBoard board = Board.fromLevelFile(levelName);
		V start = board.getCurrentUnicorn().pos;
		final Marker end = board.getMarkers().get(0);
		
		List<PNode> expectedPath = PathUtils.vToPNodes(PathUtils.fromFile(pathName), board);

		List<PNode> actualPath = searcher.search(
				new PNode(board, start),
				new PositionReached(end.pos));

		TestUtils.assertListEquals(expectedPath, actualPath);
	}

}
