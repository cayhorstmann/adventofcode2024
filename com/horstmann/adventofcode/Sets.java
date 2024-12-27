package com.horstmann.adventofcode;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Sets {

    public static <T> Set<T> union(Collection<? extends T> a, Collection<? extends T> b) {
        var result = new HashSet<T>();
        result.addAll(a);
        result.addAll(b);
        return result;
    }

    public static <T> Set<T> intersection(Collection<? extends T> a, Collection<? extends T> b) {
        var result = new HashSet<T>();
        result.addAll(a);
        result.retainAll(b);
        return result;
    }

    public static <T> Set<T> difference(Collection<? extends T> a, Collection<? extends T> b) {
        var result = new HashSet<T>();
        result.addAll(a);
        result.removeAll(b);
        return result;
    }

    /*
     * TODO: all pairs? all distinct pairs? All combinations? (day 5, day 8)
     */
    
    public static <S, T, U> Set<U> bimap(Set<S> lefts, Set<T> rights, BiFunction<S, T, U> f) {
        return lefts.stream().flatMap(s -> rights.stream().map(t -> f.apply(s, t))).collect(Collectors.toSet());
    }

    public static <S> Set<S> reduceAll(List<Set<S>> operands, BinaryOperator<S> operator) {
        if (operands.isEmpty()) throw new IllegalArgumentException("Missing operands");
        if (operands.size() == 1) return operands.getFirst();
        else return bimap(operands.getFirst(), reduceAll(Lists.withoutFirst(operands), operator), operator);
    }            
}
