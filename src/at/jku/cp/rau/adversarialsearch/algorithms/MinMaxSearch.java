package at.jku.cp.rau.adversarialsearch.algorithms;

import at.jku.cp.rau.adversarialsearch.AdversarialSearch;
import at.jku.cp.rau.adversarialsearch.predicates.SearchLimitingPredicate;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.utils.Pair;

public class MinMaxSearch<T extends Node<T>> implements AdversarialSearch<T> {

    /**
     * To limit the extent of the search, this implementation should honor a
     * limiting predicate
     * 
     * @param slp
     */
    public MinMaxSearch(SearchLimitingPredicate<T> slp) {
    }

    public Pair<T, Double> search(T start, Function<T> evalFunction) {
        /**
         * TODO: Implement MinMax Search.
         */

        return new Pair<>(null, 0.0);
    }
}
