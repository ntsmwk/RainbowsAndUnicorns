package at.jku.cp.rau.search.algorithms;

import java.util.Collections;
import java.util.List;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// A* Search
public class ASTAR<T extends Node<T>> implements Search<T>
{
	private Function<T> cost;
	private Function<T> heuristic;
	
	public ASTAR(Function<T> costs, Function<T> heuristic)
	{
		this.cost = costs;
		this.heuristic = heuristic;
	}
	
	@Override
	public List<T> search(T start, Predicate<T> endPredicate)
	{
		//TODO: implement A* search 
		return Collections.emptyList();
	}
}
