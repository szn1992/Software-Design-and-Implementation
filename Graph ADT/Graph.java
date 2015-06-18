// Homework 5, Zhuonan Sun, CSE 331 AC, 5/6/2014

package hw5;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class Graph<N, E> {
	/**
	 * This class can be used to represent a directed labeled multigraph
	 * where labels are not unique, which means multiple edges may have the same label 
	 * 
	 * Abstraction Function: 
	 * nodeMap contains all the nodes and edges in the graph
	 * nodeMap.keySet() contains all the nodes in the graph
	 * nodeMap.get(node) contains all the edges from node to other nodes
	 * 
	 * Representation Invariant:
	 * nodeMap != null & nodeMap.keySet() != null 
	 * & For each node of nodeMap.keySet(), nodeMap.get(node) != null
	 */
	
	private HashMap<N, Edges<N, E>> nodeMap;
	private boolean debug;	// debug mode
	
	/**
	 * @effect creates a new graph with an empty map inside, set debug mode off
	 */
	public Graph() {
		nodeMap = new HashMap<N, Edges<N, E>>();
		debug = false;
		checkRep();
	}
	
	/**
	 * 
	 * @param parent, the new key node to be added in nodeMap
	 * @throws IllegalArgumentException if parent is null
	 * @modifies this
	 * @effect new parent node will be added to keySet of nodeMap if it's
	 * not in keySet already
	 */
	public void addNode(N parent) {
		if(parent == null)
			throw new IllegalArgumentException("node is null");
		if(!nodeMap.containsKey(parent))
			nodeMap.put(parent, new Edges<N, E>());
		checkRep();
	}
	
	/**
	 * 
	 * @param parent, the key node to be adding an edge
	 * @param label, the label of the edge to be added
	 * @param child, the target node of the edge to be added
	 * @modifies this
	 * @throws IllegalArgumentException if parent or label or child is null |
	 * parent or child does not exist
	 * @effect new edge will be added in the set if there is no identical
	 * edge, it will not be added otherwise
	 */
	public void addEdge(N parent, E label, N child) {
		if(parent == null || label == null || child == null) {
			throw new IllegalArgumentException("parent, label, or child is null");
		} else if(!this.containsNode(parent) || !this.containsNode(child)) {
			throw new IllegalArgumentException("parent, or child does not exist");
		} else {
			nodeMap.get(parent).addEdge(child, label);
		}
		checkRep();
	}
	
	/**
	 * 
	 * @param parent, the parent node to be deleted from nodeMap
	 * @modifies this
	 * @throws IllegalArgumentException if parent == null | parent does not exist
	 * @effect the parent node and all edges pointing to it will be removed if 
	 * there is any
	 */
	public void removeNode(N parent) {
		if(parent == null || !containsNode(parent)) {
			throw new IllegalArgumentException("parent is null or it does not exist");
		}
		nodeMap.remove(parent);
		for(N n : nodeMap.keySet()) {
			Edges<N, E> edges = nodeMap.get(n);
			if(edges.containsChild(parent))
				edges.removeChild(parent);
		}
		checkRep();
	}
	
	/**
	 * 
	 * @param parent, whose one edge to be deleted
	 * @param label, the label of the edge to be deleted
	 * @param child, target of the edge to be deleted
	 * @modifies this
	 * @throws IllegalArgumentException if parent == null | parent does not
	 * exists | label == null | child == null | label or child does not exist
	 * @effect the edge will be deleted
	 */
	public void removeEdge(N parent, E label, N child) {
		if(parent == null) {
			throw new IllegalArgumentException("parent is null");
		} else {
			// removeEdge will throw exception from other cases when needed
			getEdges(parent).removeEdge(child, label);
		}
		checkRep();
	}
	
	/**
	 * 
	 * @param parent, whose edges will all be deleted
	 * @modifes this
	 * @throws an IllegalArgumentException if parent == null | parent does not
	 * exists  
	 * @effect the edges of parent node will all be deleted
	 */
	public void removeAllEdges(N parent) {
		if(parent == null || !containsNode(parent)) 
			throw new IllegalArgumentException("parent is null | parent does not exist");
		getEdges(parent).clear();
		checkRep();
	}
	
	/**
	 * @modifies this
	 * @effect remove all the contents of the graph
	 */
	public void clear() {
		nodeMap.clear();
		checkRep();
	}
	
	/**
	 * @return the set of nodes in graph
	 */
	public Set<N> getNodes() {
		return Collections.unmodifiableSet(nodeMap.keySet());
	}
	
	/**
	 * Helper method to get Edges of the parent node
	 * @param parent, the Node whose edges to child nodes to be returned
	 * @throws IllegalArgumentException if parent == null | parent does
	 * not exist
	 * @return all the edges from the parent node 
	 */
	private Edges<N, E> getEdges(N parent) {
		if(parent == null || !containsNode(parent))
			throw new IllegalArgumentException("parent is null | parent does not exist");
		return nodeMap.get(parent);
	}
	
	/**
	 * @param parent, whose set of child nodes to be returned
	 * @throws an IllegalArgumentException if parent == null | parent does not exist
	 * @return set of child nodes of the parent node
	 */
	public Set<N> getChildren(N parent) {
		if(parent == null || !containsNode(parent))
			throw new IllegalArgumentException("parent is null | parent does not exist");
		return nodeMap.get(parent).getChildren();
	}
	
	/**
	 * @param parent, whose labels of a child node to be returned
	 * @throws an IllegalArgumentException if parent or child is null | 
	 * parent or child does not exist
	 * @return set of child nodes of the parent node
	 */
	public Set<E> getLabels(N parent, N child) {
		if(parent == null || !containsNode(parent))
			throw new IllegalArgumentException("parent is null | parent does not exist");
		// Edges.getLabels will throw exception if child is null or does not exist
		return nodeMap.get(parent).getLabels(child);
	}
	
	/**
	 * @param parent, the parent node to be checked if it exists on nodeMap
	 * @throws IllegalArgumentException if parent == null 
	 * @return true, if it is in keySet of nodeMap
	 */
	public boolean containsNode(N parent) {
		if(parent == null)
			throw new IllegalArgumentException("parent is null");
		return nodeMap.containsKey(parent);
	}
	
	/**
	 * 
	 * @param parent, to be checked if it has the edge
	 * @param label, the label of edge to be searched for
	 * @param child, the target of the edge to be searched
	 * @throws IllegalArgumentException if parent == null | label == null |
	 * child == null | parent or child do not exist
	 * @return true if the parent node has an edge to the child node
	 */
	public boolean containsEdge(N parent, E label, N child) {
		if(parent == null || child == null || label == null ||
			!containsNode(parent) || !containsNode(child)) {
			throw new IllegalArgumentException("parent, child, or label is null"
												+ "| parent does not exist");
		}
		if(getChildren(parent).contains(child)) {
			return getLabels(parent, child).contains(label);
		} else
			return false;
	
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
	 * Checks that the representation invariant holds if debug mode is on
	 * checks partial holds if debug mode is off
	 * 
	**/
	private void checkRep() {
		assert(nodeMap != null);
		assert(nodeMap.keySet() != null);
		if(debug) {
			for(N n : nodeMap.keySet())
				assert(nodeMap.get(n) != null);
		}
	}
}
