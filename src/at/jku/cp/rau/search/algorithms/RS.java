package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

/* Random Search
 * 
 * This search does nothing but selecting a random successor of a node
 * until it eventually finds the goal (or not). This class is meant to 
 * show you how to use some of the methods you need in order to implement
 * real search strategies
 */
public class RS<T extends Node<T>> implements Search<T> {
    @Override
    public List<T> search(T start, Predicate<T> endPredicate) {
        // first we will need some randomness
        Random random = new Random(42L);

        // we'll need to remember the path we took
        List<T> path = new ArrayList<>();

        // we will iterate for as long as we do not find the goal
        T current = start;
        while (true) {
            // we add the current node to the path
            path.add(current);

            if (endPredicate.isTrueFor(current))
                break;

            // we 'expand' the node - we get its directly adjacent neighbors
            // for that, we call the 'adjacent()' method on the current node
            // this is it, actually.
            // you have become acquainted with the API to program against,
            // the rest is up to you...
            List<T> adjacent = current.adjacent();

            // we reached a terminal node, leave the loop
            if (adjacent.size() == 0)
                break;

            // if we actually have neighbors, we'll choose a random one
            // to expand next
            int choice = random.nextInt(adjacent.size());
            current = adjacent.get(choice);
        }

        return path;
    }
}