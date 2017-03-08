package scott.spelling.model;

import java.util.*;

public class Utils {
    @SafeVarargs
    public static <T> List<T> asList(T... items) {
        List<T> list = new ArrayList<>();
        list.addAll(Arrays.asList(items));
        return list;
    }
    public static boolean isNullOrEmpty(HashMap map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean isNullOrEmpty(List<T> items) {
        return items == null || items.isEmpty();
    }

    public static <T> T lastInList(List<T> items) {
        if (isNullOrEmpty(items)) { return null; }
        return items.get(items.size() - 1);
    }
}
