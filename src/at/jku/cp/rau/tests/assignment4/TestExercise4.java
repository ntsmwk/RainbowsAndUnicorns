package at.jku.cp.rau.tests.assignment4;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.endconditions.CloudEvaporater;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.learning.QLearner;

public class TestExercise4 {
    private boolean verbose = true;

    @Test
    public void simplest() {
        doLearningAndTestOnBoard("simplest.lvl", verbose);
    }

    @Test
    public void simple() {
        doLearningAndTestOnBoard("simple.lvl", verbose);
    }

    @Test
    public void length() {
        doLearningAndTestOnBoard("length.lvl", verbose);
    }

    @Test
    public void L0() {
        doLearningAndTestOnBoard("L0.lvl", verbose);
    }

    @Test
    public void L1() {
        doLearningAndTestOnBoard("L1.lvl", verbose);
    }

    @Test
    public void L2() {
        doLearningAndTestOnBoard("L2.lvl", verbose);
    }

    // ////////////////////////////////////////////////////////////
    // these are some bonus levels - your QLearning implementation
    // may not be able to solve these with only 20.000 learning
    // episodes ...
    // ... the point here is to adapt the QLearner in such a way
    // that it can learn a suitable model with only 20.000 episodes!
    //

    @Test
    public void bonus0() {
        doLearningAndTestOnBoard("bonus0.lvl", verbose);
    }

    @Test
    public void bonus1() {
        doLearningAndTestOnBoard("bonus1.lvl", verbose);
    }

    @Test
    public void bonus2() {
        doLearningAndTestOnBoard("bonus2.lvl", verbose);
    }

    private void doLearningAndTestOnBoard(String filename, boolean verbose) {
        IBoard board = Board.fromLevelFile("assets/assignment4/" + filename);

        if (verbose)
            System.out.println(board);

        // we restrict the number of seeds the unicorn can spawn, to make the
        // statespace considerably smaller!
        board.getUnicorns().get(0).seeds = 1;

        // we need a new end-condition for the game as well
        board.setEndCondition(new CloudEvaporater());

        // /////////////////////////////////////////////////
        // 20,000 episodes is enough to learn a successful model for
        // all but the bonus excercises

        // for the bonus exercises, you will have to adapt the QLearner
        // implementation, such that it is able to learn a proper model
        // with 20,000 episodes.
        QLearner learner = new QLearner(new Random(42L), 20001, 0.9);

        // learning the qmatrix happens here
        learner.learnQFunction(board.copy());

        // apply the learned model to the original board
        // if we go sailing, or need more than 100 ticks before
        // we reach the goal, this test will fail
        while (board.isRunning() && board.getTick() < 100) {
            // determine the next move based upon the current board
            // state and the learned model
            Move nextMove = learner.getMove(board.copy());
            board.executeMove(nextMove);

            if (verbose)
                System.out.println(board);
        }

        if (verbose)
            System.out.println(board);

        // the learned model should have guided the unicorn to the cloud
        // and the cloud should have been evaporated as well.
        assertEquals(0, board.getEndCondition().getWinner());
    }

}
