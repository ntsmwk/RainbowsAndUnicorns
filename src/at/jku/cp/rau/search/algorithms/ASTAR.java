package at.jku.cp.rau.search.algorithms;

import java.util.Arrays;
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

    public ASTAR(Function<T> costs, Function<T> heuristic) {
        this.cost = costs;
        this.heuristic = heuristic;
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        Set<T> closedset = new HashSet<>();
        Set<T> openset = new HashSet<>(Arrays.asList(start));

        Map<T, T> routes = new HashMap<>();

        Map<T, Double> costMap = new HashMap<>();
        Map<T, Double> fMap = new HashMap<>();
        costMap.put(start, 0.0);
        fMap.put(start, costMap.get(start) + heuristic.value(start));

        while (!openset.isEmpty()) {
            T current = Collections.min(openset, new CostComparator(fMap));
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
                    routes.put(neighbor, current);
                    costMap.put(neighbor, costOfNeighbor);
                    fMap.put(neighbor, costOfNeighbor + heuristic.value(neighbor));

                    if (!openset.contains(neighbor)) {
                        openset.add(neighbor);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private final class CostComparator implements Comparator<T> {
        private Map<T, Double> fMap;

        public CostComparator(Map<T, Double> fMap) {
            this.fMap = Collections.unmodifiableMap(fMap);
        }

        @Override
        public int compare(T o1, T o2) {
            double costO1 = fMap.get(o1);
            double costO2 = fMap.get(o2);
            return costO1 == costO2 ? 0 : costO1 < costO2 ? -1 : 1;
        }
    }

}
