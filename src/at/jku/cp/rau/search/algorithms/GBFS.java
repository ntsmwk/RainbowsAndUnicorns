package at.jku.cp.rau.search.algorithms;

import java.util.Collections;
import java.util.List;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// Greedy Best-First Search
public class GBFS<T extends Node<T>> implements Search<T>
{
	private Function<T> heuristic;
	
	public GBFS(Function<T> heuristic)
	{
		this.heuristic = heuristic;
	}
	
	@Override
	public List<T> search(T start, Predicate<T> endPredicate)
	{
		//TODO: Implement Greedy Best-First Search
		return Collections.emptyList();
	}
}
