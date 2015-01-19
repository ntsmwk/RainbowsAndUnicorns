package at.jku.cp.rau.game.functions;

import java.util.Random;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Path;
import at.jku.cp.rau.search.nodes.ContainsPos;
import at.jku.cp.rau.utils.RenderUtils;

public class RandomCost<T extends ContainsPos> implements Function<T> {
    private int[][] costs;

    public RandomCost(int width, int height, long seed, int upperBound) {
        Random random = new Random(seed);
        costs = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                costs[x][y] = 1 + random.nextInt(upperBound - 1);
            }
        }
    }

    @Override
    public double value(ContainsPos node) {
        return costs[node.getPos().x][node.getPos().y];
    }

    public String render(IBoard board) {
        char[][] rep = board.getTextBoard();
        for (Path p : board.getPaths())
            rep[p.pos.x][p.pos.y] = Character.forDigit(costs[p.pos.x][p.pos.y], 10);
        return RenderUtils.asStringNoInfo(rep);
    }
}
