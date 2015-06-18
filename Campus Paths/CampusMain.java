// Zhuonan Sun, hw8, CSE331AC, 5/29/2014 
package hw8;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/*
 * This class represents the UI and controller of a campus path finding tool
 * it loads the data of location information from two files, and prompts the user
 * to give a command. It can help the user to list all the buildings on campus, or
 * find the shortest path between two buildings
 */
public class CampusMain {
	/**
	 * This class is not an ADT, so there is no AF nor RI 
	 */
	
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		CampusPaths campus = new CampusPaths("campus_buildings.dat", "campus_paths.dat");
		TreeMap<String, String> nameMap = campus.sortBuildingNames();
		
		menu();
		boolean commentOrBlank = false;
		while(true) {
			if(!commentOrBlank) {
				System.out.println();
				System.out.print("Enter an option ('m' to see the menu): ");
			} else {
				commentOrBlank = false;
			}
			
			String input = console.nextLine();
			
			if(input.equals("r")) {		// find path
				printPath(campus, console);
			
			} else if(input.equals("b")) {	// list buildings
				printBuildings(nameMap);
						
			} else if(input.equals("m")) { // print menu
				menu();
				
			} else if(input.equals("q")) { // quit
				break;	
			
			} else if(input.startsWith("#") || input.trim().isEmpty()) { // echo 
															// comment or blank lines
				System.out.println(input);		
				commentOrBlank = true;
			} else { 
				System.out.println("Unknown option");
			}	
		}
		console.close();
	}
	
	/**
	 * prints all buildings on campus
	 * @param nameMap map maps short names to long names of buildings
	 */
	public static void printBuildings(Map<String, String> nameMap) {
		System.out.println("Buildings:");
		for(String abbr : nameMap.keySet()) {
			System.out.println("\t" + abbr + ": " + nameMap.get(abbr));
		}
	}
	
	/**
	 * prompts for two names of buildings
	 * prints the shortest path between them
	 * @param campus campus stores all information of locations and paths
	 * @param console console prompts the user
	 */
	public static void printPath(CampusPaths campus, Scanner console) {
		System.out.print("Abbreviated name of starting building: ");
		String start = console.nextLine();
		System.out.print("Abbreviated name of ending building: ");
		String dest = console.nextLine();
		
		// starting building
		Building startB = campus.findBuilding(start);
		
		// destination building
		Building destB = campus.findBuilding(dest);
		
		if(startB != null && destB != null) {
			System.out.println("Path from " + startB.getLongName() + 
									" to " + destB.getLongName() + ":");
			
			Path<Building> path = campus.getShortestPath(start, dest);
    		List<Building> pathList = path.getPath();
    		for(int i = 0; i < pathList.size() - 1; i++) {
    			Building current = pathList.get(i);
    			Building nextSpot = pathList.get(i + 1);
    			int x = (int) Math.round(nextSpot.getX());
    			int y = (int) Math.round(nextSpot.getY());
    			
    			int distance = (int) Math.round(campus.getDistance(current, nextSpot));
    			String direction =  campus.getDirection(-Math.atan2(nextSpot.getY() - current.getY(),
    														nextSpot.getX() - current.getX()));
    			
    			System.out.println("\tWalk " + distance + " feet " + 
    										direction + " to (" + x + ", " + y +")");
    		}
    		System.out.println("Total distance: " + (int) Math.round(path.getCost()) + " feet");
	    	
			
		} else {
			if(startB == null) 
				System.out.println("Unknown building: " + start);
			if(destB == null)
				System.out.println("Unknown building: " + dest);
		}
	}
	
	/**
	 * prints the menu
	 */
	public static void menu() {
		System.out.println("Menu:");
		System.out.println("\t" + "r to find a route");
		System.out.println("\t" + "b to see a list of all buildings");
		System.out.println("\t" + "q to quit");
	}
}
