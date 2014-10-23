package at.jku.cp.rau.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SearchUtils {
    private SearchUtils() {

    }

    public static <S> List<S> buildBackPath(S current, S start, Map<S, S> routes) {
        List<S> path = new ArrayList<>();
        while (current != start) {
            path.add(current);
            current = routes.get(current);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }
}
