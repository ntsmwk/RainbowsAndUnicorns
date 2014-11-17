package at.jku.cp.rau.tests.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import at.jku.cp.rau.adversarialsearch.algorithms.MinMaxSearch;
import at.jku.cp.rau.adversarialsearch.predicates.HardDepthLimitingPredicate;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.nodes.BNode;

@RunWith(Parameterized.class)
public class TestExercise2MinMax extends AdversarialSearchersBase {

    @Parameters
    public static Collection<Object[]> generateParams() {

        List<Object[]> params = new ArrayList<Object[]>();

        params.add(new Object[] { Arrays.asList("#######", "#p.m#p#", "#######"),
                Arrays.asList(Move.RIGHT, Move.RIGHT, Move.STAY), Arrays.asList(Move.STAY, Move.STAY, Move.STAY), 6 });
        params.add(new Object[] { Arrays.asList("##########", "#m.p..m#p#", "##########"),
                Arrays.asList(Move.LEFT, Move.LEFT, Move.STAY), Arrays.asList(Move.STAY, Move.STAY, Move.STAY), 6 });
        params.add(new Object[] { Arrays.asList("#########", "#p..m..p#", "#########"),
                Arrays.asList(Move.RIGHT, Move.RIGHT, Move.RIGHT), Arrays.asList(Move.LEFT, Move.LEFT, Move.LEFT), 10 });
        params.add(new Object[] { Arrays.asList("####", "#pm#", "#.##", "#.#", "#p#", "###"),
                Arrays.asList(Move.DOWN, Move.SPAWN, Move.UP, Move.RIGHT),
                Arrays.asList(Move.STAY, Move.STAY, Move.STAY), 10 });

        params.add(new Object[] { Arrays.asList("####", "#pm#", "#.##", "#.#", "#p#", "###"),
                Arrays.asList(Move.RIGHT), Arrays.asList(Move.STAY), 2 });

        params.add(new Object[] { Arrays.asList("###", "#p#", "#m.#", "#.#", "#p#", "#m#", "###"),
                Arrays.asList(Move.DOWN, Move.STAY, Move.STAY), Arrays.asList(Move.DOWN, Move.STAY, Move.STAY), 6 });

        params.add(new Object[] {
                Arrays.asList("####", "#p.#", "#.##", "#c#", "#m#", "###", "#p#", "###"),
                Arrays.asList(Move.DOWN, Move.SPAWN, Move.UP, Move.RIGHT, Move.LEFT, Move.DOWN, Move.DOWN, Move.DOWN,
                        Move.STAY),
                Arrays.asList(Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY,
                        Move.STAY), 15 });

        params.add(new Object[] { Arrays.asList("#####", "#p.##", "#.cm#", "#c###", "#p###", "#####"),
                Arrays.asList(Move.RIGHT, Move.SPAWN, Move.LEFT, Move.DOWN, Move.RIGHT, Move.RIGHT, Move.STAY),
                Arrays.asList(Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY, Move.STAY), 14 });

        params.add(new Object[] { Arrays.asList("####", "#..#", "#pm#", "#.##", "#p##", "####"),
                Arrays.asList(Move.RIGHT, Move.STAY), Arrays.asList(Move.UP, Move.UP), 8 });

        params.add(new Object[] { Arrays.asList("######", "#..###", "##p.m#", "##.###", "##p###", "######"),
                Arrays.asList(Move.RIGHT, Move.RIGHT, Move.STAY, Move.STAY, Move.STAY),
                Arrays.asList(Move.UP, Move.UP, Move.SPAWN, Move.UP, Move.LEFT), 6 });

        params.add(new Object[] { Arrays.asList("######", "#..###", "##p.m#", "##.###", "##p###", "######"),
                Arrays.asList(Move.SPAWN, Move.UP, Move.LEFT), Arrays.asList(Move.UP, Move.STAY), 10 });

        params.add(new Object[] { Arrays.asList("####", "#..#", "#pm#", "#.##", "#p##", "####"),
                Arrays.asList(Move.SPAWN, Move.RIGHT, Move.UP), Arrays.asList(Move.UP, Move.STAY), 10 });

        return params;
    }

    public TestExercise2MinMax(List<String> level, List<Move> expectedMoves, List<Move> opponentMoves, Integer depth) {
        super(level, expectedMoves, opponentMoves, depth);
    }

    public class MinMaxSearchDepthLimited<T extends Node<T>> extends MinMaxSearch<T> {
        public MinMaxSearchDepthLimited(int maxDepth) {
            super(new HardDepthLimitingPredicate<T>(maxDepth));
        }
    }

    @Test
    public void testMinMaxMAX() {
        testGameSearchMAX(new MinMaxSearchDepthLimited<BNode>(depth));
    }

}
