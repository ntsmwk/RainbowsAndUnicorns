package at.jku.cp.rau.adversarialsearch;

import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.utils.Pair;

public interface AdversarialSearch<T extends Node<T>> {
    /**
     * This method takes a node from the state space and an evalFunction. The
     * evalFunction should get called when the search terminates. Each leaf of
     * the search-tree should therefore have a value from this evalFunction
     * associated with it.
     * 
     * The function returns a pair, namely next state on the path to the most
     * beneficial state, given the current state and the evalFunction.
     * Additionally we'll return the value of the evalFunction that has led to
     * this decision.
     * 
     * @param start
     *            the starting node in state space
     * @param evalFunction
     *            the eval function that scores a leaf
     * @return Pair<T, Double> a pair (bestMove, score)
     */
    Pair<T, Double> search(T start, Function<T> evalFunction);
}
