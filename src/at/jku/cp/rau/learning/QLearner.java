package at.jku.cp.rau.learning;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.utils.Pair;

public class QLearner {
    /**
     * The qmatrix is (very) sparse, that is why this is a hashmap.
     */
    private Map<Pair<IBoard, Move>, Double> qmatrix;
    private int numEpisodes;

    /**
     * If you use a randomness source, please use this one. it's passed in in
     * the unit-tests...
     */
    private Random random;
    private double discountFactor;

    public QLearner() {
        this(20001, 0.9);
    }

    public QLearner(int numEpisodes, double discountFactor) {
        this(new Random(), numEpisodes, discountFactor);
    }

    /**
     * This QLearner has no learning rate, because in a deterministic
     * environment, such as the Rainbows and Unicorns game, a learning rate of
     * 1.0 is optimal.
     * 
     * @param random
     *            the random number generator to be used
     * @param numEpisodes
     *            the number of episodes for learning the model
     * @param discountFactor
     *            this determines the importance of future rewards; in our test
     *            cases this plays a very minor role.
     */
    public QLearner(Random random, int numEpisodes, double discountFactor) {
        if (discountFactor < 0d) {
            throw new RuntimeException("discountFactor must be greater than 0.0!");
        }

        this.random = random;
        this.numEpisodes = numEpisodes;
        this.discountFactor = discountFactor;
        this.qmatrix = new HashMap<>();
    }

    /**
     * Starting with a given board, learn a model for behaviour
     * 
     * @param board
     *            the board given
     */
    public void learnQFunction(IBoard board) {
        // TODO: Implement this!
        // HINT(1): Adapt algorithm for q-learning from the lecture slides
        // in the following way:
        //
        // for e in 0 .. number_of_episodes:
        // do until goal reached:
        // ...

        // HINT(2): For storing the q-matrix, please use the provided hashmap.
        // This is going to save you a lot of trouble!
    }

    /**
     * Based on the learned model, this function shall return the best move for
     * a given board.
     * 
     * @param board
     *            the board given
     * @return a move
     */
    public Move getMove(IBoard board) {
        // TODO: Implement this!
        // The code for selecting the best move goes here.
        return Move.STAY;
    }
}