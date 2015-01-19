package at.jku.cp.rau.learning;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.endconditions.EndCondition;
import at.jku.cp.rau.game.objects.Cloud;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.Unicorn;
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
        EndCondition endCondition = board.getEndCondition();
        List<Unicorn> unicorns = board.getUnicorns();
        List<Cloud> clouds = board.getClouds();
        // TODO: Implement this!
        // HINT(1): Adapt algorithm for q-learning from the lecture slides
        // in the following way:
        //
        // for e in 0 .. number_of_episodes:
        // do until goal reached:
        // ...

        for (int i = 0; i < numEpisodes; i++) {
            if (endCondition.hasEnded(board, clouds, unicorns)){
                break;
            }
//            random.nextInt()
        }

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
        Move move = null;
        double costOfMove = 0.0;
        for (Pair<IBoard, Move> pair : qmatrix.keySet()) {
            double cost = qmatrix.get(pair).doubleValue();
            if (isSameBoard(board, pair) && costOfMove < cost) {
                costOfMove = cost;
                move = pair.s;
            }
        }

        return move;
    }

    private boolean isSameBoard(IBoard board, Pair<IBoard, Move> pair) {
        return pair.f.equals(board);
    }
}