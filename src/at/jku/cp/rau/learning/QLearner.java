package at.jku.cp.rau.learning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

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
	private int spawn = 0;

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
		
		//TODO add runnaway from rainbow..........

		int currEpisodes = startNewGame(board.copy(), qmatrix, 0, numEpisodes);
		while (currEpisodes < numEpisodes) {
			// set unicorn to random place etc.
			currEpisodes = startNewGame(board.copy(), qmatrix, currEpisodes, numEpisodes);
		}

		System.out.println("end");

		// HINT(2): For storing the q-matrix, please use the provided hashmap.
		// This is going to save you a lot of trouble!
	}

	private int startNewGame(IBoard board, Map<Pair<IBoard, Move>, Double> qmatrix, int startingPoint,
			int numEpisodes) {
		ArrayList<Pair<IBoard, Move>> listToSpawn = new ArrayList<>();
		int unicornId = board.getCurrentUnicorn().id;
		double reward = 0.0;
		// board .. Board of current round (after the Move is executed)
		// lastBoard .. Board of last round (before the Move is executed)
		IBoard lastBoard;
		for (int i = startingPoint; i < numEpisodes; i++) {
			Move move = chooseMove(board);
			lastBoard = board.copy();
			System.out.println(move);
			listToSpawn.add(new Pair<IBoard, Move>(lastBoard.copy(), move));

			board.executeMove(move);

			if (containsBoardState(board.copy(), qmatrix)) {
				reward = getCorrespondingReward(board.copy(), qmatrix);
				reward = reward * this.discountFactor;
				qmatrix.put(new Pair<IBoard, Move>(lastBoard.copy(), move), reward);
				return i;
			}

			// Only for last move to goal
			if (!board.isRunning()) {
				if (board.getEndCondition().getWinner() == unicornId) {
					// Get Spawn Move!
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 8), 100.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 7), 101.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 6), 101.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 5), 101.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 4), 101.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 3), 101.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 2), 101.0);
					qmatrix.put(listToSpawn.get(listToSpawn.size() - 1), 101.0);
					//qmatrix.put(listToSpawn.get(listToSpawn.size() - 0), 101.0);
				}
				return i;
			}

		}
		return numEpisodes;
	}

	private void setUnicorn(IBoard board, short x, short y) {
		// TODO set to random place
		board.getCurrentUnicorn().pos.x = x;
		board.getCurrentUnicorn().pos.y = y;
	}

	/**
	 * Returns a random move, except Spawn and Stay
	 * 
	 * @param board
	 * @return
	 */
	private Move chooseMove(IBoard board) {
		Move m = board.getPossibleMoves().get(random.nextInt(board.getPossibleMoves().size()));
		return m;
	}

	/**
	 * Searches in the HashMap if it contains a specific board
	 * 
	 * @param board
	 * @param qmatrix
	 * @return true if board is in the HashMap
	 */
	private boolean containsBoardState(IBoard board, Map<Pair<IBoard, Move>, Double> qmatrix) {
		for (Entry<Pair<IBoard, Move>, Double> entry : qmatrix.entrySet()) {
			if (entry.getKey().f.equals(board)) {
				return true;
			}
		}
		return false;

	}

	private double getCorrespondingReward(IBoard board, Map<Pair<IBoard, Move>, Double> qmatrix) {
		double reward = 0.0;
		for (Entry<Pair<IBoard, Move>, Double> entry : qmatrix.entrySet()) {
			if (entry.getKey().f.equals(board)) {
				if (reward < entry.getValue()) {
					reward = entry.getValue();
				}
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
		//int spawn = 0;
		Move move = null;
		double costOfMove = 0.0;
		for (Pair<IBoard, Move> pair : qmatrix.keySet()) {
			double cost = qmatrix.get(pair).doubleValue();
			if (isSameBoard(board, pair) && costOfMove < cost && cost <= 100.0) {
				//Enable Back Off Moves
				if(pair.s == Move.SPAWN){
					spawn = 7;
				}
				costOfMove = cost;
				move = pair.s;
			}
			//Back off Moves....
			if(isSameBoard(board, pair) && cost > 100.0 && spawn > 0){
				spawn--;
				return pair.s;
			}
		}

		return move;
	}

	private boolean isSameBoard(IBoard board, Pair<IBoard, Move> pair) {
		return pair.f.equals(board);
	}
}
