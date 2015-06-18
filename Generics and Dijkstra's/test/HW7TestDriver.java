// Zhuonan Sun, HW7, CSE331 AC
// 5/22/2014
package hw7.test;

import hw5.Graph;
import hw6.MarvelParser.MalformedDataException;
import hw7.MarvelPaths2;
import hw7.MarvelPaths2.Path;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;


/**
 * This class implements a testing driver which reads test scripts
 * from files for your graph ADT and improved MarvelPaths application
 * using Dijkstra's algorithm.
 **/
public class HW7TestDriver {


    public static void main(String args[]) {
    	  try {
              if (args.length > 1) {
                  printUsage();
                  return;
              }

              HW7TestDriver td;

              if (args.length == 0) {
                  td = new HW7TestDriver(new InputStreamReader(System.in),
                                         new OutputStreamWriter(System.out));
              } else {

                  String fileName = args[0];
                  File tests = new File (fileName);

                  if (tests.exists() || tests.canRead()) {
                      td = new HW7TestDriver(new FileReader(tests),
                                             new OutputStreamWriter(System.out));
                  } else {
                      System.err.println("Cannot read from " + tests.toString());
                      printUsage();
                      return;
                  }
              }

              td.runTests();

          } catch (IOException e) {
              System.err.println(e.toString());
              e.printStackTrace(System.err);
          }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("to read from a file: java hw7.test.HW7TestDriver <name of input script>");
        System.err.println("to read from standard in: java hw6.test.HW7TestDriver");
    }

    /** String -> Graph: maps the names of graphs to the actual graph **/
    private final Map<String, Graph<String, Double>> graphs = new HashMap<String, Graph<String, Double>>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @requires r != null && w != null
     *
     * @effects Creates a new HW5TestDriver which reads command from
     * <tt>r</tt> and writes results to <tt>w</tt>.
     **/
    public HW7TestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @effects Executes the commands read from the input and writes results to the output
     * @throws IOException if the input or output sources encounter an IOException
     **/
    public void runTests()
        throws IOException
    {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            }
            else
            {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            if (command.equals("CreateGraph")) {
                createGraph(arguments);
            } else if (command.equals("AddNode")) {
                addNode(arguments);
            } else if (command.equals("AddEdge")) {
                addEdge(arguments);
            } else if (command.equals("ListNodes")) {
                listNodes(arguments);
            } else if (command.equals("ListChildren")) {
                listChildren(arguments);
            } else if (command.equals("LoadGraph")) {
            	loadGraph(arguments);
            } else if (command.equals("FindPath")) {
            	findPath(arguments);
            } else {
                output.println("Unrecognized command: " + command);
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
    	graphs.put(graphName, new Graph<String, Double>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to addNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> g = graphs.get(graphName);
        g.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to addEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        double edgeLabel = Double.parseDouble(arguments.get(3));

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
            double edgeLabel) {
 

        Graph<String, Double> g = graphs.get(graphName);
        g.addEdge(parentName, edgeLabel, childName);
        output.println("added edge " + String.format("%.3f", edgeLabel) + " from " + parentName + " to "
        										+ childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to listNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
    	Graph<String, Double> g = graphs.get(graphName);
    	TreeSet<String> sorted = new TreeSet<String>(g.getNodes());
    	output.print(graphName + " contains:");
    	for(String s : sorted)
    		output.print(" " + s);
    	output.println();
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to listChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String, Double> g = graphs.get(graphName);
       
        TreeSet<String> children = new TreeSet<String>(g.getChildren(parentName));
        output.print("the children of " + parentName + " in " + graphName + " are:");
        for(String child : children) {
        	TreeSet<Double> labels = new TreeSet<Double>(g.getLabels(parentName, child));
        	for(Double label : labels) {
        		output.print(" " + child + "(" + String.format("%.3f", label) + ")");
        	}
        }
        output.println();
    }
    
    private void loadGraph(List<String> arguments) throws MalformedDataException {
    	if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to loadGraph: " + arguments);
        }
   
    	String graphName = arguments.get(0);
    	String fileName = arguments.get(1);
    	loadGraph(graphName, fileName);
    }
    
    private void loadGraph(String graphName, String fileName) throws MalformedDataException {
    	graphs.put(graphName, MarvelPaths2.buildGraph(fileName));
    	output.println("loaded graph " + graphName);
    }
    
    private void findPath(List<String> arguments) {
    	if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to findPath: " + arguments);
        }
    
    	String graphName = arguments.get(0);
    	String node_1 = arguments.get(1);
    	String node_n = arguments.get(2);
    	
    	findPath(graphName, node_1, node_n);
    }
    
    private void findPath(String graphName, String node_1, String node_n) {
    	Graph<String, Double> g = graphs.get(graphName);
    	node_1 = node_1.replace('_', ' ');
    	node_n = node_n.replace('_', ' ');
    	if(!g.containsNode(node_1) || !g.containsNode(node_n)) {
    		if(!g.containsNode(node_1)) 
    			output.println("unknown character " + node_1);
    		if(!g.containsNode(node_n))
    			output.println("unknown character " + node_n);
    		return;
    	}  else {
    		Path path = MarvelPaths2.getShortestPath(node_1, node_n, g);
    		output.println("path from " + node_1 + " to " + node_n + ":");
    
	
	    	if(path == null) {
	    		output.println("no path found");
	    	} else {
	    		List<String> pathList = path.getPath();
	    		for(int i = 0; i < pathList.size() - 1; i++) {
	    			String start = pathList.get(i);
	    			String dest = pathList.get(i + 1);
	    			TreeSet<Double> sorted = new TreeSet<Double>(g.getLabels(start, dest));
	    			String cost = String.format("%.3f", sorted.first());
	    			output.println(start + " to " + dest + " with weight " + cost);
	    		}
	    		output.println("total cost: " + String.format("%.3f", path.getCost()));
	    	}
    	}
    }
    
    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }
        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
