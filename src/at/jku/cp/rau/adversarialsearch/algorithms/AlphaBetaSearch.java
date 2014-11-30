package at.jku.cp.rau.adversarialsearch.algorithms;

import at.jku.cp.rau.adversarialsearch.AdversarialSearch;
import at.jku.cp.rau.adversarialsearch.predicates.SearchLimitingPredicate;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.utils.Pair;

public class AlphaBetaSearch<T extends Node<T>> implements AdversarialSearch<T> {
    private static final int START_DEPTH = 0;

    private SearchLimitingPredicate<T> searchLimitingPredicate;
    private Function<T> evalFunction;
    private T current;

    /**
     * To limit the extent of the search, this implementation should honor a
     * limiting predicate
     * 
     * @param searchLimitingPredicate
     */
    public AlphaBetaSearch(SearchLimitingPredicate<T> searchLimitingPredicate) {
        this.searchLimitingPredicate = searchLimitingPredicate;
    }

    public Pair<T, Double> search(T start, Function<T> evalFunction) {
        this.evalFunction = evalFunction;
        double heuristic = max(start, 0, Double.MIN_VALUE, Double.MAX_VALUE);
        return new Pair<T, Double>(current, heuristic);
    }

    private double max(T node, int depth, double alpha, double beta) {
        if (!searchLimitingPredicate.expandFurther(depth, node)) {
            return evalFunction.value(node);
        }
        double maxValue = alpha;
        for (T neighbour : node.adjacent()) {

            double value = min(neighbour, depth + 1, maxValue, beta);
            if (value > maxValue) {
                maxValue = value;
                if (maxValue >= beta) {
                }
                if (depth == START_DEPTH) {
                    current = neighbour;
                }
            }
        }
        return maxValue;
    }

    private double min(T node, int depth, double alpha, double beta) {
        if (!searchLimitingPredicate.expandFurther(depth, node)) {
            return evalFunction.value(node);
        }
        double minValue = beta;
        for (T neighbour : node.adjacent()) {

            double value = max(neighbour, depth + 1, alpha, minValue);
            if (value < minValue) {
                minValue = value;
                if (minValue <= alpha) {
                    break;
                }
            }
        }
        return minValue;
    }
}
