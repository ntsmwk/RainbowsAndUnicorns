package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.SearchUtils;
import at.jku.cp.rau.search.datastructures.StablePriorityQueue;
import at.jku.cp.rau.search.predicates.Predicate;
import at.jku.cp.rau.utils.Pair;

// Iterative Deepening Search
public class IDS<T extends Node<T>> implements Search<T> {
    private int limit;
    private List<T> list;
    private Map<T, T> routes;
    private Map<T, List<T>> routes2;
    private T start;
    StablePriorityQueue<Integer, Pair<Integer, T>> spq;
    
    public IDS(int limit) {
    	this.routes = new HashMap<T, T>();
    	this.routes2 = new HashMap<T, List<T>>();
    	this.list = new ArrayList<>();
        this.limit = limit;
        this.spq = new StablePriorityQueue<Integer, Pair<Integer, T>>();
        
    }

    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
    	this.start=start;
    	list.add(start);
       
        return iterativeDeepeningSearch( endPredicate);
    }
/*
    private List<T> iterativeDeepeningSearch(T node, Predicate<T> endPredicate, int index) {
        if (limit <= index) {
            return null;
        }
        if (endPredicate.isTrueFor(node)) {
            return node;
        }
        list.add(node);
        for (T child : node.adjacent()) {
            T result = searchNode(child, endPredicate, index + 1);
            if (result != null) {
                routes.put(child, node);
                return result;
            }
        }
        return null;
    }
    */
    private List<T> iterativeDeepeningSearch(Predicate<T> endPredicate) {
    	int i = 0;
    	Pair<Integer, T> sPair = new Pair<>(i,start);
    	Pair<Integer, Pair<Integer, T>> node = new Pair<>(i,sPair);
    	Pair<Integer, Pair<Integer, T>> qNode;// = new Pair<>(i,start);
    	spq.add(node);
    	//List<T> resultList;
    	while(!spq.isEmpty()){
    		node = spq.remove();
    		if(node.f > limit){
    			return Collections.EMPTY_LIST;
    		}
			if (endPredicate.isTrueFor(node.s.s)) {
				//end founded
				return SearchUtils.buildBackPath(node.s.s, start, routes);
			}
			for (T element : node.s.s.adjacent()) {
				if (!list.contains(element)) {
					list.add(element);
					addRoute(element, node.s.s);
					qNode = new Pair<>(new Integer(++node.f), new Pair<Integer, T>(new Integer(0), element));
					spq.add(qNode);
				}
			}
    	}
		return Collections.EMPTY_LIST;
    
    }
	private void addRoute(T current, T next) {
		routes.put(current, next);
	}
}
