package com.horstmann.adventofcode;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Digraph<V, E> {
	public record Edge<V, E>(V from, V to, E label) {
	}

	private Set<V>vertices=new HashSet<>(); // TODO why not use map?
	private Map<V, Set<Edge<V, E>>> outgoing = new HashMap<>();
	private Map<V, Set<Edge<V, E>>> incoming = new HashMap<>();

	public void addEdge(V from, V to, E label) {
		vertices.add(from);
		vertices.add(to);
		outgoing.computeIfAbsent(from, _ -> new HashSet<>()).add(new Edge<>(from, to, label));
		incoming.computeIfAbsent(to, _ -> new HashSet<>()).add(new Edge<>(from, to, label));
	}

	public Set<Edge<V, E>> outgoing(V v) {
		return outgoing.computeIfAbsent(v, _ -> Collections.emptySet());
	}

	public Set<Edge<V, E>> incoming(V v) {
		return incoming.computeIfAbsent(v, _ -> Collections.emptySet());
	}

	public Optional<Edge<V, E>> edge(V from, V to) {
		return outgoing.computeIfAbsent(from, _ -> Collections.emptySet()).stream().filter(e -> e.to.equals(to))
				.findFirst();
	}
	
	public Set<V> vertices() { return vertices; }

	public Set<V> neighbors(V v) {
		return outgoing(v).stream().map(Edge::to).collect(Collectors.toSet());
	}

	public Function<V, Set<V>> neighbors() { return v -> neighbors(v); }
	
	public String dot() {
		var builder = new StringBuilder();
		builder.append("digraph {\n");
		for (var edges : outgoing.values()) {
			for (Edge<V, E> e : edges) {
			    if (e.label() == null)
	                builder.append("   \"%s\" -> \"%s\"\n".formatted(e.from(), e.to(), e.label()));
			    else
			        builder.append("   \"%s\" -> \"%s\"[label=\"%s\"]\n".formatted(e.from(), e.to(), e.label()));
			}
		}
		builder.append("}\n");
		return builder.toString();
	}
}
