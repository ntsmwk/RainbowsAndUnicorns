package at.jku.cp.rau.adversarialsearch.predicates;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.V;

import at.jku.cp.rau.search.nodes.ContainsBoard;

public class UnicornsAreClosePredicate<T extends ContainsBoard> extends SoftDepthLimitingPredicate<T> {

    int unicornDistance;

    /**
     * Unicorns are close Predicate
     * 
     * @param softLimit
     *            depth limit for all search branches
     * @param hardLimit
     *            maximum depth limit for search branches when unicorns are
     *            closer then unicornDistance
     * @param unicornDistance
     *            unicorns must stand closer (distance must be SMALLER THAN)
     *            then this parameter to carry on searching
     */
    public UnicornsAreClosePredicate(int softLimit, int hardLimit, int unicornDistance) {
        super(softLimit, hardLimit);
        this.unicornDistance = unicornDistance;
    }

    @Override
    public boolean softLimitExceedingCriteria(int depth, T state) {

        IBoard board = state.getBoard();

        if (!board.isRunning())
            return false;

        V start = board.getUnicorns().get(0).pos;
        V end = board.getUnicorns().get(1).pos;

        return V.manhattan(start, end) < unicornDistance;
    }
}
