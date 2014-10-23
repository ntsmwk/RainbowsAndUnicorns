package at.jku.cp.rau.search.algorithms;

import java.util.Collections;
import java.util.List;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// Breadth-First search
public class BFS<T extends Node<T>> implements Search<T>
{
	@Override
	public List<T> search(T start, Predicate<T> endPredicate)
	{
		//TODO: Implement Breadth-First Search
		return Collections.emptyList();
	}
}
