package at.jku.cp.rau.search.nodes;

import java.util.ArrayList;
import java.util.List;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.Node;

public class IgnoreCloudsNode implements Node<IgnoreCloudsNode>, ContainsPos, ContainsMove {
    private Move move;
    private V pos;
    private IBoard board;

    public IgnoreCloudsNode(IBoard board, Move move, V pos) {
        this.board = board;
        this.move = move;
        this.pos = pos;
    }

    public IgnoreCloudsNode(IBoard board, V pos) {
        this(board, null, pos);
    }

    @Override
    public Move getMove() {
        return move;
    }

    @Override
    public V getPos() {
        return pos;
    }

    @Override
    public IgnoreCloudsNode current() {
        return this;
    }

    @Override
    public List<IgnoreCloudsNode> adjacent() {
        List<IgnoreCloudsNode> adjacent = new ArrayList<>();

        for (V d : Board.getDirections()) {
            V next = V.add(pos, d);
            // System.out.println("board:"+board);
            if (board.isPassable(next) || board.isRemovable(next)) {
                adjacent.add(new IgnoreCloudsNode(board, Board.getDirectionToMoveMapping().get(d), next));
            }
        }

        return adjacent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
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
        IgnoreCloudsNode other = (IgnoreCloudsNode) obj;
        if (pos == null) {
            if (other.pos != null)
                return false;
        } else if (!pos.equals(other.pos))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return pos.toString();
    }

    @Override
    public boolean isLeaf() {
        return adjacent().isEmpty();
    }
}
