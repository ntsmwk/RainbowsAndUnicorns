package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.SearchUtils;
import at.jku.cp.rau.search.predicates.Predicate;

// Breadth-First search
public class BFS<T extends Node<T>> implements Search<T> {
	private T start;
	private Map<T, T> route;
	private List<T> closedList = new ArrayList<>();
	// private List<T> searchList = new ArrayList<>();
	private LinkedList<T> nextNodes = new LinkedList<>();

	@Override
	public List<T> search(T start, Predicate<T> endPredicate) {
		this.start = start;
		this.route = new HashMap<T, T>();
		nextNodes.add(start);
		return breadthFirstSearch(endPredicate);
	}

	private List<T> breadthFirstSearch(Predicate<T> endPredicate) {
		while (!nextNodes.isEmpty()) {
			T current = nextNodes.removeFirst();
			
			while (isVisited(current)) {
				if (nextNodes.isEmpty()) {
					return Collections.emptyList();
				}
				current = nextNodes.removeFirst();
			}
			closedList.add(current);
			System.out.println(current.toString() + current.adjacent().toString());
			addAdjacentRoutes(current);
			if (endPredicate.isTrueFor(current)) {
				return SearchUtils.buildBackPath(current, start, route);
			}
			concatLists(nextNodes, current.adjacent());

		}
		return Collections.emptyList();
	}

	private boolean isVisited(T element) {
		return closedList.contains(element);
	}

	private void concatLists(List<T> first, List<T> second) {
		for (T element : second) {
			if(!isVisited(element)){
				first.add(element);
			}
		}
	}
	
	private void addAdjacentRoutes(T current){
		for(T element : current.adjacent()){
			if(!isVisited(element)){
				addRoute(element, current);
			}
		}
	}

	private void addRoute(T current, T next) {
		route.put(current, next);
	}

}
