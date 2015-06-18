// Homework 5, Zhuonan Sun, CSE 331 AC, 5/6/2014

package hw5;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Edges<N, E> {
	
	/**
	 * This class represent all edges to a child node in the graph, edges are classified
	 * by name of the child node
	 * 
	 * Abstraction Function:
	 * Edges e map String child to their corresponding labels
	 * 
	 * Representation Invariant:
	 * edges != null & edges.keySet != null & if i is in keySet then edges.get(i) != null
	 */
	
	private HashMap<N, HashSet<E>> edges;
	private boolean debug;	// debug mode
	
	/**
	 * @effect create a new empty map of edges, set debug mode off
	 */
	public Edges() {
		edges = new HashMap<N, HashSet<E>>();
		debug = false;
		checkRep();
	}
	
	/**
	 * 
	 * @param label, label of the edge to be added
	 * @param child, target of the edge to be added
	 * @throws IllegalArgumentException if label or child is null
	 * @effect label will be put into child's set. 
	 * child will be put in keySet of map with its label 
	 * if child is not in keySet already
	 */
	public void addEdge(N child, E label) {
		if(label == null || child == null)
			throw new IllegalArgumentException("label or child is null");
		if(!edges.containsKey(child)) {
			edges.put(child, new HashSet<E>());
		}
		edges.get(child).add(label);
		checkRep();
	}
	
	/**
	 * 
	 * @param label, the label of the edge
	 * @param child, the edge pointing to it to be removed
	 * @throws an illegal argument exception if label or child is null|
	 * label or child does not exist
	 * @effect the edge to child with label will be removed.
	 * After removal, the set the label maps to will be removed if the set is empty
	 */
	public void removeEdge(N child, E label) {
		if(child == null || label == null) {
			throw new IllegalArgumentException("child or label is null");
	    } else if(!edges.containsKey(child)) {
			throw new IllegalArgumentException("child does not exist");
		} else if(!edges.get(child).contains(label)) {
			throw new IllegalArgumentException("label does not exist");
		} else {
			edges.get(child).remove(label);
			// remove the label if the set it maps to is empty
			if(edges.get(child).isEmpty())
				edges.remove(child);
		}	
		checkRep();
	}
	
	/**
	 * 
	 * @param child to be removed
	 * @effect child will be removed from keySet of edges
	 */
	public void removeChild(N child) {
		if(child == null) {
			throw new IllegalArgumentException("child is null");
		} else if(!edges.containsKey(child)) {
			throw new IllegalArgumentException("child does not exist");
		} else
			edges.remove(child);
		checkRep();
	}
	
	/**
	 * 
	 * @return set of child nodes
	 */
	public Set<N> getChildren() {
		return Collections.unmodifiableSet(edges.keySet());
	}
	
	/**
	 * @param label, label of the edge
	 * @throws illegal argument exception if child is null |
	 * child does not exist
	 * @return a set of child nodes with the same label
	 */
	public Set<E> getLabels(N child) {
		checkRep();
		if(child == null || !containsChild(child))
			throw new IllegalArgumentException("child is null | child does not exist");
		return Collections.unmodifiableSet(edges.get(child));
	}
	
	/**
	 * 
	 * @param label, the key to child to be searched for
	 * @param child, the child node to be searched for
	 * @throws an illegal argument exception if label or child is null |
	 * child does not exist
	 * @return true if the edge exists
	 */
	public boolean containsLabel(N child, E label) {
		if(label == null || child == null || !containsChild(child))
			throw new IllegalArgumentException("label or child is null | child does not exist");
		if(edges.containsKey(child)) {
			return edges.get(child).contains(label);
		} else
			return false;
	}
	
	/**
	 * 
	 * @param label to be checked if it's in keySet of edges
	 * @throws an illegal argument exception if child is null
	 * @return true if edges has label in keySet
	 */
	public boolean containsChild(N child) {
		if(child == null)
			throw new IllegalArgumentException("child is null");
		return edges.containsKey(child);
	}
	
	/**
	 * remove all the contents on the map
	 */
	public void clear() {
		edges.clear();
		checkRep();
	}
	
	/**
	 * turn debug mode on
	 * @modifies this
	 */
	public void debugOn() {
		debug = true;
	}
	
	/**
	 * turn debug mode off
	 * @modifies this
	 */
	public void debugOff() {
		debug = false;
	}
	
	/**
	 * Checks that all the representation invariant holds if debug mode is on
	 * checks partial holds if debug mode is off
	 */
	private void checkRep() {
		assert(edges != null);
		assert(edges.keySet() != null);
		if(debug) {
			for(N child : edges.keySet()) {
				assert(edges.get(child) != null);
			}
		}
	}
}
