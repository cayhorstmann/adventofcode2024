package com.horstmann.adventofcode;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
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
    
    public static List<Integer> parseIntegers(String line, String separatorRegex) { 
        return Stream.of(line.split(separatorRegex)).map(Integer::parseInt).toList();
    }
    
    public static List<Long> parseLongs(String line, String separatorRegex) { 
        return Stream.of(line.split(separatorRegex)).map(Long::parseLong).toList();
    }
        
    public static Path inputPath(String suffix) { 
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        String day = walker.walk(s -> s.filter(f -> f.getMethodName().equals("main")).findFirst()).get().getClassName().replaceAll("\\D", "");
        return Path.of("inputs/input" + day + suffix); 
    }
    
    /*
     * Todo: middle? (day 5)
     * all pairs? all distinct pairs? All combinations? (day 5, day 8)
     * make comparator from order relation (day 5)
     * tail of list, all but last of list (day 7)
     * 
     */
}
