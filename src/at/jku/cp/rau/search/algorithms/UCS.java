package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.SearchUtils;
import at.jku.cp.rau.search.datastructures.StablePriorityQueue;
import at.jku.cp.rau.search.predicates.Predicate;
import at.jku.cp.rau.utils.Pair;

// Uniform Cost Search
public class UCS<T extends Node<T>> implements Search<T> {
    private Function<T> cost;
    private Map<T, T> route;
    private StablePriorityQueue<Double, T> limit;
    private T start;
    private Pair<Double, T> pair;
    private List<T> list;

    public UCS(Function<T> costs) {
        this.cost = costs;
        this.route = new HashMap<T, T>();
        limit = new StablePriorityQueue<Double, T>();
        list = new ArrayList<T>();
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        this.start = start;
        pair = new Pair<Double, T>(cost.value(start), start);

        return uniformCostSearch(endPredicate);
    }

    private List<T> uniformCostSearch(Predicate<T> endPredicate) {
        limit.add(pair);
        while (!limit.isEmpty()) {
            Pair<Double, T> pair2 = limit.poll();
            if (endPredicate.isTrueFor(pair2.s)) {
                return SearchUtils.buildBackPath(pair2.s, start, route);
            }
            list.add(pair2.s);
            for (T element : pair2.s.adjacent()) {
                if (!list.contains(element) && !limit.contains(element)) {
                    limit.add(new Pair<Double, T>(pair2.f + cost.value(element), element));
                    list.add(element);
                    addRoute(element, pair2.s);
                }
            }
        }
        return Collections.emptyList();
    }

    private void addRoute(T current, T next) {
        route.put(current, next);
    }
}
