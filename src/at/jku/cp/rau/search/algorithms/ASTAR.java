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

        // g_score[start] := 0 // Cost from start along best known path.
        // Estimated total cost from start to goal through y.
        // f_score[start] := g_score[start] + heuristic_cost_estimate(start,
        // goal)

        while (!openset.isEmpty()) {

            T current = Collections.min(openset, new CostComparator());
            if (endPredicate.isTrueFor(current)) {
                return SearchUtils.buildBackPath(current, start, routes);
            }
            openset.remove(current);
            closedset.add(current);
            for (T neighbor : current.adjacent()) {
                if (closedset.contains(neighbor)) {
                    continue;
                }
                // tentative_g_score := g_score[current] +
                // dist_between(current,neighbor)

                if (!openset.contains(neighbor)) { // or tentative_g_score <
                                                   // g_score[neighbor]
                    routes.put(neighbor, current);
                    // g_score[neighbor] := tentative_g_score
                    // f_score[neighbor] := g_score[neighbor] +
                    // heuristic_cost_estimate(neighbor, goal)
                    openset.add(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }

    private final class CostComparator implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            double value = cost.value(o1) + heuristic.value(o1) - cost.value(o2) - heuristic.value(o2);
            return value == 0 ? 0 : value < 0 ? -1 : 1;
        }
    }

}
