package at.jku.cp.rau.tests.assignment2;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import at.jku.cp.rau.adversarialsearch.algorithms.AlphaBetaSearch;
import at.jku.cp.rau.adversarialsearch.predicates.HardDepthLimitingPredicate;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.nodes.BNode;

@RunWith(Parameterized.class)
public class TestExercise2AlphaBeta extends AdversarialSearchersBase {

    @Parameters
    public static Collection<Object[]> generateParams() {

        List<Object[]> params = (List<Object[]>) TestExercise2MinMax.generateParams();

        // MinMax takes too long

        params.add(new Object[] { Arrays.asList("####", "#p###", "#m.#", "#.##", "#p##", "#m##", "#####"),
                Arrays.asList(Move.DOWN, Move.DOWN, Move.SPAWN, Move.UP, Move.RIGHT),
                Arrays.asList(Move.DOWN, Move.STAY, Move.STAY, Move.STAY), 14 });

        params.add(new Object[] {
                Arrays.asList("#####", "#p.######", "#.c....m#", "#.#######", "#cm##", "#p###", "#####"),
                Arrays.asList(Move.RIGHT, Move.SPAWN, Move.LEFT, Move.DOWN, Move.RIGHT, Move.RIGHT, Move.RIGHT,
                        Move.RIGHT, Move.RIGHT, Move.RIGHT, Move.STAY),
                Arrays.asList(Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY,
                        Move.STAY, Move.STAY, Move.STAY), 20 });

        return params;
    }

    public TestExercise2AlphaBeta(List<String> level, List<Move> expectedMoves, List<Move> opponentMoves, Integer depth) {
        super(level, expectedMoves, opponentMoves, depth);
    }

    private class AlphaBetaSearchDepthLimited<T extends Node<T>> extends AlphaBetaSearch<T> {
        public AlphaBetaSearchDepthLimited(int maxDepth) {
            super(new HardDepthLimitingPredicate<T>(maxDepth));
        }
    }

    @Test
    public void testAlphaBetaMAX() {
        testGameSearchMAX(new AlphaBetaSearchDepthLimited<BNode>(depth));
    }
}
