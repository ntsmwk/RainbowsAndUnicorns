package at.jku.cp.rau.search.nodes;

import java.util.ArrayList;
import java.util.List;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.Node;

public class PNode implements Node<PNode>, ContainsPos, ContainsMove
{
	private Move move;
	private V pos;
	private IBoard board;

	public PNode(IBoard board, Move move, V pos)
	{
		this.board = board;
		this.move = move;
		this.pos = pos;
	}

	public PNode(IBoard board, V pos)
	{
		this(board, null, pos);
	}
	
	@Override
	public Move getMove()
	{
		return move;
	}
	
	@Override
	public V getPos()
	{
		return pos;
	}
	
	@Override
	public PNode current()
	{
		return this;
	}

	@Override
	public List<PNode> adjacent()
	{
		List<PNode> adjacent = new ArrayList<>();

		for (V d : Board.getDirections())
		{
			V apos = V.add(pos, d);
			if (board.isPassable(apos))
			{
				adjacent.add(new PNode(board, Board.getDirectionToMoveMapping().get(d), apos));
			}
		}

		return adjacent;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PNode other = (PNode) obj;
		if (pos == null)
		{
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return pos.toString();
	}
}
