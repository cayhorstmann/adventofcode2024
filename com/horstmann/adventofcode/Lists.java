package com.horstmann.adventofcode;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    public static <T> T middle1(List<T> oddSizeList) { return oddSizeList.get(oddSizeList.size() / 2); }

    public static <T> List<T> middle2(List<T> evenSizeList) { 
        int m = evenSizeList.size() / 2;
        return List.of(evenSizeList.get(m), evenSizeList.get(m + 1)); 
    }

    public static <T> List<T> withoutFirst(List<T> lst) { return lst.subList(1, lst.size()); }

    public static <T> List<T> withoutLast(List<T> lst) { return lst.subList(0, lst.size() - 1); }

    public static <V> List<V> append(List<V> vs, V v) {
        List<V> result = new ArrayList<V>(vs);
        result.add(v);
        return result;
    }

    public static <V> List<V> prepend(V v, List<V> vs) {
        List<V> result = new ArrayList<V>();
        result.add(v);
        result.addAll(vs);
        return result;
    }
}
