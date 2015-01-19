package at.jku.cp.rau.utils.visualize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.jku.cp.rau.utils.Pair;

public class Graph<T> {
    private Set<T> vertices;
    private Map<T, Set<T>> _edges;
    private List<Pair<T, T>> edges;
    private Map<Pair<T, T>, Integer> weights;
    private Map<T, Double> vertexWeights;

    public Graph() {
        vertices = new HashSet<>();
        edges = new ArrayList<>();
        _edges = new HashMap<>();
        weights = new HashMap<>();
        vertexWeights = new HashMap<>();
    }

    public void addVertex(T vertex) {
        vertices.add(vertex);
    }

    public void addVertexWeight(T vertex, double weight) {
        vertexWeights.put(vertex, weight);
    }

    public void addEdge(Pair<T, T> edge) {
        edges.add(edge);

        if (_edges.containsKey(edge.f)) {
            _edges.get(edge.f).add(edge.s);
        } else {
            Set<T> set = new HashSet<>();
            set.add(edge.s);
            _edges.put(edge.f, set);
        }

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

    public Double getVertexWeight(T vertex) {
        return vertexWeights.get(vertex);
    }

    public Double getVertexWeight(T vertex, Double def) {
        return (vertexWeights.get(vertex) == null) ? def : vertexWeights.get(vertex);
    }

    public Set<T> getWeightedVertices() {
        return vertexWeights.keySet();
    }

    public Set<Pair<T, T>> getWeightedEdges() {
        return weights.keySet();
    }

    public Set<T> getStarts(T vertex) {
        Set<T> starts = new HashSet<>();
        for (Pair<T, T> edge : edges) {
            starts.add(edge.f);
        }
        return starts;
    }

    public Set<T> getEnds(T vertex) {
        Set<T> ends = new HashSet<>();
        for (Pair<T, T> edge : edges) {
            ends.add(edge.s);
        }
        return ends;
    }

    public Set<T> getSiblings(T vertex) {
        Set<T> siblings = new HashSet<>();
        for (T start : getStarts(vertex))
            siblings.addAll(getEnds(start));

        return siblings;
    }

    public Set<T> getLeaves() {
        Set<T> starts = new HashSet<>();
        Set<T> ends = new HashSet<>();
        for (Pair<T, T> edge : edges) {
            starts.add(edge.f);
            ends.add(edge.s);
        }

        ends.removeAll(starts);
        return ends;
    }

    public T getRoot() {
        Set<T> starts = new HashSet<>();
        Set<T> ends = new HashSet<>();
        for (Pair<T, T> edge : edges) {
            starts.add(edge.f);
            ends.add(edge.s);
        }

        starts.removeAll(ends);

        if (starts.size() != 1)
            throw new RuntimeException("lol");
        return starts.iterator().next();
    }

    public Set<T> getChildren(T vertex) {
        return (null == _edges.get(vertex)) ? Collections.<T> emptySet() : _edges.get(vertex);
    }
}
