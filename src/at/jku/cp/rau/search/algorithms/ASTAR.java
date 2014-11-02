package at.jku.cp.rau.search.algorithms;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.SearchUtils;
import at.jku.cp.rau.search.predicates.Predicate;

// A* Search
public class ASTAR<T extends Node<T>> implements Search<T> {
    private static final int CAPACITY = 20;

    private Function<T> cost;
    private Function<T> heuristic;

    private Set<T> closedset = new HashSet<>();
    private PriorityQueue<T> openset = new PriorityQueue<>(CAPACITY, new CostComparator());

    private Map<T, T> routes = new HashMap<>();
    private Map<T, Double> costMap = new HashMap<>();
    private Map<T, Double> scoreMap = new HashMap<>();

    public ASTAR(Function<T> costs, Function<T> heuristic) {
        this.cost = costs;
        this.heuristic = heuristic;
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        changeCost(start, 0);
        addToOpenset(start);

        while (!openset.isEmpty()) {
            T current = getMinElement();
            if (endPredicate.isTrueFor(current)) {
                return SearchUtils.buildBackPath(current, start, routes);
            }
            openset.remove(current);
            closedset.add(current);
            for (T neighbor : current.adjacent()) {
                if (closedset.contains(neighbor)) {
                    continue;
                }
                double costOfNeighbor = costMap.get(current) + cost.value(neighbor);
                if (!openset.contains(neighbor) || costOfNeighbor < costMap.get(neighbor)) {
                    addRoute(current, neighbor);
                    changeCost(neighbor, costOfNeighbor);
                    addToOpenset(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }

    private T getMinElement() {
        return openset.poll();
    }

    private void addToOpenset(T node) {
        if (openset.contains(node)) {
            openset.remove(node);
        }
        openset.add(node);
    }

    private void addRoute(T current, T neighbor) {
        routes.put(neighbor, current);
    }

    private void changeCost(T node, double cost) {
        costMap.put(node, cost);
        scoreMap.put(node, cost + heuristic.value(node));
    }

    private final class CostComparator implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            double scoreO1 = scoreMap.get(o1);
            double scoreO2 = scoreMap.get(o2);
            return scoreO1 > scoreO2 ? 1 : scoreO1 < scoreO2 ? -1 : 0;
        }
    }

}
