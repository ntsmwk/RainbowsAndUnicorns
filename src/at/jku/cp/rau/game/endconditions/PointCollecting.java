package at.jku.cp.rau.game.endconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Cloud;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.utils.Pair;

final public class PointCollecting implements EndCondition {
    private enum Outcome {
        KOWIN, KODRAW, SCORE
    };

    private int winner;
    private Outcome outcome;
    private Map<Integer, Integer> scores;

    public PointCollecting() {
        winner = -1;
        outcome = Outcome.SCORE;
        scores = new HashMap<>();
    }

    public PointCollecting(PointCollecting pointCollecting) {
        this.winner = pointCollecting.winner;
        this.outcome = pointCollecting.outcome;
        this.scores = new HashMap<>(pointCollecting.scores);
    }

    @Override
    public boolean hasEnded(IBoard board, List<Cloud> evaporated, List<Unicorn> sailing) {
        if (board.getUnicorns().size() == 0) {
            outcome = Outcome.KODRAW;
            winner = -1;
            return true;
        }

        if (board.getUnicorns().size() == 1 && sailing.size() > 0) {
            outcome = Outcome.KOWIN;
            winner = board.getUnicorns().get(0).id;
            return true;
        }

        for (Marker m : board.getMarkers()) {
            if (m.lastVisitedBy != Marker.LAST_VISITED_BY_DEFAULT) {
                if (!scores.containsKey(m.lastVisitedBy)) {
                    scores.put(m.lastVisitedBy, 0);
                }

                scores.put(m.lastVisitedBy, scores.get(m.lastVisitedBy) + 1);
            }
        }

        return false;
    }

    public int getScore(int unicorn_id) {
        if (scores.containsKey(unicorn_id)) {
            return scores.get(unicorn_id);
        } else {
            return 0;
        }
    }

    @Override
    public int getWinner() {
        if (outcome == Outcome.SCORE) {
            if (scores.size() == 1) {
                return scores.entrySet().iterator().next().getKey();
            }

            List<Pair<Integer, Integer>> sorted = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : scores.entrySet()) {
                int unicorn_id = entry.getKey();
                int score = entry.getValue();

                sorted.add(new Pair<>(unicorn_id, score));
            }

            Collections.sort(sorted, new Comparator<Pair<Integer, Integer>>() {
                @Override
                public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
                    return -1 * Integer.compare(o1.s, o2.s);
                }
            });

            if (sorted.size() == 0 || sorted.get(0).s == sorted.get(1).s) {
                return -1;
            } else {
                return sorted.get(0).f;
            }
        } else {
            // Outcome.KODRAW
            // Outcome.KOWIN
            return winner;
        }
    }

    @Override
    public EndCondition copy() {
        return (EndCondition) new PointCollecting(this);
    }
}
