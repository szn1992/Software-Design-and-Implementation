// Zhuonan Sun, HW7, CSE331 AC
// 5/22/2014
package hw7.test;

import static org.junit.Assert.*;
import hw5.Graph;
import hw6.MarvelParser.MalformedDataException;
import hw7.MarvelPaths2;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TestGraphBuilding {
	
	/**
	 * Test buildGraph from MarvelPaths2
	 */
	
	Graph<String, Double> g;
	Set<String> s;
	String er = "Ernst-the-Bicycling-Wizard";
	String no = "Notkin-of-the-Superhuman-Beard";
	String pe = "Perkins-the-Magical-Singing-Instructor";
	
	@Before
	public void setUp() throws MalformedDataException {
		g = MarvelPaths2.buildGraph("staffSuperheroes.tsv");
		s = g.getNodes();
	}
	
	/**
	 * TEST NODES ***********************************************************
	 */
	@Test
	public void test_graph_contains_nodes() {
		assertTrue(g.containsNode(er));
		assertTrue(g.containsNode(no));
	}
	
	@Test
	public void test_nodes_numbers() {
		assertEquals(4, g.getNodes().size());
	}

	/**
	 * Test EDGES *************************************************************
	 */
	@Test
	public void test_edges_weight() {
		assertTrue(g.containsEdge(er, 1.0, pe));
		assertTrue(g.containsEdge(no, 0.5, er));
	}
	
	@Test
	public void test_edges_symmetric() {
		assertTrue(g.containsEdge(no, 0.5, er));
		assertTrue(g.containsEdge(er, 0.5, no));
	}
	
	@Test
	public void test_edges_amount() {
		assertEquals(2, g.getChildren(er).size());
		assertEquals(1, g.getLabels(er, no).size());
	}
}