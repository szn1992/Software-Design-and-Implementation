// Zhuonan Sun, HW6, 5/14/2014
// CSE331 AC
package hw6;

import hw5.Graph;
import hw6.MarvelParser.MalformedDataException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MarvelPaths {
	
	/**
	 * This class constructs a graph of characters from a well-formed file, and 
	 * finds the shortest path between two nodes that the user enters
	 */
	
	/**
	 * 
	 * @param args arguments of the main method
	 * @requires fileName entered is valid or is "exit"(case insensitive)
	 * @requires args must be "java -ea -cp bin hw6.MarvelPaths"
	 * @effects prompt the user to enter a fileName. Leave the console if fileName 
	 * entered is "exit"(case insensitive), else build a graph with the corresponding
	 * file; Then keep prompting the user to enter two names of two characters to print
	 * the shortest path, until user enters "exit"(case insensitive)
	 * @throws MalformedDataException if the file corresponding to fileName is not 
	 * well-formed
	 */
	public static void main(String[] args) throws MalformedDataException {
		@SuppressWarnings("resource")
		Scanner console = new Scanner(System.in);
		System.out.println("Which file do you want to read?");

		String fileName = console.nextLine();
		if(fileName.equalsIgnoreCase("EXIT"))
			return;
		Graph<String, String> g = buildGraph(fileName);
		
		System.out.println("Which path of two characters do you want to find out?");
		String line = console.nextLine();
		while(!line.equalsIgnoreCase("EXIT")) {
			String[] arguments = line.split(" ");
			if(arguments.length != 2) {
				System.out.println("Bad arguments!");
			} else {
				String node_1 = arguments[0];
				String node_n = arguments[1];
				printPath(node_1, node_n, g);
			}
			
			System.out.println();
			System.out.println("Which path of two characters do you want to find out?");
			line = console.nextLine();
		}
	}
	
	/**
	 * Helper method for main
	 * @param node_1 the starting node
	 * @param node_n the destination node
	 * @param g the graph being search
	 * @effects print the path between two nodes; print the appropriate message 
	 * based on the result
	 */
	private static void printPath(String node_1, String node_n, Graph<String, String> g) {
		node_1 = node_1.replace('_', ' ');
    	node_n = node_n.replace('_', ' ');
    	if(!g.containsNode(node_1) || !g.containsNode(node_n)) {
    		if(!g.containsNode(node_1)) 
    			System.out.println("unknown character " + node_1);
    		if(!g.containsNode(node_n))
    			System.out.println("unknown character " + node_n);
    		return;
    	}  else {
    		ArrayList<Edge> path = getShortestPath(node_1, node_n, g);
    		System.out.println("path from " + node_1 + " to " + node_n + ":");
    
	    	if(node_1.equals(node_n)) {
	    		return;
	    	} else if(path.isEmpty()) {
	    		System.out.println("no path found");
	    	} else {
	    		String left = node_1;
	    		String right = path.get(0).getLabel();
	    		String middle;
	    		for(int i = 1; i < path.size(); i++) {
	    			middle = path.get(i).getParent();
	    			System.out.println(left + " to " + middle + " via " + right);
	    			left = middle;
	    			right = path.get(i).getLabel();
	    		}
	    		System.out.println(left + " to " + node_n + " via " + right);
	    	}
    	}
	}
	
	/**
	 * 
	 * @param name of the file to help build the graph
	 * @return the graph built from data in the file
	 * @throws MalformedDataException if the file corresponding to the fileName
	 * is not well-formed
	 */
	public static Graph<String, String> buildGraph(String fileName) 
										throws MalformedDataException {
		
		Graph<String, String> graph = new Graph<String, String>();
		Set<String> characters = new HashSet<String>();
		Map<String, List<String>> books= new HashMap<String, List<String>>();
	
		// read data from marvel.tsv
		MarvelParser.parseData("src/hw6/data/" + fileName, characters, books);
    
		// add nodes in graph
		addCharacters(graph, characters);
				
		// add edges in graph
		addEdges(graph, books);
		return graph; 
	}
	
	/**
	 * 
	 * @param start starting character of the graph
	 * @param dest ending character of the graph
	 * @param graph the graph being searched
	 * @return an ArrayList of Edge, in a form as "(start, n1), (n1, n2),..., (nn, dest),"
	 * which is the shortest path between start and dest
	 * @throws IllegalArgumentException if one of three parameters is null
	 */
	public static ArrayList<Edge> getShortestPath(String start, String dest, 
												Graph<String, String> graph) {
		
		if(start == null || dest == null || graph == null) {
			throw new IllegalArgumentException("start, dest, or graph is null");
		}
		LinkedList<String> toVisit = new LinkedList<String>();
		HashMap<String, ArrayList<Edge>> paths = new HashMap<String, ArrayList<Edge>>();
		toVisit.add(start);
		paths.put(start, new ArrayList<Edge>());
		
		while(!toVisit.isEmpty()) {
			String node = toVisit.removeFirst();
			if(node.equals(dest)) {
				return paths.get(dest);
			}
			TreeSet<String> neighbors = new TreeSet<String>(graph.getChildren(node));
			
			for(String neighbor : neighbors) {
				if(!paths.containsKey(neighbor)) {
					String label = new TreeSet<String>(
									graph.getLabels(node, neighbor)).first();
					
					ArrayList<Edge> pathToNeighbor = new ArrayList<Edge>(paths.get(node));
					pathToNeighbor.add(new Edge(node, label));
					paths.put(neighbor, pathToNeighbor);
					
					toVisit.add(neighbor);
				}
			}
		}
		
		return new ArrayList<Edge>();
	}
	
	/**
	 * Helper method of buildGraph
	 * @param graph to be updated be characters
	 * @param characters to help update nodes in graph
	 * @modifies graph
	 * @effect add each character from characters to graph
	 * @throws IllegalArgumentException if graph or characters is null
	 */
	private static void addCharacters(Graph<String, String> graph, Set<String> characters) {
		if(graph == null || characters == null) {
			throw new IllegalArgumentException("graph or characters is null");
		}
		for(String character : characters) {
			graph.addNode(character);
		}
	}
	
	/**
	 * Helper method of buildGraph
	 * @param graph to be updated by books
	 * @param books to help adding edges to graph
	 * @modifies graph
	 * @effect add edges to nodes which appear in the same book
	 * @throws IllegalArgumentException is graph or books is null
	 */
	private static void addEdges(Graph<String, String> graph, Map<String, List<String>> books) {
		if(graph == null || books == null) {
			throw new IllegalArgumentException("graph or books is null");
		}
	
		for(String bookName : books.keySet()) {
			List<String> characterList = books.get(bookName);
			for(int i = 0; i < characterList.size(); i++) {
				String parent = characterList.get(i);
				for(int j = i + 1; j < characterList.size(); j++) {
					String child = characterList.get(j);
					graph.addEdge(parent, bookName, child);
					graph.addEdge(child, bookName, parent);
				}
			}
		}
	}
	
	/**
	 * Inner class in MarvelPath, used to represent one element of list returned
	 * from getShortestPath
	 * @author szn1992
	 *
	 */
	public static class Edge {
		/**
		 * Abstraction Function: Edge e include two fields: parent: name
		 * of the parent node; label: name of the label of the edge between
		 * parent node and child node
		 *  
		 * Representation Invariant: parent != null & label != null
		 */
		
		private String parent;	// name of the parent node
		private String label;	// name of the label
		
		/**
		 * 
		 * @param parent name of parent
		 * @param label name of label
		 * @effects construct a new Edge with given name of parent node
		 * and name of label
		 */
		public Edge(String parent, String label) {
			this.parent = parent;
			this.label = label;
			checkRep();
		}
		
		/**
		 * @return the name of parent
		 */
		public String getParent() {
			checkRep();
			return parent;
		}
		
		/**
		 * @return the name of label
		 */
		public String getLabel() {
			checkRep();
			return label;
		}
		
		
		/**
		 *  Checks that the representation invariant holds
		 */
		private void checkRep() {
			assert(parent != null);
			assert(label != null);
		}
	}
}
