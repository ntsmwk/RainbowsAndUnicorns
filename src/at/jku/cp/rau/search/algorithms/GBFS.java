package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.SearchUtils;
import at.jku.cp.rau.search.datastructures.StablePriorityQueue;
import at.jku.cp.rau.search.predicates.Predicate;
import at.jku.cp.rau.utils.Pair;

// Greedy Best-First Search
public class GBFS<T extends Node<T>> implements Search<T> {
    private Function<T> heuristic;
    private StablePriorityQueue<Double, T> fringe;
    private Map<T, T> route;
    private List<T> closedList;
    private T start;

    public GBFS(Function<T> heuristic) {
        this.heuristic = heuristic;
        this.route = new HashMap<T, T>();
        this.fringe = new StablePriorityQueue<>();
        this.closedList = new ArrayList<>();

    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        this.start = start;
        this.fringe.add(new Pair<Double, T>(0.0, start));

        return greedyBestFirstSearch(endPredicate);
    }

    private List<T> greedyBestFirstSearch(Predicate<T> endPredicate) {
        while (!fringe.isEmpty()) {
            T current = getNextUnvisitedNode();
            if (current == null) {
                break;
            }
            closedList.add(current);
            addAdjacentRoutes(current);
            if (endPredicate.isTrueFor(current)) {
                return SearchUtils.buildBackPath(current, start, route);
            }

            addAdjacentToFringe(current);
        }
        return null;
    }

    private T getNextUnvisitedNode() {
        T current = fringe.remove().s;
        while (isVisited(current)) {
            if (fringe.isEmpty()) {
                return null;
            }
            current = fringe.remove().s;
        }
        return current;
    }

    private boolean isVisited(T element) {
        return closedList.contains(element);
    }

    private void addAdjacentToFringe(T element) {
        for (T next : element.adjacent()) {
            fringe.add(new Pair<Double, T>(heuristic.value(next), next));
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
