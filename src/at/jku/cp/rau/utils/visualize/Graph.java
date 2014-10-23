package at.jku.cp.rau.utils.visualize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.utils.Pair;

public class Graph<T extends Node<T>> {
    private Set<T> vertices;
    private List<Pair<T, T>> edges;
    private Map<Pair<T, T>, Integer> weights;

    public Graph() {
        vertices = new HashSet<>();
        edges = new ArrayList<>();
        weights = new HashMap<>();
    }

    public void addVertex(T vertex) {
        vertices.add(vertex);
    }

    public void addEdge(Pair<T, T> edge) {
        edges.add(edge);

        if (!weights.containsKey(edge))
            weights.put(edge, 0);

        weights.put(edge, weights.get(edge) + 1);
    }

    public Set<T> getVertices() {
        return vertices;
    }

    public List<Pair<T, T>> getEdges() {
        return edges;
    }

    public int getWeight(Pair<T, T> edge) {
        return weights.get(edge);
    }

    public Set<Pair<T, T>> getWeightedEdges() {
        return weights.keySet();
    }
}
