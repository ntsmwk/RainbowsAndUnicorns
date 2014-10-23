package at.jku.cp.rau.search.algorithms;

import java.util.Collections;
import java.util.List;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// Iterative Deepening Search
public class IDS<T extends Node<T>> implements Search<T> {
    private int limit;

    public IDS(int limit) {
        this.limit = limit;
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        // TODO: Implement Iterative Deepening Search
        return Collections.emptyList();
    }

}
