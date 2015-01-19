package at.jku.cp.rau.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import at.jku.cp.rau.game.functions.RandomCost;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.nodes.ContainsPos;

public class TestCosts {
    @Test
    public void range() {
        // make sure range stays between [1, ub)
        for (int r = 0; r < 1000; r++) {
            int N = 50;
            int ub = 10;
            RandomCost<ContainsPos> rc = new RandomCost<>(N, N, r, ub);

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    V pos = new V(i, j);
                    assertTrue(rc.value(pos) > 0);
                    assertTrue(rc.value(pos) < ub);
                }
            }
        }
    }
}
