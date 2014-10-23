package at.jku.cp.rau.game.functions;

import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.search.nodes.ContainsPos;

public class EuclideanDistance<T extends ContainsPos> implements Function<T> {
    V pos;

    public EuclideanDistance(V pos) {
        this.pos = pos;
    }

    @Override
    public double value(T current) {
        return V.euclidean(pos, current.getPos());
    }

}
