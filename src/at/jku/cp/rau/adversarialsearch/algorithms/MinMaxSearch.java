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

        Pair<T, Double> maxValue = new Pair<T, Double>(null, 0.0);
        for (T neighbour : start.adjacent()) {
            double value = min(neighbour, START_DEPTH + 1);

            if (maxValue.f == null || isGreaterThan(value, maxValue.s)) {
                maxValue.s = value;
                maxValue.f = neighbour;
            }
        }
        return maxValue;
    }

    private double max(T node, int depth) {
        if (isSearchLimitPredicateTrue(node, depth)) {
            return evaluateValue(node);
        }

        double maxValue = Double.MIN_VALUE;
        for (T neighbour : node.adjacent()) {
            double value = min(neighbour, depth + 1);

            if (isGreaterThan(value, maxValue)) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    private double min(T node, int depth) {
        if (isSearchLimitPredicateTrue(node, depth)) {
            return evaluateValue(node);
        }

        double minValue = Double.MAX_VALUE;
        for (T neighbour : node.adjacent()) {
            double value = max(neighbour, depth + 1);

            if (isSmallerThan(value, minValue)) {
                minValue = value;
            }
        }
        return minValue;
    }

    private double evaluateValue(T node) {
        return evalFunction.value(node);
    }

    private boolean isSearchLimitPredicateTrue(T node, int depth) {
        return node.adjacent().isEmpty() || !searchLimitingPredicate.expandFurther(depth, node);
    }

    private boolean isGreaterThan(double value, double maxValue) {
        return value > maxValue;
    }

    private boolean isSmallerThan(double value, double minValue) {
        return value < minValue;
    }
}
