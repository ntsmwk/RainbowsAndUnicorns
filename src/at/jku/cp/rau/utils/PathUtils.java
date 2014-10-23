package at.jku.cp.rau.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.nodes.BNode;
import at.jku.cp.rau.search.nodes.PNode;

public class PathUtils
{
	public static void toFile(String filename, List<V> path)
	{
		try
		{
			if (Files.exists(Paths.get(filename)))
				Files.delete(Paths.get(filename));
			List<String> lines = new ArrayList<>();
			for (V v : path)
				lines.add(v.toString());

			Files.write(Paths.get(filename), lines, StandardCharsets.UTF_8,
					StandardOpenOption.CREATE_NEW);
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static List<V> fromFile(String filename)
	{
		try
		{
			List<String> lines = Files.readAllLines(
					Paths.get(filename), StandardCharsets.UTF_8);
			List<V> path = new ArrayList<>();
			for (String line : lines)
				path.add(V.fromString(line));
			return path;
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static List<Move> vToMoves(List<V> path)
	{
		List<Move> moves = new ArrayList<>();
		for (int i = 1; i < path.size(); i++)
		{
			V previous = path.get(i - 1);
			V current = path.get(i);

			V d = V.sub(current, previous);
			if (Board.getDirectionToMoveMapping().containsKey(d))
			{
				moves.add(Board.getDirectionToMoveMapping().get(d));
			}
			else
			{
				throw new RuntimeException("the path is broken!");
			}
		}
		return moves;
	}

	public static List<Move> pNodeToMoves(List<PNode> path)
	{
		if (path.isEmpty())
			return Collections.emptyList();

		List<Move> moves = new ArrayList<>();
		// first is always zero
		for (PNode node : path.subList(1, path.size()))
		{
			moves.add(node.getMove());
		}
		return moves;
	}

	public static List<PNode> vToPNodes(List<V> path,
			IBoard board)
	{
		if (path.isEmpty())
			return Collections.emptyList();

		List<PNode> nodes = new ArrayList<>();
		nodes.add(new PNode(board, path.get(0)));

		for (int i = 1; i < path.size(); i++)
		{
			V previous = path.get(i - 1);
			V current = path.get(i);

			V d = V.sub(current, previous);
			if (Board.getDirectionToMoveMapping().containsKey(d))
			{
				nodes.add(new PNode(
						board,
						Board.getDirectionToMoveMapping().get(d),
						current));
			}
			else
			{
				throw new RuntimeException("the path is broken!");
			}
		}
		return nodes;
	}

	public static List<V> pNodeToVs(List<PNode> path)
	{
		List<V> positions = new ArrayList<>();
		for(PNode node: path)
			positions.add(node.getPos());
		return positions;
	}

	public static List<BNode> moveToBNodes(List<Move> path,
			IBoard board)
	{
		List<BNode> nodes = new ArrayList<>();

		BNode start = new BNode(board);
		if (path.size() > 0)
		{
			nodes.add(start);

			BNode current = new BNode(board.copy());

			for (Move move : path)
			{
				current.board.executeMove(move);
				nodes.add(
						new BNode(move,
								current.board.copy()));
			}
		}

		return nodes;
	}

	public static List<V> asList(int... coords)
	{
		List<V> lst = new ArrayList<>();

		if (coords.length % 2 == 1)
		{
			throw new RuntimeException(
					"invalid length! must be a multiple of 2!");
		}

		for (int i = 0; i < coords.length; i += 2)
		{
			lst.add(new V(coords[i], coords[i + 1]));
		}

		return lst;
	}
}
