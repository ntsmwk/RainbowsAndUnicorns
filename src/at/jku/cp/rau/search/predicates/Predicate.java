package at.jku.cp.rau.search.predicates;

public interface Predicate<T> {
    public boolean isTrueFor(T current);
}
