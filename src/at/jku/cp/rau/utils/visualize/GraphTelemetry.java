package at.jku.cp.rau.utils.visualize;

import java.util.ArrayList;
import java.util.List;

import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.utils.Pair;

public class GraphTelemetry<T extends Node<T>> implements Node<GraphTelemetry<T>>
{
	public Graph<T> graph;
	public T content;

	public GraphTelemetry(Graph<T> graph, Node<T> wrapped)
	{
		this.content = wrapped.current();
		this.graph = graph;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphTelemetry<?> other = (GraphTelemetry<?>) obj;
		if (content == null)
		{
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		return true;
	}

	@Override
	public GraphTelemetry<T> current()
	{
		return this;
	}
	
	@Override
	public List<GraphTelemetry<T>> adjacent()
	{
		graph.addVertex(content);
		List<T> adjacent = content.adjacent();
		List<GraphTelemetry<T>> wrappedAdjacent = new ArrayList<>();
		
		for (T next : adjacent)
		{
			graph.addVertex(next);
			graph.addEdge(new Pair<>(content, next));
			wrappedAdjacent.add(new GraphTelemetry<>(graph, next));
		}

		return wrappedAdjacent;
	}
	
	@Override
	public String toString()
	{
		return content.toString();
	}

}
