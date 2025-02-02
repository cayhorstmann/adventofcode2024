package com.horstmann.adventofcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SequencedMap;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;

// TODO Bidirectional BFS https://zdimension.fr/everyone-gets-bidirectional-bfs-wrong/
// TODO Clique finding https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm

public class Graphs {
    /**
     * Breadth first search
     * @param <V> The type of the graph's vertices
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex 
     * @return a map that maps each reachable vertex to its predecessor      
     */
    public static <V> SequencedMap<V, V> bfs(V root, Function<V, Set<V>> neighbors) {
        return bfs(root, neighbors, _ -> {});
    }

    /**
     * Breadth first search with visitor
     * @param <V> The type of the graph's vertices
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex
     * @param visit is applied to each new vertex as it is discovered 
     * @return a map that maps each reachable vertex to its predecessor. The root is mapped to null.      
     */
    public static <V> SequencedMap<V, V> bfs(V root, Function<V, Set<V>> neighbors, Consumer<V> visit) {
        var parents = new LinkedHashMap<V, V>();
        Set<V> discovered = new HashSet<V>();
        discovered.add(root);
        parents.put(root, null);
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
     * @param <V> The type of the graph's vertices
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
     * The path to a given vertex
     * @param <V> The type of the graph's vertices
     * @param predecessors a map from vertices to their predecessors from a bfs or dfs
     * @param end the vertex to reach
     * @return a path that starts with the root of the search and ends at the given vertex
     */
    public static <V> List<V> path(Map<V, V> predecessors, V end) {
        var p = new ArrayList<V>();
        p.add(end);
        var pred = predecessors.get(end); 
        while (pred != null) {
            p.add(pred);
            pred = predecessors.get(pred);
        }
        return p.reversed();
    }

    /**
     * All paths from the root of a DAG
     * @param <V> The type of the graph's vertices
     * @param root the starting node
     * @param neighbors yields the set of neighbors for any vertex 
     * @return a set of all maximum length paths      
     */
    public static <V> Set<List<V>> dagPaths(V root, Function<V, Set<V>> neighbors) {
        var ns = neighbors.apply(root);
        if (ns.isEmpty()) return Set.of(List.of(root));
        var result = new HashSet<List<V>>();
        for (var n : ns) {
            for(var p : dagPaths(n, neighbors))
                result.add(Lists.prepend(root, p));
       }   
       return result;
   }

    
    /**
     * Depth first search
     * @param <V> The type of the graph's vertices
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex 
     * @return a map that maps each reachable vertex to its predecessor. The root has predecessor null.      
     */
    public static <V> SequencedMap<V, V> dfs(V root, Function<V, Set<V>> neighbors) {
        return dfs(root, neighbors, _ -> {});
    }

    /**
     * Depth first search with visitor
     * @param <V> The type of the graph's vertices
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex
     * @param finished is applied to each vertex after all descendants have been visited
     * @return a map that maps each reachable vertex to its predecessor. The root has parent null.
     */
    private static <V> SequencedMap<V, V> dfs(V root, Function<V, Set<V>> neighbors, Consumer<V> finished) {
        var parents = new LinkedHashMap<V, V>();
        var discovered = new HashSet<V>();
        discovered.add(root);
        parents.put(root, null);
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
     * @param <V> The type of the graph's vertices
     * @param root the starting node for the search
     * @param neighbors yields the set of neighbors for any vertex
     * @param filter receives each neighbor node and parent (but not the root), and returns true if the node should be visited
     * @param finished is applied to each vertex after all descendants have been visited
     * (Note: If you don't want to revisit already visited nodes, you need to filter them out.)
     */
    private static <V> void dfs(V root, Function<V, Set<V>> neighbors, BiPredicate<V, V> filter, Consumer<V> finished) {
        for (V n : neighbors.apply(root)) {
            if (filter.test(n, root)) {
                dfs(n, neighbors, filter, finished);
            }
        }
        finished.accept(root); 
    }
    
    /**
     * Topological sort of a directed graph.
     * @param <V> The type of the graph's vertices
     * @param root the starting node for the sort
     * @param neighbors yields the set of neighbors for any vertex
     * @return a list of nodes so that for i < j, there is a path from the ith element to the jth      
     */
    public static <V> List<V> topologicalSort(V root, Function<V, Set<V>> neighbors) {
        var sorted = new ArrayList<V>();
        dfs(root, neighbors, sorted::add);
        return sorted.reversed();
    }

    /**
     * Gets all simple paths (without cycles) between two vertices 
     * @param <V> The type of the graph's vertices
     * @param from the starting vertex
     * @param to the ending vertex
     * @param neighbors yields the set of neighbors for any vertex
     * @return all simple paths joining from with to  
     */
    public static <V> Set<List<V>> simplePaths(V from, V to, Function<V, Set<V>> neighbors) {
        return simplePaths(from, to, neighbors, _ -> false);
    }   
    /**
     * Gets simple paths (without cycles) between two vertices, pruning fruitless searches  
     * @param <V> The type of the graph's vertices
     * @param from the starting vertex
     * @param to the ending vertex
     * @param neighbors yields the neighbors of a vertex
     * @param prune called with paths starting at from. Return false if this path should not be extended
     * (because it is too long, or has some other undesirable property)
     * @return all simple paths joining from with to  
     */
    public static <V> Set<List<V>> simplePaths(V from, V to, Function<V, Set<V>> neighbors, Predicate<List<V>> prune) {
        Queue<List<V>> pathsToExtend = new LinkedList<>();
        Set<List<V>> completed = new LinkedHashSet<>();
        pathsToExtend.add(List.of(from));
        while (!pathsToExtend.isEmpty()) {
            List<V> path = pathsToExtend.remove();
            for (V n : neighbors.apply(path.getLast())) {
                if (!path.contains(n)) {
                    List<V> extended = Lists.append(path, n);
                    if (!prune.test(extended)) {
                        if (n.equals(to)) completed.add(extended);
                        else pathsToExtend.add(extended);
                    }
                }
            }
        }
        return completed;
    }
   
    /**
     * Computes the minimum costs from a given vertex to all vertices in the graph  
     * @param <V> The type of the graph's vertices
     * @param root the starting vertex
     * @param neighbors yields the neighbors of a vertex
     * @param neighborDistances yields the cost of an edge joining two neighboring vertices
     * @return a map with the cost of the shortest path from the root to each vertex
     */
    public static <V> Map<V, Integer> dijkstraCosts(V root, Function<V, Set<V>> neighbors, ToIntBiFunction<V, V> neighborDistances) {
        Map<V, Integer> dist = new HashMap<>();
        Set<V> selected = new HashSet<>();
        PriorityQueue<Map.Entry<V, Integer>> q = new PriorityQueue<>(
                Map.Entry.<V, Integer>comparingByValue(Comparator.<Integer>naturalOrder()));
        q.add(Map.entry(root, 0));
        dist.put(root, 0);
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
                        q.remove(Map.entry(n, nd));
                        dist.put(n, sd + snd);
                        q.add(Map.entry(n, sd + snd));
                    }
                }
            }
        }
        return dist;
    }

    /**
     * Computes shortest paths from a given vertex to all vertices in the graph  
     * @param <V> The type of the graph's vertices
     * @param root the starting vertex
     * @param neighbors yields the neighbors of a vertex
     * @param neighborDistances yields the cost of an edge joining two neighboring vertices
     * @return a map with the predecessor of each vertex on a shortest path
     */
    public static <V> Map<V, V> dijkstraPaths(V from, Function<V, Set<V>> neighbors, ToIntBiFunction<V, V> neighborDistances) {
        Map<V, Integer> dist = new HashMap<>();
        Map<V, V> predecessors = new HashMap<>();
        Set<V> selected = new HashSet<>();
        PriorityQueue<Map.Entry<V, Integer>> q = new PriorityQueue<>(
                Map.Entry.<V, Integer>comparingByValue(Comparator.<Integer>naturalOrder()));
        q.add(Map.entry(from, 0));
        predecessors.put(from, null);
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
                        q.remove(Map.entry(n, nd));
                        dist.put(n, sd + snd);
                        q.add(Map.entry(n, sd + snd));
                        predecessors.put(n, s);
                    }
                }
            }
        }
        return predecessors;
    }
    
    /**
     * Computes all shortest paths from a given vertex to all vertices in the graph  
     * NOTE: After calling dijkstraAllPaths, call Graphs.dagPaths(target, preds::get) to get all min cost paths to the target
     * @param <V> The type of the graph's vertices
     * @param root the starting vertex
     * @param neighbors yields the neighbors of a vertex
     * @param neighborDistances yields the cost of an edge joining two neighboring vertices
     * @return a map with the predecessors of each vertex on a shortest path
     */
    public static <V> Map<V, Set<V>> dijkstraAllPaths(V from, Function<V, Set<V>> neighbors, ToIntBiFunction<V, V> neighborDistances) {
        Map<V, Integer> dist = new HashMap<>();
        Map<V, Set<V>> predecessors = new HashMap<>();
        Set<V> selected = new HashSet<>();
        PriorityQueue<Map.Entry<V, Integer>> q = new PriorityQueue<>(
                Map.Entry.<V, Integer>comparingByValue(Comparator.<Integer>naturalOrder()));
        q.add(Map.entry(from, 0));
        dist.put(from, 0);
        predecessors.put(from, new HashSet<>());
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
                        q.remove(Map.entry(n, nd));
                        dist.put(n, sd + snd);
                        q.add(Map.entry(n, sd + snd));
                        predecessors.put(n, new HashSet<>(Set.of(s)));
                    }
                    else if (nd == sd + snd) {
                        predecessors.get(n).add(s);
                    }
                }
            }
        }
        return predecessors;
    }

    /**
     * Computes the connected components of a graph
     * @param <V> The type of the graph's vertices
     * @param vertices all vertices of the graph
     * @param neighbors yields the neighbors of a vertex
     * @return the connected components 
     */
    public static <V> Set<Set<V>> connectedComponents(Collection<V> vertices, Function<V, Set<V>> neighbors) {
        var visited = new ArrayList<V>();
        var components = new HashSet<Set<V>>();
        for (var v : vertices) {
            if (!visited.contains(v)) {
                var c = Graphs.bfs(v, neighbors).keySet(); 
                visited.addAll(c);
                components.add(c);
            }   
        }
        return components;
    }

    /**
     * Formats the description of this graph in the GraphViz dot language.
     * @param <V> The type of the graph's vertices
     * @param root the starting vertex
     * @param neighbors yields the neighbors of a vertex
     * @param edgeLabels yields the label of an edge joining neighboring vertices, or null if no edge label is desired
     * @return the dot description
     * NOTE: Run dot -Tpdf outputs/dayN-a.dot > /tmp/dayN-a.pdf
     */    
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