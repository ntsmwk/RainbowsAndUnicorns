package at.jku.cp.rau.search;

import java.util.List;

import at.jku.cp.rau.search.predicates.Predicate;

public interface  Search<T extends Node<T>>
{
	List<T> search(T start, Predicate<T> endPredicate);
}
