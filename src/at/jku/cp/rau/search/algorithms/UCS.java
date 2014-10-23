package at.jku.cp.rau.search.algorithms;

import java.util.Collections;
import java.util.List;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// Uniform Cost Search
public class UCS<T extends Node<T>> implements Search<T> {
    private Function<T> cost;

    public UCS(Function<T> costs) {
        this.cost = costs;
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        // TODO: Implement Unified Costs Search
        return Collections.emptyList();
    }
}
