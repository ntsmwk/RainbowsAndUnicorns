package at.jku.cp.rau.learning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.V;
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
	private boolean spawn = false;
	private boolean finished = false;
	private double backOffMoveCost = 101.0;

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
		int currEpisodes = 0;
		while (currEpisodes < numEpisodes) {

			IBoard newBoard = board.copy();
			// Use Random Position to also get a solution for bigger boards
			// Used for bonus-maps
			board.getCurrentUnicorn().pos = calculateRandomPosition(newBoard);

			startNewGame(newBoard);
			currEpisodes++;
		}
	}

	/**
	 * Calculates a random starting Position on the board, that is passable
	 * 
	 * @param board
	 * @return
	 */
	public V calculateRandomPosition(IBoard board) {
		V position = new V(random.nextInt(board.getWidth()), random.nextInt(board.getHeight()));
		while (!board.isPassable(position)) {
			position = new V(random.nextInt(board.getWidth()), random.nextInt(board.getHeight()));
		}
		return position;
	}

	/**
	 * Starts a new learning episode
	 * 
	 * @param board
	 */
	private void startNewGame(IBoard board) {
		ArrayList<Pair<IBoard, Move>> listToSpawn = new ArrayList<>();
		int unicornId = board.getCurrentUnicorn().id;

		IBoard lastBoard;
		for (;;) {
			Move move = chooseMove(board);
			lastBoard = board.copy();
			listToSpawn.add(new Pair<IBoard, Move>(lastBoard.copy(), move));

			board.executeMove(move);
			if (containsBoardState(board.copy(), move)) {
				double reward = getCorrespondingReward(board.copy());
				if (!(move == Move.SPAWN && reward == 100.0)) {
					qmatrix.put(new Pair<IBoard, Move>(lastBoard.copy(), move), reward * discountFactor);
				}
				return;
			}

			// This case is only accessible when the EndCondition is the first time accomplished
			// In this case, we want to know all backOff- Moves to survive
			if (!board.isRunning()) {
				if (board.getEndCondition().getWinner() == unicornId && !finished) {
					finished = true;
					// Get Spawn Move!
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 8), 100.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 7), 101.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 6), 102.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 5), 103.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 4), 104.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 3), 105.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 2), 106.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 1), 107.0);
				}
				return;
			}

		}
	}

	/**
	 * Returns a random move, except Spawn and Stay
	 * 
	 * @param board
	 * @return
	 */
	private Move chooseMove(IBoard board) {
		return board.getPossibleMoves().get(random.nextInt(board.getPossibleMoves().size()));
	}

	/**
	 * Searches in the HashMap if it contains a specific board
	 * 
	 * @param board
	 * @param qmatrix
	 * @return true if board is in the HashMap
	 */
	private boolean containsBoardState(IBoard board, Move move) {
		for (Entry<Pair<IBoard, Move>, Double> entry : qmatrix.entrySet()) {
			if (isSameBoard(board, entry) && entry.getValue() <= 100.0) {
				double reward = getCorrespondingReward(board.copy());
				if (qmatrix.containsKey(new Pair<IBoard, Move>(board, move))
						&& entry.getValue() > reward * discountFactor) {
					return false;
				}
				return true;
			}
		}
		return false;

	}

	/**
	 * get the maximum reward of all actions accessible from the current state
	 * 
	 * @param board
	 * @return
	 */
	private double getCorrespondingReward(IBoard board) {
		double reward = 0.0;
		for (Entry<Pair<IBoard, Move>, Double> entry : qmatrix.entrySet()) {
			if (isSameBoard(board, entry) && reward < entry.getValue() && entry.getValue() <= 100.0) {
				reward = entry.getValue();
			}
		}
		return reward;
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
		Entry<Pair<IBoard, Move>, Double> bestEntry = null;
		double costOfMove = 0.0;
		for (Entry<Pair<IBoard, Move>, Double> entry : qmatrix.entrySet()) {
			double cost = entry.getValue();
			//Normal Moves
			if (isSameBoard(board, entry) && costOfMove < cost && cost <= 100.0 && !spawn) {
				costOfMove = cost;
				bestEntry = entry;
			}
			// Back off Moves.
			if (isSameBoard(board, entry) && cost == backOffMoveCost && spawn) {
				backOffMoveCost++;
				return entry.getKey().s;
			}
		}
		// Enable Back Off Moves after Spawning
		if (bestEntry.getKey().s == Move.SPAWN) {
			spawn = true;
		}
		qmatrix.remove(bestEntry);
		return bestEntry.getKey().s;
	}

	/**
	 * Checks if the unicornPositions of two boards are identical
	 * 
	 * @param board
	 * @param entry
	 * @return
	 */
	private boolean isSameBoard(IBoard board, Entry<Pair<IBoard, Move>, Double> entry) {
		try {
			return entry.getKey().f.getCurrentUnicorn().pos.equals(board.getCurrentUnicorn().pos);
		} catch (ArithmeticException e) {
			return false;
		}

	}
}
