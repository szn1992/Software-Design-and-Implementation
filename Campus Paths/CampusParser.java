// Zhuonan Sun, hw8, CSE331AC, 5/29/2014 
package hw8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CampusParser {
	
	/**
	 * This class is not an ADT, it is used to parse data from
	 * buildings file and paths file, to help construct CampusPaths
	 */
	
	/**
     * A checked exception class for bad data files
     */
    @SuppressWarnings("serial")
	public static class MalformedDataException extends Exception {
        public MalformedDataException() { }

        public MalformedDataException(String message) {
            super(message);
        }

        public MalformedDataException(Throwable cause) {
            super(cause);
        }

        public MalformedDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * 
     * @param filename filename of buildings file
     * @return set of building loaded from the file
     * @throws MalformedDataException if the file is not well-formed
     */
    public static Set<Building> parseBuildings(String filename) 
    										throws MalformedDataException {
    	BufferedReader reader = null;
    	 Set<Building> buildings = new HashSet<Building>();
    	try {
            reader = new BufferedReader(new FileReader(filename));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {

                // Ignore comment lines.
                if (inputLine.startsWith("#")) {
                    continue;
                }

                
                // split into 4 parts, shortname , longname, x, and y
                String[] tokens = inputLine.split("\t");
                if (tokens.length != 4) {
                    throw new MalformedDataException("Line should contain exactly three tabs: "
                                                     + inputLine);
                }

                String shortName = tokens[0];
                String longName = tokens[1];
                double x = Double.parseDouble(tokens[2]);
                double y = Double.parseDouble(tokens[3]);
                
                Building b = new Building(shortName, longName, x, y);
                // add b to set of buildings
                buildings.add(b);        
            }
       
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                    e.printStackTrace(System.err);
                }
            }
        }
    	return buildings;
    }
    
    /**
     * 
     * @param filename filename of paths file
     * @return map which maps locations to their edges
     * @throws MalformedDataException if paths file is not well-formed
     */
    public static Map<Building, HashMap<Building, TreeSet<Double>>> parsePaths(String filename)
    													throws MalformedDataException {
    	BufferedReader reader = null;
    	HashMap<Building, HashMap<Building, TreeSet<Double>>> coMap = 	// coordinate map
									new HashMap<Building, HashMap<Building, TreeSet<Double>>>();
    	try {
            reader = new BufferedReader(new FileReader(filename));
      
            String parentXY = reader.readLine();
            while (parentXY != null) {
            	// split parent x and y
                String[] xy = parentXY.split(",");
                if(xy.length != 2) {
                	throw new MalformedDataException("Line should contain exactly one comma: "
                            + parentXY);
                }
                
                double x = Double.parseDouble(xy[0]);
                double y = Double.parseDouble(xy[1]);
                Building start = new Building("", "", x, y);
                String edge = reader.readLine();
                
                while(edge != null && edge.startsWith("\t")) {
                	edge = edge.trim();
                	String[] edgeInfo = edge.split(": ");
        			
                	// split child xy and distance
                	if(edgeInfo.length != 2) {
                    	throw new MalformedDataException("Line should contain "
                    								+ "exactly one \": \": " + edge);
                    }
        			String[] childXY = edgeInfo[0].split(",");
        			
        			// split child x and y
        			if(childXY.length != 2) {
        				throw new MalformedDataException("Line should contain "
												+ "exactly one comma: " + edgeInfo[0]);
        			}
        			double childX = Double.parseDouble(childXY[0]);
        			double childY = Double.parseDouble(childXY[1]);
        			
        			Building dest = new Building("", "", childX, childY);
        			double distance = Double.parseDouble(edgeInfo[1]);
        			
        			if(!coMap.containsKey(start)) 
        				coMap.put(start, new HashMap<Building, TreeSet<Double>>());
        			if(!coMap.get(start).containsKey(dest))
        				coMap.get(start).put(dest, new TreeSet<Double>());
        			
        			coMap.get(start).get(dest).add(distance);
        			edge = reader.readLine();
                }
                
                // edge does not have indentation, edge must be the line representaing parent xy
                parentXY = edge;
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                    e.printStackTrace(System.err);
                }
            }
        }
    	return coMap;
    }
}
