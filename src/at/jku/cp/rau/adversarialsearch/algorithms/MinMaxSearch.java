package at.jku.cp.rau.adversarialsearch.algorithms;

import at.jku.cp.rau.adversarialsearch.AdversarialSearch;
import at.jku.cp.rau.adversarialsearch.predicates.SearchLimitingPredicate;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.utils.Pair;

public class MinMaxSearch<T extends Node<T>> implements AdversarialSearch<T> {
    private static final int START_DEPTH = 0;

    private SearchLimitingPredicate<T> searchLimitingPredicate;
    private Function<T> evalFunction;

    /**
     * To limit the extent of the search, this implementation should honor a
     * limiting predicate
     * 
     * @param searchLimitingPredicate
     */
    public MinMaxSearch(SearchLimitingPredicate<T> searchLimitingPredicate) {
        this.searchLimitingPredicate = searchLimitingPredicate;
    }

    public Pair<T, Double> search(T start, Function<T> evalFunction) {
        this.evalFunction = evalFunction;

        Pair<T, Double> maxValue = null;
        for (T neighbour : start.adjacent()) {
            Double value = min(neighbour, START_DEPTH + 1);

            if (maxValue == null || isGreaterThan(value, maxValue.s)) {
                maxValue = new Pair<>(neighbour, value);
            }
        }
        return maxValue;
    }

    private double max(T node, int depth) {
        if (isSearchLimitPredicateTrue(node, depth)) {
            return evalFunction.value(node);
        }

        Double maxValue = null;
        for (T neighbour : node.adjacent()) {
            Double value = min(neighbour, depth + 1);

            if (isGreaterThan(value, maxValue)) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    private Double min(T node, int depth) {
        if (isSearchLimitPredicateTrue(node, depth)) {
            return evalFunction.value(node);
        }
        Double minValue = null;
        for (T neighbour : node.adjacent()) {
            Double value = max(neighbour, depth + 1);
            if (isSmallerThan(value, minValue)) {
                minValue = value;
            }
        }
        return minValue;
    }

    private boolean isSearchLimitPredicateTrue(T node, int depth) {
        return node.adjacent().isEmpty() || !searchLimitingPredicate.expandFurther(depth, node);
    }

    private boolean isGreaterThan(Double value, Double maxValue) {
        return maxValue == null || (value.compareTo(maxValue) > 0);
    }

    private boolean isSmallerThan(Double value, Double minValue) {
        return minValue == null || (value.compareTo(minValue) < 0);
    }
}
