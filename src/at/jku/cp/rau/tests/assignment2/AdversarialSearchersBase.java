package at.jku.cp.rau.tests.assignment2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import at.jku.cp.rau.adversarialsearch.AdversarialSearch;
import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.endconditions.PointCollecting;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.search.nodes.BNode;
import at.jku.cp.rau.utils.Pair;

public abstract class AdversarialSearchersBase {
    protected IBoard startBoard;
    protected List<Move> expectedMoves;
    protected List<Move> opponentMoves;
    protected Integer depth;

    public AdversarialSearchersBase(List<String> level, List<Move> expectedMoves, List<Move> opponentMoves,
            Integer depth) {
        this.startBoard = Board.fromLevelRepresentation(level);
        this.expectedMoves = expectedMoves;
        this.opponentMoves = opponentMoves;
        this.depth = depth;
    }

    static Function<BNode> evalFunction1 = new Function<BNode>() {

        @Override
        public double value(BNode t) {

            PointCollecting pce = ((PointCollecting) t.board.getEndCondition());
            int winner = pce.getWinner();
            int points = (pce.getScore(0) - pce.getScore(1));
            if (winner != -1) {
                if (winner == 0)
                    points += (10000 - t.board.getTick());
                else if (winner == 1)
                    points += -(10000 - t.board.getTick());
            }
            return points;
        }
    };

    protected void testGameSearchMAX(AdversarialSearch<BNode> searcher) {
        IBoard board = startBoard.copy();
        board.setEndCondition(new PointCollecting());

        for (int i = 0; i < expectedMoves.size(); i++) {
            Pair<BNode, Double> nb = searcher.search(new BNode(board), evalFunction1);

            assertTrue("Move " + i + " was null!", nb != null && nb.f != null);
            assertTrue("Move " + i + " could not be executed!", board.executeMove(nb.f.move));

            assertEquals("Move " + i + " was wrong!", expectedMoves.get(i), nb.f.move);

            if (i < opponentMoves.size())
                assertTrue("Move of Opponent could not be executed!", board.executeMove(opponentMoves.get(i)));
            else
                assertFalse("Game should be over now", board.isRunning());
        }
    }
}
