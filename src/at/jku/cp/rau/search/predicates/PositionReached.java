package at.jku.cp.rau.search.predicates;

import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.nodes.PNode;

public class PositionReached implements Predicate<PNode>
{
	private V end;
	public PositionReached(V end)
	{
		this.end = end;
	}
	
	@Override
	public boolean isTrueFor(PNode current)
	{
		return end.equals(current.getPos());
	}
}
