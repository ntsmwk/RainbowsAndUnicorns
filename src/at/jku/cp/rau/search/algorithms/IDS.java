package at.jku.cp.rau.search.algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.SearchUtils;
import at.jku.cp.rau.search.predicates.Predicate;

// Iterative Deepening Search
public class IDS<T extends Node<T>> implements Search<T> {
    private T start;
    private int limit;
    private Map<T, T> route;

    public IDS(int limit) {
        this.limit = limit;
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        this.start = start;
        this.route = new HashMap<T, T>();
        return searchForCurrent(start, endPredicate, 0);
    }

    private List<T> searchForCurrent(T current, Predicate<T> endPredicate, int currentIndex) {
        if (endPredicate.isTrueFor(current)) {
            return SearchUtils.buildBackPath(current, start, route);
        }

        if (currentIndex < limit) {
            for (T node : current.adjacent()) {
//                route.put(current, node);
                List<T> results = searchForCurrent(node, endPredicate, currentIndex + 1);
                if (!results.isEmpty()) {
                    return results;
                }
            }
        }
        return Collections.emptyList();
    }
}
