package at.jku.cp.rau.utils.visualize;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.functions.ExplicitCost;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.algorithms.ASTAR;
import at.jku.cp.rau.search.algorithms.BFS;
import at.jku.cp.rau.search.algorithms.IDS;
import at.jku.cp.rau.search.algorithms.RS;
import at.jku.cp.rau.search.nodes.ContainsBoard;
import at.jku.cp.rau.search.nodes.ContainsMove;
import at.jku.cp.rau.search.nodes.SafeBNode;
import at.jku.cp.rau.search.predicates.Predicate;
import at.jku.cp.rau.utils.RenderUtils;

public class VisualizeExamples
{
	public static <T extends Node<T> & ContainsBoard & ContainsMove> void visualize(
			String dirname,
			String name,
			IBoard board,
			Class<T> clazz, Search<GraphTelemetry<T>> searcher)
			throws Exception
	{
		Predicate<GraphTelemetry<T>> endReached = new Predicate<GraphTelemetry<T>>()
		{
			@Override
			public boolean isTrueFor(GraphTelemetry<T> current)
			{
				return ((ContainsBoard) current.content).getBoard().isRunning()
						&& ((ContainsBoard) current.content).getBoard()
								.getUnicorns().get(0).pos.equals(
								((ContainsBoard) current.content).getBoard()
										.getMarkers().get(0).pos);

			}
		};

		Constructor<T> cons = clazz.getConstructor(IBoard.class);

		Graph<T> graph = new Graph<>();
		List<GraphTelemetry<T>> path = searcher.search(
				new GraphTelemetry<T>(graph, cons.newInstance(board)),
				endReached);

		//System.out.println(path.size());

		List<T> unwrappedPath = new ArrayList<>();

		for (GraphTelemetry<T> gtnode : path)
		{
			unwrappedPath.add(gtnode.content);
		}

		RenderUtils.visualizeSearchGraph(
				dirname + name + "_states",
				graph,
				unwrappedPath);

		RenderUtils.visualizeSearchTree(
				dirname + name + "_tree",
				graph,
				unwrappedPath);

		RenderUtils.visualizeSearchPath(dirname + name + "_path",
				unwrappedPath);
	}

	public static void main(String[] args) throws Exception
	{
		List<String> lvl = Arrays.asList(
				"#####",
				"#p..#",
				"#..m#",
				"#####"
				);

		List<String> costs = Arrays.asList(
				"#####",
				"#111#",
				"#111#",
				"#####"
				);

		final Function<V> ecf = new ExplicitCost<V>(costs);

		Function<GraphTelemetry<SafeBNode>> costfunction = new Function<GraphTelemetry<SafeBNode>>()
		{
			@Override
			public double value(GraphTelemetry<SafeBNode> t)
			{
				List<Unicorn> unicorns = t.content.board.getUnicorns();
				Unicorn current = t.content.board.getCurrentUnicorn();
				if (unicorns.size() > 0)
				{
					if (current.id == 0)
						return ecf.value(current.pos);
					else
						return 0;
				}

				return Double.POSITIVE_INFINITY;
			}
		};

		Function<GraphTelemetry<SafeBNode>> heuristic = new Function<GraphTelemetry<SafeBNode>>()
		{
			@Override
			public double value(GraphTelemetry<SafeBNode> t)
			{
				List<Unicorn> unicorns = t.content.board.getUnicorns();
				Unicorn current = t.content.board.getCurrentUnicorn();
				Marker marker = t.content.board.getMarkers().get(0);
				if (unicorns.size() > 0)
				{
					if (current.id == 0)
					{
						return V.euclidean(current.pos, marker.pos);
					}
				}

				return 0;
			}
		};

		Board board = Board.fromLevelRepresentation(lvl);

		visualize("/home/rainer/Coding/cp/scratch/graphs/", "bfs",
				board.copy(), SafeBNode.class,
				new BFS<GraphTelemetry<SafeBNode>>());

		visualize("/home/rainer/Coding/cp/scratch/graphs/", "ids",
				board.copy(), SafeBNode.class,
				new IDS<GraphTelemetry<SafeBNode>>(100));

		visualize("/home/rainer/Coding/cp/scratch/graphs/", "astar",
				board.copy(), SafeBNode.class,
				new ASTAR<GraphTelemetry<SafeBNode>>(costfunction, heuristic));

		visualize("/home/rainer/Coding/cp/scratch/graphs/", "random",
				board.copy(), SafeBNode.class,
				new RS<GraphTelemetry<SafeBNode>>());
	}
}
