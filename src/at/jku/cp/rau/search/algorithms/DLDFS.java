package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.SearchUtils;
import at.jku.cp.rau.search.datastructures.StackWithFastContains;
import at.jku.cp.rau.search.predicates.Predicate;

// Depth-Limited Depth-First Search
public class DLDFS<T extends Node<T>> implements Search<T> {
    private int limit;
    private T start;
    private List<T> closedList;
    private Map<T, T> route;
    private StackWithFastContains<T> stack;

    public DLDFS(int limit) {
        this.limit = limit;
        this.closedList = new ArrayList<>();
        this.route = new HashMap<T, T>();
        stack = new StackWithFastContains<T>();
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        this.start = start;
        stack.push(start);
        return depthFristSearch(endPredicate);
    }

    private List<T> depthFristSearch(Predicate<T> endPredicate) {
        closedList.add(start);
        while (!stack.isEmpty()) {
            // T current = getNextUnvisitedNode();
            T current = stack.peek();
            if (endPredicate.isTrueFor(current)) {
                // end founded
                return SearchUtils.buildBackPath(current, start, route);
            }
            // closedList.add(current);
            T child = getChild(current);
            if (child != null) {
                closedList.add(child);
                stack.push(child);
                addRoute(child, current);
            } else {
                stack.pop();
            }
        }
        return Collections.emptyList();
    }

    private boolean isVisited(T element) {
        return closedList.contains(element);
    }

    private T getChild(T current) {
        for (T element : current.adjacent()) {
            if (!isVisited(element) && !route.containsKey(element)) {
                return element;
            }
        }
        return null;
    }

    private void addRoute(T current, T next) {
        route.put(current, next);
    }
}