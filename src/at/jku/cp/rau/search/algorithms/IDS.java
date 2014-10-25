package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.SearchUtils;
import at.jku.cp.rau.search.predicates.Predicate;

// Iterative Deepening Search
public class IDS<T extends Node<T>> implements Search<T> {
    private int limit;
    private List<T> list = new ArrayList<>();;
    private Map<T, T> routes = new HashMap<T, T>();

    public IDS(int limit) {
        this.limit = limit;
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        T current = searchNode(start, endPredicate, 0);
        return SearchUtils.buildBackPath(current, start, routes);
    }

    private T searchNode(T node, Predicate<T> endPredicate, int index) {
        if (limit <= index) {
            return null;
        }
        if (endPredicate.isTrueFor(node)) {
            return node;
        }
        list.add(node);
        for (T child : node.adjacent()) {
            T result = searchNode(child, endPredicate, index + 1);
            if (result != null) {
                routes.put(child, node);
                return result;
            }
        }
        return null;
    }
}
