package at.jku.cp.rau.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.jku.cp.rau.game.endconditions.EndCondition;
import at.jku.cp.rau.game.objects.Cloud;
import at.jku.cp.rau.game.objects.GameObject;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.Path;
import at.jku.cp.rau.game.objects.Rainbow;
import at.jku.cp.rau.game.objects.Seed;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.game.objects.Wall;

public class BoardWithHistory implements Serializable, IBoard {
    private static final long serialVersionUID = 1L;

    private IBoard board;
    private List<Move> moves;

    public BoardWithHistory(IBoard board) {
        this.board = board;
        this.moves = new ArrayList<>();
    }

    public BoardWithHistory(IBoard board, List<Move> moves) {
        this.board = board;
        this.moves = new ArrayList<>();
        for (Move move : moves)
            this.moves.add(move);
    }

    public List<Move> getHistory() {
        return moves;
    }

    @Override
    public IBoard copy() {
        return new BoardWithHistory(board.copy(), moves);
    }

    @Override
    public IBoard deepCopy() {
        return new BoardWithHistory(board.deepCopy(), moves);
    }

    @Override
    public List<GameObject> at(V pos) {
        return board.at(pos);
    }

    @Override
    public boolean isStoppingRainbow(V pos) {
        return board.isStoppingRainbow(pos);
    }

    @Override
    public boolean isRemovable(V pos) {
        return board.isRemovable(pos);
    }

    @Override
    public boolean isRainbowAt(V pos) {
        return board.isRainbowAt(pos);
    }

    @Override
    public boolean isPassable(V pos) {
        return board.isPassable(pos);
    }

    @Override
    public List<Move> getPossibleMoves() {
        return board.getPossibleMoves();
    }

    @Override
    public boolean executeMove(Move move) {
        if (board.executeMove(move)) {
            moves.add(move);
            return true;
        } else {
            // don't record ...
            return false;
        }
    }

    @Override
    public String toString() {
        return board.toString();
    }

    @Override
    public char[][] getTextBoard() {
        return board.getTextBoard();
    }

    @Override
    public Unicorn getCurrentUnicorn() {
        return board.getCurrentUnicorn();
    }

    @Override
    public int getTick() {
        return board.getTick();
    }

    @Override
    public int getWidth() {
        return board.getWidth();
    }

    @Override
    public int getHeight() {
        return board.getHeight();
    }

    @Override
    public boolean isRunning() {
        return board.isRunning();
    }

    @Override
    public EndCondition getEndCondition() {
        return board.getEndCondition();
    }

    @Override
    public void setEndCondition(EndCondition endCondition) {
        board.setEndCondition(endCondition);
    }

    @Override
    public List<Wall> getWalls() {
        return board.getWalls();
    }

    @Override
    public List<Path> getPaths() {
        return board.getPaths();
    }

    @Override
    public List<Marker> getMarkers() {
        return board.getMarkers();
    }

    @Override
    public List<Cloud> getClouds() {
        return board.getClouds();
    }

    @Override
    public List<Unicorn> getUnicorns() {
        return board.getUnicorns();
    }

    @Override
    public List<Seed> getSeeds() {
        return board.getSeeds();
    }

    @Override
    public List<Rainbow> getRainbows() {
        return board.getRainbows();
    }

    @Override
    public List<List<? extends GameObject>> getAllObjects() {
        return board.getAllObjects();
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
        BoardWithHistory other = (BoardWithHistory) obj;
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        return true;
    }

}
