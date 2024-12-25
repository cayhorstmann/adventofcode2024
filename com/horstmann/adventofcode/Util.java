package com.horstmann.adventofcode;

import java.io.IO;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {
    public static String reverse(String s) { return new StringBuilder(s).reverse().toString(); }

    public static <T> T middle1(List<T> oddSizeList) { return oddSizeList.get(oddSizeList.size() / 2); }
    public static <T> List<T> middle2(List<T> evenSizeList) { 
        int m = evenSizeList.size() / 2;
        return List.of(evenSizeList.get(m), evenSizeList.get(m + 1)); 
    }
    public static <T> List<T> allButFirst(List<T> lst) { return lst.subList(1, lst.size()); }
    public static <T> List<T> allButLast(List<T> lst) { return lst.subList(0, lst.size() - 1); }
    
    public static <T> Comparator<T> comparatorFromOrder(BiPredicate<T, T> order) {
        return (x, y) -> x.equals(y) ? 0 : order.test(x,  y) ? -1 : 1;        
    }

    public static List<Integer> parseIntegers(String line) { return parseIntegers(line, "[^0-9+-]"); }

    public static List<Integer> parseIntegers(String line, String separatorRegex) { 
        return Stream.of(line.split(separatorRegex)).filter(l -> l.length() > 0).map(Integer::parseInt).toList();
    }

    public static List<Long> parseLongs(String line) { return parseLongs(line, "[^0-9+-]"); }
    
    public static List<Long> parseLongs(String line, String separatorRegex) { 
        return Stream.of(line.split(separatorRegex)).filter(l -> l.length() > 0).map(Long::parseLong).toList();
    }
    
    // TODO parse empty-line separated blocks
        
    public static Path inputPath(String suffix) { 
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        String day = walker.walk(s -> s.filter(f -> f.getMethodName().equals("main")).findFirst()).get().getClassName().replaceAll("\\D", "");
        return Path.of("inputs/input" + day + suffix); 
    }
    
    public static Path outputPath(String suffix) { 
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        String day = walker.walk(s -> s.filter(f -> f.getMethodName().equals("main")).findFirst()).get().getClassName().replaceAll("\\D", "");
        return Path.of("outputs/output" + day + suffix); 
    }
    /*
     * TODO: all pairs? all distinct pairs? All combinations? (day 5, day 8)
     */
    
    public static boolean logging = true;
    public static int margin = 100;

    public static void log(Object... objects) {
        if (logging)
            IO.println(Stream.of(objects).map(Util::format).collect(Collectors.joining(" ")));
    }
    
    public static String format(Object object) {
        var s = object == null ? "null" : object.toString();
        return switch(object) {
            case Collection<?> values ->
                s.length() <= margin ? s 
                        : "[\n " + values.stream().map(Util::format).collect(Collectors.joining(",\n ")) + "]";                                
            case Map<?, ?> values ->
                s.length() <= margin ? s 
                    : "{\n " + values.entrySet().stream().map(Util::format).collect(Collectors.joining(",\n ")) + "}";                                
            case Map.Entry<?, ?> e -> 
               s.length() <= margin ? s 
                    : Util.format(e.getKey()) + "=\n" + Util.format(e.getValue());
            case null -> "null";
            default -> s;
        };
    }

    private static HashMap<List<?>, Object> memo = new HashMap<>();
    public static void resetMemo() { memo = new HashMap<>(); }
    
    /**
     * @param <A> The argument type
     * @param <R> The return type
     * @param memo a map that you must allocate for holding the memoized results
     * @param s the lambda with the code of the function, e.g.
     * long fib(long n) { return Util.memoize(() -> n <= 1 ? 1 : fib(n - 1) + fib(n - 2), n); }
     * Just write the function without memoization, then add 
     *     return Util.memoize(() -> { ... }, arg1, arg2, ...)
     * @param args the arguments
     * CAUTION: If you memoize more than one function, add a unique key as the initial arg
     * CAUTION: If the memoized code captures mutable state, reset the memo when the state changes 
     * @return the result of s
     */
    public static <R> R memoize(Supplier<R> s, Object...args) {
        List<?> a = List.of(args);
        @SuppressWarnings("unchecked") R r = (R) memo.get(a);
        if (r == null) {
            r = s.get();    
            memo.put(a, r);
        }
        return r;
    }
    
    public interface Task<E extends Throwable> {
        void run() throws E;
    }
    
    public static <E extends Throwable> void time(Task<E> t) throws E {
        long start = System.nanoTime();
        t.run();
        IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
    }
    
    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            var temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    public static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }
    
    public static long pow2(int n) {
        long r = 1;
        for (int i = 1; i <= n; i++) r <<= 1;
        return r;
    }
    
    public static int sign(long n) {
        return n == 0 ? 0 : n > 0 ? 1 : -1;
    }
    
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
}
