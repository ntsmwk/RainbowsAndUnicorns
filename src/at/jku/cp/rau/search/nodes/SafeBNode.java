package at.jku.cp.rau.search.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.search.Node;

public class SafeBNode implements Node<SafeBNode>, ContainsBoard, ContainsMove {

    public Move move;
    public IBoard board;

    public SafeBNode(IBoard board) {
        this(null, board);
    }

    public SafeBNode(Move move, IBoard board) {
        this.move = move;
        this.board = board;
    }

    @Override
    public SafeBNode current() {
        return this;
    }

    @Override
    public IBoard getBoard() {
        return board;
    }

    @Override
    public Move getMove() {
        return move;
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
    public List<SafeBNode> adjacent() {
        if (!board.isRunning())
            return Collections.emptyList();

        List<SafeBNode> successors = new ArrayList<>();
        List<Move> possible = board.getPossibleMoves();

        possible.remove(Move.SPAWN);

        for (Move move : possible) {
            IBoard next = board.copy();
            next.executeMove(move);
            successors.add(new SafeBNode(move, next));
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
        SafeBNode other = (SafeBNode) obj;
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

}
