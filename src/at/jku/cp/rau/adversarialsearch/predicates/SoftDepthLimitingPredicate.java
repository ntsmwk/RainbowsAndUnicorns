package at.jku.cp.rau.adversarialsearch.predicates;

/**
 * This predicate has a 'soft' limit that may stop search before the 'hard'
 * limit is reached
 * 
 * @param <T>
 */
public abstract class SoftDepthLimitingPredicate<T> implements SearchLimitingPredicate<T> {

    public final int softLimit;
    public final int hardLimit;

    public SoftDepthLimitingPredicate(int softLimit, int hardLimit) {
        this.softLimit = softLimit;
        this.hardLimit = hardLimit;
    }

    @Override
    public final boolean expandFurther(int depth, T state) {
        return depth < softLimit || (depth < hardLimit && softLimitExceedingCriteria(depth, state));
    }

    public abstract boolean softLimitExceedingCriteria(int depth, T state);

}
