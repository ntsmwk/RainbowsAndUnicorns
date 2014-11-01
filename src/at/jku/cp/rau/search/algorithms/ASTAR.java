package at.jku.cp.rau.search.algorithms;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.SearchUtils;
import at.jku.cp.rau.search.predicates.Predicate;

// A* Search
public class ASTAR<T extends Node<T>> implements Search<T> {
    private Function<T> cost;
    private Function<T> heuristic;

    private Set<T> closedset = new HashSet<>();
    private Set<T> openset = new HashSet<>();

    private HashMap<T, T> routes = new HashMap<T, T>();
    private Map<T, Double> costMap = new HashMap<>();
    private Map<T, Double> functionMap = new HashMap<>();

    public ASTAR(Function<T> costs, Function<T> heuristic) {
        this.cost = costs;
        this.heuristic = heuristic;
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        changeMaps(start, 0);
        addToOpenset(start, 0);

        while (!openset.isEmpty()) {
            System.out.println(openset.toString());
            T current = getMinElement();
            System.out.println(current);
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
                    changeMaps(neighbor, costOfNeighbor);
                    if (!openset.contains(neighbor)) {
                        addToOpenset(neighbor, costOfNeighbor);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private void addRoute(T current, T neighbor) {
        routes.put(neighbor, current);
    }

    private T getMinElement() {
        return Collections.min(openset, new CostComparator());
    }

    private void addToOpenset(T node, double cost) {
        openset.add(node);
    }

    private void changeMaps(T node, double cost) {
        costMap.put(node, cost);
        functionMap.put(node, cost + heuristic.value(node));
    }

    private final class CostComparator implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            return functionMap.get(o1).compareTo(functionMap.get(o2));
        }
    }

}
