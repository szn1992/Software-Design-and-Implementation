// Zhuonan Sun, HW7, CSE331 AC
// 5/22/2014
package hw7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import hw5.Graph;
import hw6.MarvelParser;
import hw6.MarvelParser.MalformedDataException;
import hw6.MarvelPaths;

public class MarvelPaths2 {
		
		/**
		 * This class constructs a graph of characters from a well-formed file, and 
		 * finds the shortest path between two nodes that the user enters, and calculate
		 * its totalCost by Dijkstra Algorithm
		 */
	
		/**
		 * 
		 * @param fileName name of file to be processed to build graph
		 * @return a built graph with nodes and edges
		 * @throws MalformedDataException if the file is not well-formed
		 */
		public static Graph<String, Double> buildGraph(String fileName) 
											throws MalformedDataException {
			// graph to be built
			Graph<String, Double> graph = new Graph<String, Double>();
			try {	
				HashSet<String> characters = new HashSet<String>();		// character lists
				
				// each name of book maps to a list of characters
				HashMap<String, List<String>> books= new HashMap<String, List<String>>();
				
				// read data from marvel.tsv
				MarvelParser.parseData("src/hw7/data/" + fileName, characters, books);
				
				// add nodes to graph
				addCharacters(graph, characters);
				
				// build a graph from MarvelPaths
				Graph<String, String> oldGraph = MarvelPaths.buildGraph(fileName);
				
				// add edges to graph
				addEdges(graph, oldGraph);
				
			} catch (MalformedDataException e) {
		        System.err.println(e.toString());
		        e.printStackTrace(System.err);
		    }
			return graph;
		}
		
		/**
		 * 
		 * @param start starting node of the path to be searched
		 * @param dest ending node of the path to be searched
		 * @param graph the graph to be searched for shortest path
		 * @throw IllegalArgumentException if start, dest or graph is null, or
		 * start or dest is not on graph
		 * @return the shortest Path from start to dest, which consists of a list
		 * of nodes and the totalCost of the path; 
		 * @return null if no path is found
		 */
		public static Path getShortestPath(String start, String dest, 
													Graph<String, Double> graph) {
			
			if(start == null || dest == null || graph == null) {
				throw new IllegalArgumentException("start, dest, or graph is null");
			}
			if(!graph.containsNode(start) || !graph.containsNode(dest)) {
				throw new IllegalArgumentException("start or dest is not on graph");
			}
			
			// a comparator used in PriorityQueue to compare the weights of paths
			PathComparator comparator = new PathComparator();
			
			// stores the paths to be processed, in the order of their totalCost
			PriorityQueue<Path> active = new PriorityQueue<Path>(10, comparator);
			
			// set of nodes we know the minimum-cost path from start
			HashSet<String> finished = new HashSet<String>();
			
			// add a path from start to itself to active
			ArrayList<String> initialList = new ArrayList<String>();
			initialList.add(start);
			
			Path startToItself = new Path(initialList, 0.0);
			
			active.add(startToItself);
			while(!active.isEmpty()) {
				// minPath is the lowest-cost path in active and is the
		        // minimum-cost path for some node
				Path minPath = active.remove();
				List<String> minPathList = minPath.getPath();
				String minDest = minPathList.get(minPathList.size() - 1);
				
				if(minDest.equals(dest)) {
					return minPath;
				}
				
				if(finished.contains(minDest)) {
					continue;
				}
				
				for(String child : graph.getChildren(minDest)) {
					// check if a minimum-cost path to child has been found
					if(!finished.contains(child)) {
						Set<Double> costs = graph.getLabels(minDest, child);
						TreeSet<Double> sorted = new TreeSet<Double>(costs);
						Path newPath = minPath.add(child, sorted.first());
						active.add(newPath);
					}
				}
				
				finished.add(minDest);
			}
			
			return null;
		}
		
		/**
		 * Helper method to build graph
		 * @param graph to be added with edges
		 * @param oldGraph used to get the inverses of number of books as costs edges
		 * @modifies graph
		 * @effect add edges to graph
		 */
		private static void addEdges(Graph<String, Double> graph, Graph<String, String> oldGraph) {
			for(String parent : oldGraph.getNodes()) {
				for(String child : oldGraph.getChildren(parent)) {
					// get the cost of each path from number of books
					int numOfBooks = oldGraph.getLabels(parent, child).size();
					graph.addEdge(parent, 1.0 / numOfBooks, child);
					graph.addEdge(child, 1.0 / numOfBooks, parent);
				}
			}
		}
		
		/**
		 * Helper method of buildGraph
		 * @param graph to be updated be characters
		 * @param characters to help update nodes in graph
		 * @modifies graph
		 * @effect add each character from characters to graph
		 * @throws IllegalArgumentException if graph or characters is null
		 */
		private static void addCharacters(Graph<String, Double> graph, Set<String> characters) {
			if(graph == null || characters == null) {
				throw new IllegalArgumentException("graph or characters is null");
			}
			for(String character : characters) {
				graph.addNode(character);
			}
		}
		
		
		/**
		 * Inner immutable class in MarvelPaths2, used to represent a path 
		 * from a starting node to a ending node
		 *
		 */
		public static class Path {
			/**
			 * Abstraction Function: Path p include two fields: 
			 * path: the list of nodes in the path; 
			 * totalCost: sum of weight of edges between each pair of nodes
			 *  
			 * Representation Invariant: path != null & totalCost >= 0
			 */
			
			private List<String> path;	// the list of nodes in the path
			private double totalCost;	// total cost of all edges in path
			
			/**
			 * construct Path with no argument
			 */
			public Path() {
				path = new ArrayList<String>();
				totalCost = 0.0;
				checkRep();
			}
			
			/**
			 * construct Path with a list of nodes and its totalCOST
			 * @param path the list of nodes which forms the path
			 * @param totalCost the totalCost of the path
			 */
			public Path(List<String> path, double totalCost) {
				this.path = path;
				this.totalCost = totalCost;
				checkRep();
			}
			
			/**
			 * @throws IllegalArgumentException if dest is null | cost < 0
			 * @return new Path consist of the list appended with dest, and the updated
			 * totalCost
			 */
			public Path add(String dest, double cost) {
				if(dest == null || cost < 0) {
					throw new IllegalArgumentException("dest is null or cost is negative");
				}
				ArrayList<String> newPath = new ArrayList<String>(path);
				newPath.add(dest);
				return new Path(newPath, totalCost + cost);
			}
			
			/**
			 * @return the list of Path
			 */
			public List<String> getPath() {
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
		
		/**
		 * Inner class of MarvelPaths2, used to support the PriortyQueue to compare
		 * weights of paths
		 * @author szn1992
		 *
		 */
		public static class PathComparator implements Comparator<Path> {
			/**
			 * @param p1 Path1
			 * @param p2 Path2
			 * @return 0 if p1 p2 have same cost, 1 if p1 has greater cost, -1 otherwise
			 */
			public int compare(Path p1, Path p2) {
				if(p1 == null  || p2 == null) {
					throw new IllegalArgumentException("p1 is null or p2 is null");
				}
				if(p1.getCost() < p2.getCost()) {
					return -1;
				} else if (p1.getCost() == p2.getCost())
					return 0;
				else 
					return 1;
			}
		}
}
