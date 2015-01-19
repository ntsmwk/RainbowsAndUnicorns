package at.jku.cp.rau.search.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.search.Node;

public class RNode implements Node<RNode>, ContainsBoard, ContainsMove {
    public Move move;
    public IBoard board;
    public Random random;

    public RNode(Random random, IBoard board) {
        this(null, random, board);
    }

    public RNode(Move move, Random random, IBoard board) {
        this.move = move;
        this.random = random;
        this.board = board;
    }

    @Override
    public IBoard getBoard() {
        return board;
    }

    @Override
    public Move getMove() {
        return move;
    }

    @Override
    public RNode current() {
        return this;
    }

    /**
     * This method provides the successor-boards for a given board, in
     * statespace. So, given a board as a point in statespace, it will return
     * all boards reachable from this point. This depends of course, on whose
     * turn it currently is, and what the player can do at this point in
     * gametime.
     * 
     * @param current
     *            the board of which we want to know the adjacent boards in
     *            statespace
     * @return a list of boards adjacent to the current board in statespace
     */
    @Override
    public List<RNode> adjacent() {
        if (!board.isRunning())
            return Collections.emptyList();

        List<RNode> successors = new ArrayList<>();
        List<Move> possible = board.getPossibleMoves();

        Collections.shuffle(possible, random);

        for (Move move : possible) {
            IBoard next = board.copy();
            next.executeMove(move);
            successors.add(new RNode(move, random, next));
        }

        return successors;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((board == null) ? 0 : board.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RNode other = (RNode) obj;
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return board.toString();
    }

    @Override
    public boolean isLeaf() {
        return !board.isRunning();
    }

}
