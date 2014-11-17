package at.jku.cp.rau.adversarialsearch.predicates;

/**
 * This predicate just cuts off after a certain depth is reached.
 * 
 * @param <T>
 */
public class HardDepthLimitingPredicate<T> implements SearchLimitingPredicate<T> {

    public final int maxDepth;

    public HardDepthLimitingPredicate(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean expandFurther(int depth, T state) {
        return depth < maxDepth;
    }

}
