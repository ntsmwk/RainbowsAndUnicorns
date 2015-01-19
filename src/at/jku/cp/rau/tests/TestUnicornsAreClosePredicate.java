package at.jku.cp.rau.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import at.jku.cp.rau.adversarialsearch.predicates.UnicornsAreClosePredicate;
import at.jku.cp.rau.game.BitBoard;
import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.search.nodes.BNode;

public class TestUnicornsAreClosePredicate {

    static IBoard b0 = new BitBoard(Board.fromLevelRepresentation(Arrays.asList("##########", "#pp......#",
            "##########")));

    static IBoard b1 = new BitBoard(Board.fromLevelRepresentation(Arrays.asList("##########", "#p..p....#",
            "##########")));

    static IBoard b2 = new BitBoard(Board.fromLevelRepresentation(Arrays.asList("##########", "#p...p...#",
            "##########")));

    static IBoard b3 = new BitBoard(Board.fromLevelRepresentation(Arrays.asList("##########", "#p.....p.#",
            "##########")));

    @Test
    public void test1() {

        UnicornsAreClosePredicate<BNode> pred = new UnicornsAreClosePredicate<>(2, 4, 4);

        assertTrue(pred.expandFurther(0, new BNode(b0)));
        assertTrue(pred.expandFurther(1, new BNode(b0)));
        assertTrue(pred.expandFurther(2, new BNode(b0)));
        assertTrue(pred.expandFurther(3, new BNode(b0)));
        assertFalse(pred.expandFurther(4, new BNode(b0)));

        assertTrue(pred.expandFurther(0, new BNode(b1)));
        assertTrue(pred.expandFurther(1, new BNode(b1)));
        assertTrue(pred.expandFurther(2, new BNode(b1)));
        assertTrue(pred.expandFurther(3, new BNode(b1)));
        assertFalse(pred.expandFurther(4, new BNode(b1)));

        assertTrue(pred.expandFurther(0, new BNode(b2)));
        assertTrue(pred.expandFurther(1, new BNode(b2)));
        assertFalse(pred.expandFurther(2, new BNode(b2)));
        assertFalse(pred.expandFurther(3, new BNode(b2)));
        assertFalse(pred.expandFurther(4, new BNode(b2)));

        assertTrue(pred.expandFurther(0, new BNode(b3)));
        assertTrue(pred.expandFurther(1, new BNode(b3)));
        assertFalse(pred.expandFurther(2, new BNode(b3)));
        assertFalse(pred.expandFurther(3, new BNode(b3)));
        assertFalse(pred.expandFurther(4, new BNode(b3)));

    }

    @Test
    public void test2() {

        UnicornsAreClosePredicate<BNode> pred = new UnicornsAreClosePredicate<>(2, 6, 6);

        assertTrue(pred.expandFurther(0, new BNode(b0)));
        assertTrue(pred.expandFurther(1, new BNode(b0)));
        assertTrue(pred.expandFurther(2, new BNode(b0)));
        assertTrue(pred.expandFurther(3, new BNode(b0)));
        assertTrue(pred.expandFurther(4, new BNode(b0)));
        assertTrue(pred.expandFurther(5, new BNode(b0)));
        assertFalse(pred.expandFurther(6, new BNode(b0)));

        assertTrue(pred.expandFurther(0, new BNode(b1)));
        assertTrue(pred.expandFurther(1, new BNode(b1)));
        assertTrue(pred.expandFurther(2, new BNode(b1)));
        assertTrue(pred.expandFurther(3, new BNode(b1)));
        assertTrue(pred.expandFurther(4, new BNode(b1)));
        assertTrue(pred.expandFurther(5, new BNode(b1)));
        assertFalse(pred.expandFurther(6, new BNode(b1)));

        assertTrue(pred.expandFurther(0, new BNode(b2)));
        assertTrue(pred.expandFurther(1, new BNode(b2)));
        assertTrue(pred.expandFurther(2, new BNode(b2)));
        assertTrue(pred.expandFurther(3, new BNode(b2)));
        assertTrue(pred.expandFurther(4, new BNode(b2)));
        assertTrue(pred.expandFurther(5, new BNode(b2)));
        assertFalse(pred.expandFurther(6, new BNode(b2)));

        assertTrue(pred.expandFurther(0, new BNode(b3)));
        assertTrue(pred.expandFurther(1, new BNode(b3)));
        assertFalse(pred.expandFurther(2, new BNode(b3)));
        assertFalse(pred.expandFurther(3, new BNode(b3)));
        assertFalse(pred.expandFurther(4, new BNode(b3)));
        assertFalse(pred.expandFurther(5, new BNode(b3)));
        assertFalse(pred.expandFurther(6, new BNode(b3)));

    }

}
