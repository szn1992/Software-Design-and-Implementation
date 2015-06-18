// Zhuonan Sun, HW7, CSE331 AC
// 5/22/2014
package hw7.test;

import static org.junit.Assert.*;
import hw5.Graph;
import hw6.MarvelParser.MalformedDataException;
import hw7.MarvelPaths2;
import hw7.MarvelPaths2.Path;

import org.junit.Before;
import org.junit.Test;

public class TestGetShortestPath {
	/**
	 * Test getShortestPath from MarvelPaths2
	 */
	
	Graph<String, Double> g;
	String er = "Ernst-the-Bicycling-Wizard";
	String no = "Notkin-of-the-Superhuman-Beard";
	String pe = "Perkins-the-Magical-Singing-Instructor";
	String be = "Beame-the-Dragon-Slayer";
	
	@Before
	public void setUp() throws MalformedDataException {
		g = MarvelPaths2.buildGraph("staffSuperheroes.tsv");
	}
	
	/**
	 * GET SHORTEST PATH ************************************************************
	 */
	@Test
	public void test_start_dest_same() {
		Path p = MarvelPaths2.getShortestPath(er, er, g);
		assertEquals(0.0, p.getCost(), 0.01);
		p = MarvelPaths2.getShortestPath(no, no, g);
		assertEquals(0.0, p.getCost(), 0.01);
	}
	
	@Test
	public void test_start_dest_different() {
		Path p = MarvelPaths2.getShortestPath(er, pe, g);
		assertEquals(1.0, p.getCost(), 0.01);
		p = MarvelPaths2.getShortestPath(no, er, g);
		assertEquals(0.5, p.getCost(), 0.01);
	}
	
	@Test
	public void test_path_not_found() {
		Path p = MarvelPaths2.getShortestPath(be, pe, g);
		assertEquals(null, p);
	}
	
	/**
	 * THROW EXCEPTION *******************************************************************8
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_start_null() {
		MarvelPaths2.getShortestPath(null, pe, g);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_dest_null() {
		MarvelPaths2.getShortestPath(er, null, g);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_graph_null() {
		MarvelPaths2.getShortestPath(er, pe, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_start_not_on_graph() {
		MarvelPaths2.getShortestPath("hello", pe, g);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_dest_not_on_graph() {
		MarvelPaths2.getShortestPath(er, "whatever", g);
	}
}
