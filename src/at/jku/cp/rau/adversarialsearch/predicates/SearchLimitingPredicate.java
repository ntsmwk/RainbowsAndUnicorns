package at.jku.cp.rau.adversarialsearch.predicates;

public interface SearchLimitingPredicate<T> {
    /**
     * A SearchLimitingPredicate is used to limit the search. It is passed the
     * current search depth, as well as a node from state space.
     * 
     * @param depth
     *            the current search depth
     * @param state
     *            the current state
     * @return whether to continue with the search
     */
    boolean expandFurther(int depth, T state);
}
