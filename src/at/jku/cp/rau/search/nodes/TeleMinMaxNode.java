package at.jku.cp.rau.search.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.utils.Pair;
import at.jku.cp.rau.utils.visualize.Graph;

public class TeleMinMaxNode implements Node<TeleMinMaxNode>, ContainsBoard, ContainsMove, ContainsGraph<TeleMinMaxNode> {
    private Graph<TeleMinMaxNode> graph;
    private IBoard board;
    private Move move;
    private Double score;

    public TeleMinMaxNode(Graph<TeleMinMaxNode> graph, IBoard board) {
        this.graph = graph;
        this.board = board;
        this.score = 0d;
    }

    public TeleMinMaxNode(Graph<TeleMinMaxNode> graph, Move move, IBoard board) {
        this.graph = graph;
        this.move = move;
        this.board = board;
        this.score = 0d;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Double getScore() {
        return score;
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
    public TeleMinMaxNode current() {
        return this;
    }

    @Override
    public boolean isLeaf() {
        return !board.isRunning();
    }

    @Override
    public List<TeleMinMaxNode> adjacent() {
        if (isLeaf())
            return Collections.emptyList();

        List<TeleMinMaxNode> successors = new ArrayList<>();
        List<Move> possible = board.getPossibleMoves();

        graph.addVertex(this);
        for (Move move : possible) {
            IBoard next = board.copy();
            next.executeMove(move);
            TeleMinMaxNode nextTmm = new TeleMinMaxNode(graph, move, next);
            successors.add(nextTmm);
            graph.addVertex(nextTmm);
            graph.addEdge(new Pair<>(this, nextTmm));
        }
        return successors;
    }

    @Override
    public Graph<TeleMinMaxNode> getGraph() {
        return graph;
    }

}
