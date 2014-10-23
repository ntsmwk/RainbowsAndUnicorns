package at.jku.cp.rau.game.functions;

import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.nodes.ContainsPos;

public class ManhattanDistance<T extends ContainsPos> implements Function<T>
{
	V pos;

	public ManhattanDistance(V pos)
	{
		this.pos = pos;
	}

	@Override
	public double value(T current)
	{
		return V.manhattan(pos, current.getPos());
	}

}
