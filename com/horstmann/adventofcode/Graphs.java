package com.horstmann.adventofcode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;

public class Graphs {
    /**
     * Breadth first search
     * @param <V> The type of the graph's nodes
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex 
     * @return a map that maps each reachable vertex to its predecessor      
     */
	public static <V> Map<V, V> bfs(V root, Function<V, Set<V>> neighbors) {
		return bfs(root, neighbors, _ -> {});
	}

    /**
     * Breadth first search with visitor
     * @param <V> The type of the graph's nodes
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex
     * @param visit is applied to each new vertex as it is discovered 
     * @return a map that maps each reachable vertex to its predecessor      
     */
	public static <V> Map<V, V> bfs(V root, Function<V, Set<V>> neighbors, Consumer<V> visit) {
		var parents = new LinkedHashMap<V, V>();
		Set<V> discovered = new HashSet<V>();
		bfs(root, neighbors, (v, p) -> {
		    if (discovered.add(v)) {
                parents.put(v, p);
                visit.accept(v);
                return true;
		    } else 
		        return false;
		});
		return parents;
	}
	
    /**
     * Breadth first search with filter
     * @param <V> The type of the graph's nodes
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex
     * @param filter receives each node and parent (but not the root), and returns true if the node should be visited
     * (Note: If you don't want to revisit already visited nodes, you need to filter them out.)
     */
	public static <V> void bfs(V root, Function<V, Set<V>> neighbors, BiPredicate<V, V> filter) {
        Queue<V> q = new LinkedList<V>();
	    q.add(root);
	    while (!q.isEmpty()) {
	        V p = q.remove();
	        for (V n : neighbors.apply(p)) {
	            if (filter.test(n, p)) {
	                q.add(n);
	            }
	        }
	    }
	}
	
    /**
     * Depth first search
     * @param <V> The type of the graph's nodes
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex 
     * @return a map that maps each reachable vertex to its predecessor      
     */
	public static <V> Map<V, V> dfs(V root, Function<V, Set<V>> neighbors) {
		return dfs(root, neighbors, _ -> {});
	}

    /**
     * Depth first search with visitor
     * @param <V> The type of the graph's nodes
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex
     * @param finished is applied to each vertex after all descendants have been visited
     * @return a map that maps each reachable vertex to its predecessor      
     */
	public static <V> Map<V, V> dfs(V root, Function<V, Set<V>> neighbors, Consumer<V> finished) {
		var parents = new LinkedHashMap<V, V>();
		var discovered = new HashSet<V>();
		discovered.add(root);
		dfs(root, neighbors, (v, p) -> {
		   if (discovered.add(v)) {
		       parents.put(v, p);
		       return true;
		   } else {
		       return false;
		   }
		}, finished);
		return parents;
	}
	
    /**
     * Depth first search with filter and visitor
     * @param <V> The type of the graph's nodes
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex
     * @param filter receives each neighbor node and parent (but not the root), and returns true if the node should be visited
     * @param finished is applied to each vertex after all descendants have been visited
     * (Note: If you don't want to revisit already visited nodes, you need to filter them out.)
     */
	public static <V> void dfs(V root, Function<V, Set<V>> neighbors, BiPredicate<V, V> filter, Consumer<V> finished) {
	    for (V n : neighbors.apply(root)) {
	        if (filter.test(n, root)) {
	            dfs(n, neighbors, filter, finished);
	        }
	    }
        finished.accept(root); 
	}
	
	
    /**
     * Topological sort of a directed graph
     * @param <V> The type of the graph's nodes
     * @param root the starting node for the sort
     * @param neighbors yields the set of neighbors for any vertex
     * @return a map that maps each reachable vertex to its predecessor      
     */
	public static <V> List<V> topologicalSort(V root, Function<V, Set<V>> neighbors) {
		var sorted = new ArrayList<V>();
		dfs(root, neighbors, sorted::add);
		return sorted.reversed();
	}

	// TODO Make Path class?
	private static <V> List<V> append(List<V> vs, V v) {
		List<V> result = new ArrayList<V>(vs);
		result.add(v);
		return result;
	}

	private static <V> List<List<V>> simplePathsHelper(V from, V to, Function<V, Set<V>> neighbors, Set<V> avoid, List<List<V>> prefixes) {
		if (from.equals(to)) {
			return prefixes;
		}
		var result = new ArrayList<List<V>>();
		for (V n : neighbors.apply(from)) {
			if (!avoid.contains(n)) {
				var prefixes2 = prefixes.stream().map(p -> append(p, n)).toList();
				var avoid2 = new HashSet<V>(avoid);
				avoid2.add(n);
				Collection<? extends List<V>> result2 = simplePathsHelper(n, to, neighbors, avoid2, prefixes2);
				result.addAll(result2);
			}
		}
		return result;
	}
	
	// TODO or maybe Set<List<V>>
	// TODO In Digraph, as Set<List<E>>
	public static <V> List<List<V>> simplePaths(V from, V to, Function<V, Set<V>> neighbors) {
		return simplePathsHelper(from, to, neighbors, Set.of(from), List.of(List.of(from)));
	}
	
	
	// Used Map.Entry because of the seductive comparingByValue, but the
	// price to pay is the icky new AbstractMap.SimpleImmutableEntry<>
    // This helper method takes the sting out of it
	static <K, V> Map.Entry<K, V> entry(K k, V v) {
		return new AbstractMap.SimpleImmutableEntry<>(k, v);
	}

	public static <V> Map<V, Integer> dijkstra(V from, Function<V, Set<V>> neighbors, ToIntBiFunction<V, V> neighborDistances) {
		Map<V, Integer> dist = new HashMap<>();
		Set<V> selected = new HashSet<>();
		PriorityQueue<Map.Entry<V, Integer>> q = new PriorityQueue<>(
				Map.Entry.<V, Integer>comparingByValue(Comparator.<Integer>naturalOrder()));
		q.add(entry(from, 0));
		dist.put(from, 0);
		while (q.size() > 0) {
			var e = q.remove();
			var s = e.getKey();
			selected.add(s);
			// For all unselected neighbors
			for (var n: neighbors.apply(s)) {
				if (!selected.contains(n)) {
					int snd = neighborDistances.applyAsInt(s,  n);
					int nd = dist.getOrDefault(n, Integer.MAX_VALUE);
					int sd = dist.getOrDefault(s, Integer.MAX_VALUE);
					if (nd > sd + snd) {
						q.remove(entry(n, nd));
						dist.put(n, sd + snd);
						q.add(entry(n, sd + snd));
					}
				}
			}
		}
		return dist;
	}
	
    public static <V> String dot(V root, Function<V, Set<V>> neighbors, BiFunction<V, V, Object> edgeLabels) {
        var builder = new StringBuilder();
        builder.append("digraph {\n");
        for (var entry : dfs(root, neighbors).entrySet()) {
            var to = entry.getKey();
            var from = entry.getValue();
            var label = edgeLabels.apply(from, to);
            builder.append("   \"%s\" -> \"%s\"".formatted(from, to));
            if (label != null) builder.append("[label=\"%s\"]".formatted(label));
            builder.append("\n");
        }  
        builder.append("}\n");
        return builder.toString();
    }
}


