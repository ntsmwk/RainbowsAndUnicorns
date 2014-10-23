package at.jku.cp.rau.search.algorithms;

import java.util.Collections;
import java.util.List;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// Depth-Limited Depth-First Search
public class DLDFS<T extends Node<T>> implements Search<T> {
    private int limit;

    public DLDFS(int limit) {
        this.limit = limit;
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        // TODO: implement Depth-Limited Depth-First Search
        return Collections.emptyList();
    }
}