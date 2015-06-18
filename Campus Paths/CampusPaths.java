// Zhuonan Sun, hw8, CSE331AC, 5/29/2014 
package hw8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import hw5.Graph;
import hw8.CampusParser.MalformedDataException;

/*
 * This class is used to represent a map of campus, it reads data
 * from buildings file and paths file, and stores all the information of
 * locations and paths. It can also look up which buildings are stored, 
 * and find the shortest path between two buildings
 */
public class CampusPaths {
	/**
	 * Abstract function: CampusPaths consists of two main structure:
	 * graph g consists of information of all locations and paths among them
	 * buildings b consists of all information about buildings
	 * 
	 * Representation invariant: graph != null & buildings != null
	 */
	
	private Graph<Building, Double> graph; // structure of the model
	private Set<Building> buildings;	// all buildings on campus
	
	/**
	 * Constructs CampusPath with buildings file and paths file
	 * @param filename_buildings filename of file which consists of data of buildings
	 * @param filename_paths filename of file which consists of data of paths
	 * @throws MalformedDataException if any file is not well-formed
	 */
	public CampusPaths(String filename_buildings, String filename_paths) {
		graph = new Graph<Building, Double>();
		buildGraph(filename_buildings, filename_paths);
		checkRep();
	}
	
	/**
	 * 
	 * @param start starting building
	 * @param dest ending building
	 * @throws IllegalArgumentException start or dest is null |
	 * start or dest does not exist
	 * @return the distance between two locations
	 */
	public double getDistance(Building start, Building dest) {
		if(start == null || dest == null) {
			throw new IllegalArgumentException("start or dest is null");
		} 
		if(!graph.containsNode(start) || !graph.containsNode(dest)) {
			throw new IllegalArgumentException("start or dest does not exist");
		}
		TreeSet<Double> sorted = new TreeSet<Double>(graph.getLabels(start, dest));
		return sorted.first();
	}
	
	/**
	 * Observer
	 * @return set of buildings on campus
	 */
	public Set<Building> getBuildings() {
		return Collections.unmodifiableSet(buildings);
	}
	
	/**
	 * Helps to print output in CampusMain when command is "b"
	 * @return sorted map where abbreviated names(K) maps to full names(V) 
	 */
	public TreeMap<String, String> sortBuildingNames() {
		TreeMap<String, String> nameMap = new TreeMap<String, String>();
		for(Building b : buildings) {
			nameMap.put(b.getShortName(), b.getLongName());
		}
		return nameMap;
	}
	
	/**
	 * Find the building given a short name
	 * @param abbr abbreviated name of one building
	 * @return building with the abbreviated name
	 * return null if it is not found
	 */
	public Building findBuilding(String abbr) {
		if(abbr == null) {
			throw new IllegalArgumentException("abbr is null");
		}
		for(Building b : buildings) {
			if(b.getShortName().equals(abbr))
				return b;
		}
		return null;
	}
	
	public String getDirection(double theta) {
		double pi = Math.PI;
		if (theta > (-pi/8) && theta < pi/8 ) {
			return "E";
		} else if (theta > pi/8 && theta < 3*pi/8) {
			return "NE";
		} else if (theta > 3*pi/8 && theta < 5*pi/8) {
			return "N";
		} else if (theta > 5*pi/8 && theta < 7*pi/8) {
			return "NW";
		} else if (theta > 7*pi/8 || theta < -7*pi/8) {
			return "W";
		} else if (theta > -7*pi/8 && theta < -5*pi/8) {
			return "SW";
		} else if (theta > -5*pi/8 && theta < -3*pi/8) {
			return "S";
		} else {
			return "SE";
		} 
	}
	
	/**
	 * 
	 * @param start shortName of starting building
	 * @param dest shortName of ending building
	 * @throws IllegalArgumentException if start or dest is null | start
	 * or dest does not exist on campus
	 * @return Path of buildings from starting building to ending
	 * building
	 */
	public Path<Building> getShortestPath(String start, String dest) {
		checkNames(start, dest);
	
		// a comparator used in PriorityQueue to compare the weights of paths
		PathComparator<Building> comparator = new PathComparator<Building>();
		
		// stores the paths to be processed, in the order of their totalCost
		PriorityQueue<Path<Building>> active = new PriorityQueue<Path<Building>>(10, comparator);
		
		// set of nodes we know the minimum-cost path from start
		HashSet<Building> finished = new HashSet<Building>();
		
		// add a path from start to itself to active
		ArrayList<Building> initialList = new ArrayList<Building>();
		initialList.add(findBuilding(start));
	
		Path<Building> startToItself = new Path<Building>(initialList, 0.0);
	
		active.add(startToItself);
		
		while(!active.isEmpty()) {
			// minPath is the lowest-cost path in active and is the
			// minimum-cost path for some node
			Path<Building> minPath = active.remove();
			List<Building> minPathList = minPath.getPath();
			Building minDest = minPathList.get(minPathList.size() - 1);
			
			if(minDest.getShortName().equals(dest)) {
				return minPath;
			}
	
			if(finished.contains(minDest)) {
				continue;
			}
			
			for(Building child : graph.getChildren(minDest)) {
			// check if a minimum-cost path to child has been found
				if(!finished.contains(child)) {
					Set<Double> costs = graph.getLabels(minDest, child);
					TreeSet<Double> sorted = new TreeSet<Double>(costs);
					Path<Building> newPath = minPath.add(child, sorted.first());
					active.add(newPath);
				}	
			}
	
			finished.add(minDest);
		}
	
		return null;
	}
	
	/**
	 * Helper method for gettingShortedPath, checks if shortNames are valid
	 * @param start shortName of starting building
	 * @param dest shortName of ending building
	 * @throws IllegalArgumentException if start or dest is null | start
	 * or dest does not exist on campus
	 */
	private void checkNames(String start, String dest) {
		if(start == null || dest == null) {
			throw new IllegalArgumentException("start, dest is null");
		}
		
		boolean startValid = false;
		boolean destValid = false;
		for(Building b : graph.getNodes()) {
			if(b.getShortName().equals(start)) 
				startValid = true;
			
			if(b.getShortName().equals(dest)) 
				destValid = true;
		}
		if(!startValid || !destValid)
			throw new IllegalArgumentException("start or dest does not exist");
	}
	
	/**
	 * Build the graph from buildings file and paths file
	 * @param filename_buildings filename of the buildings
	 * @param filename_paths filename of paths
	 * @return graph built with buildings and paths
	 * @throws MalformedDataException if the output of any file is not well-formed 
	 * @modify graph
	 */
	private void buildGraph(String filename_buildings, 
											String filename_paths) {
		try {
			// get set of buildings from buildings file
			buildings = CampusParser.parseBuildings("src/hw8/data/" + 
															filename_buildings);
							
			graph = new Graph<Building, Double>();
			
			// add buildings
			for(Building b : buildings) {
				graph.addNode(b);
			}
			
			// get the coordinate map from paths file
			Map<Building, HashMap<Building, TreeSet<Double>>> coMap = 
											CampusParser.parsePaths("src/hw8/data/" + filename_paths);
			// add endpoints
			addEndpoints(graph, coMap);
			
			// add paths
			addPaths(graph, coMap);
		} catch (MalformedDataException e) {
			System.err.println(e.toString());
	        e.printStackTrace(System.err);
		}
		
		checkRep();
	}
	
	/**
	 * Helper method of buildGraph
	 * @param graph to be added with endpoints
	 * @param coMap which contains all paths
	 * @modify graph
	 * @effect add endpoints in graph, if their coordinates are not same
	 * as buildings' in graph
	 */
	private void addEndpoints(Graph<Building, Double> graph, 
									Map<Building, HashMap<Building, TreeSet<Double>>> coMap) {
		
		for(Building start : coMap.keySet()) {
			boolean endpointFound = false;
			for(Building node : graph.getNodes()) {
				if(node.getX() == start.getX() && node.getY() == start.getY()) {
					// Endpoint is one of buildings which are already in graph
					endpointFound = true;
					break;
				}
			}
			
			if(!endpointFound) 
				graph.addNode(start);
		}
		checkRep();
	}
	
	/**
	 * Helper method of buildGraph
	 * @param graph to added with edges
	 * @param coMap which contains all paths
	 * @modify graph
	 * @effect add all paths to graph
	 */
	private void addPaths(Graph<Building, Double> graph, 
									Map<Building, HashMap<Building, TreeSet<Double>>> coMap) {
	
		for(Building start : coMap.keySet()) {
			HashMap<Building, TreeSet<Double>> edges = coMap.get(start);
			
			for(Building dest : edges.keySet()) {
				double distance = edges.get(dest).first();
			
				for(Building parent : graph.getNodes()) {
					if(parent.getX() == start.getX() && parent.getY() == start.getY()) {
						
						for(Building child : graph.getNodes()) {
							if(child.getX() == dest.getX() && child.getY() == dest.getY()) {
								graph.addEdge(parent, distance, child);
								// dest is found on the graph, stop searching
								break;
							}
						}
						
						// start is found on the graph, stop searching
						break;
					}
				}
			}
		}
		checkRep();
	}	
	
	/**
	 * check if rep inv holds
	 */
	private void checkRep() {
		assert(graph != null);
		assert(buildings != null);
	}
}
