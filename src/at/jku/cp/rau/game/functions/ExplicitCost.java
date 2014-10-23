package at.jku.cp.rau.game.functions;

import java.util.List;

import at.jku.cp.rau.search.nodes.ContainsPos;

public class ExplicitCost<T extends ContainsPos> implements Function<T> {
    private int[][] costs;

    public ExplicitCost(List<String> _costs) {
        int width = _costs.get(0).length();
        int height = _costs.size();
        costs = new int[width][height];
        int y = 0;
        for (String line : _costs) {
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (Character.isDigit(c))
                    costs[x][y] = Character.digit(c, 10);
                else
                    costs[x][y] = 0;
            }

            y++;
        }
    }

    @Override
    public double value(T node) {
        return costs[node.getPos().x][node.getPos().y];
    }

}
