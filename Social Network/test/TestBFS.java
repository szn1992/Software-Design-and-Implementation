package hw6.test;

import static org.junit.Assert.*;
import hw5.Graph;
import hw6.MarvelPaths;
import hw6.MarvelParser.MalformedDataException;
import hw6.MarvelPaths.Edge;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TestBFS {

	/**
	 * This class test getShortestPath in MarvelPath
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
	 * GET SHORTEST PATH *************************************************************
	 */
	
	@Test
	public void test_start_dest_same() {
		ArrayList<Edge> path = MarvelPaths.getShortestPath(er, er, g);
		assertTrue(path.isEmpty());
	}
	
	@Test
	public void test_start_dest_different1() {
		ArrayList<Edge> path = MarvelPaths.getShortestPath(er, no, g);
		assertEquals("CSE331", path.get(0).getLabel());
		assertEquals(er, path.get(0).getParent());
	}
	
	@Test
	public void test_path_size() {
		ArrayList<Edge> path = MarvelPaths.getShortestPath(no, er, g);
		assertEquals(1, path.size());
	}
	
	@Test
	public void test_start_dest_different3() {
		ArrayList<Edge> path = MarvelPaths.getShortestPath(er, pe, g);
		assertEquals("CSE331", path.get(0).getLabel());
		assertEquals(er, path.get(0).getParent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_start_null() {
		MarvelPaths.getShortestPath(null, no, g);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_dest_null() {
		MarvelPaths.getShortestPath(er, null, g);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_graph_null() {
		MarvelPaths.getShortestPath(er, no, null);
	}
}
