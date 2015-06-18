package hw6.test;

import static org.junit.Assert.*;

import java.util.Set;

import hw5.Graph;
import hw6.MarvelParser.MalformedDataException;
import hw6.MarvelPaths;

import org.junit.Before;
import org.junit.Test;

public class TestGraphBuilding {
	
	/**
	 * This class tests buildGraph in MarvelPath
	 */
	
	Graph<String, String> g;
	Set<String> s;
	String er = "Ernst-the-Bicycling-Wizard";
	String no = "Notkin-of-the-Superhuman-Beard";
	String pe = "Perkins-the-Magical-Singing-Instructor";
	
	@Before
	public void setUp() throws MalformedDataException {
		g = MarvelPaths.buildGraph("staffSuperheroes.tsv");
		s = g.getNodes();
	}
	
	/**
	 * NODES *************************************************************
	 */
	@Test
	public void test_nodes() {
		assertTrue(s.contains(er));
		assertTrue(s.contains(no));
	}
	
	@Test
	public void test_nodes_size() {
		assertEquals(3, s.size());
	}
	
	
	/**
	 * EDGES ***********************************************************
	 */
	@Test
	public void test_edges1() {		
		assertTrue(g.containsEdge(er, "CSE331", no));
		assertTrue(g.containsEdge(er, "CSE403", no));
	}
	
	@Test
	public void test_edges2() {
		assertTrue(g.containsEdge(pe, "CSE331", er));
		assertTrue(g.containsEdge(pe, "CSE331", no));
	}
	
	@Test
	public void test_labels_size() {
		assertEquals(2, g.getLabels(no, er).size());
		assertEquals(1, g.getLabels(no, pe).size());
	}
	
	@Test
	public void test_children_size() {
		assertEquals(2, g.getChildren(no).size());
	}
}
