package at.jku.cp.rau.tests.assignment2;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import at.jku.cp.rau.adversarialsearch.algorithms.AlphaBetaSearch;
import at.jku.cp.rau.adversarialsearch.algorithms.MinMaxSearch;
import at.jku.cp.rau.adversarialsearch.predicates.SearchLimitingPredicate;
import at.jku.cp.rau.adversarialsearch.predicates.UnicornsAreClosePredicate;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.search.nodes.BNode;

@RunWith(Parameterized.class)
public class TestExercise2MinMaxUnicornPredicate extends AdversarialSearchersBase {

    @Parameters
    public static Collection<Object[]> generateParams() {
        return TestExercise2MinMax.generateParams();
    }

    public TestExercise2MinMaxUnicornPredicate(List<String> level, List<Move> expectedMoves, List<Move> opponentMoves,
            Integer depth) {
        super(level, expectedMoves, opponentMoves, depth);
    }

    @Test
    public void testMinMaxMAX() {
        testGameSearchMAX(new MinMaxSearch<BNode>(getPredicate()));
    }

    @Test
    public void testAlphaBetaMAX() {
        testGameSearchMAX(new AlphaBetaSearch<BNode>(getPredicate()));
    }

    private SearchLimitingPredicate<BNode> getPredicate() {
        return new UnicornsAreClosePredicate<BNode>(depth - 4, depth, 8);
    }
}
