package hw8;

import java.util.Comparator;

/**
 * Helps to find the shortest path between two points
 * Used to support the PriortyQueue to compare
 * weights of paths
 * @param <E> the date type store in path
 * 
 */
public class PathComparator<E> implements Comparator<Path<E>> {
	/**
	 * @param p1 Path1
	 * @param p2 Path2
	 * @return 0 if p1 p2 have same cost, 1 if p1 has greater cost, -1 otherwise
	 */
	public int compare(Path<E> p1, Path<E> p2) {
		if(p1 == null  || p2 == null) {
			throw new IllegalArgumentException("p1 is null or p2 is null");
		}
		if(p1.getCost() < p2.getCost()) 
			return -1;
		else if (p1.getCost() == p2.getCost())
			return 0;
		else 
			return 1;
	}
}