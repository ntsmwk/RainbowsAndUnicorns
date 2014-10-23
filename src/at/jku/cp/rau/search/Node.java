package at.jku.cp.rau.search;

import java.util.List;

public interface Node<T>
{
	public T current();
	public List<T> adjacent();
}
