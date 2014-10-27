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
    private List<T> closedList;
    private LinkedList<T> nextNodes;

    public BFS() {
        this.closedList = new ArrayList<>();
        this.nextNodes = new LinkedList<>();
        this.route = new HashMap<T, T>();
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        this.start = start;
        nextNodes.add(start);
        return breadthFirstSearch(endPredicate);
    }

    private List<T> breadthFirstSearch(Predicate<T> endPredicate) {
        while (!nextNodes.isEmpty()) {
            T current = getNextUnvisitedNode();

            closedList.add(current);
            addAdjacentRoutes(current);
            if (endPredicate.isTrueFor(current)) {
                return SearchUtils.buildBackPath(current, start, route);
            }
            concatLists(nextNodes, current.adjacent());
        }
        return Collections.emptyList();
    }

    private T getNextUnvisitedNode() {
        T current = nextNodes.removeFirst();
        while (isVisited(current)) {
            if (nextNodes.isEmpty()) {
                return null;
            }
            current = nextNodes.removeFirst();
        }
        return current;
    }

    private boolean isVisited(T element) {
        return closedList.contains(element);
    }

    private void concatLists(List<T> first, List<T> second) {
        for (T element : second) {
            // if(!isVisited(element)){
            first.add(element);
            // }
        }
    }

    private void addAdjacentRoutes(T current) {
        for (T element : current.adjacent()) {
            if (!isVisited(element) && !route.containsKey(element)) {
                addRoute(element, current);
            }
        }
    }

    private void addRoute(T current, T next) {
        route.put(current, next);
    }

}
