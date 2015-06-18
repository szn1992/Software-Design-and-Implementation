// Zhuonan Sun, hw8, CSE331AC, 5/29/2014 
package hw8;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path<E> {
	
	/**
	 * Path<E> is a generic class, it is a good structure to store a path
	 * from starting point to ending point, which helps CampusPath to return
	 * a shortest path
	 */
	
	/**
	 * Abstraction Function: Path p include two fields: 
	 * path: the list of nodes in the path; 
	 * totalCost: sum of weight of edges between each pair of nodes
	 *  
	 * Representation Invariant: path != null & totalCost >= 0
	 */
	
	private List<E> path;	// the list of nodes in the path
	private double totalCost;	// total cost of all edges in path
	
	/**
	 * construct Path with no argument
	 */
	public Path() {
		path = new ArrayList<E>();
		totalCost = 0.0;
		checkRep();
	}
	
	/**
	 * construct Path with a list of nodes and its totalCOST
	 * @param path the list of nodes which forms the path
	 * @param totalCost the totalCost of the path
	 */
	public Path(List<E> path, double totalCost) {
		this.path = path;
		this.totalCost = totalCost;
		checkRep();
	}
	
	/**
	 * @param dest new point to be added
	 * @param cost weight of the new point
	 * @throws IllegalArgumentException if dest is null | cost < 0
	 * @return new Path consist of the list appended with dest, and the updated
	 * totalCost
	 */
	public Path<E> add(E dest, double cost) {
		if(dest == null || cost < 0) {
			throw new IllegalArgumentException("dest is null or cost is negative");
		}
		ArrayList<E> newPath = new ArrayList<E>(path);
		newPath.add(dest);
		return new Path<E>(newPath, totalCost + cost);
	}
	
	/**
	 * @return the list of Path
	 */
	public List<E> getPath() {
		return Collections.unmodifiableList(path);
	}
	
	/**
	 * @return the totalCost of Path
	 */
	public double getCost() {
		return totalCost;
	}
	
	/**
	 *  Checks that the representation invariant holds
	 */
	private void checkRep() {
		assert(path != null);
		assert(totalCost >= 0);
	}
}

