package hw5.test;

import static org.junit.Assert.*;
import hw5.Edges;
import org.junit.Before;
import org.junit.Test;

public class EdgeTest {

	/** This class test class Edges 
	 */

	Edges<String, Integer> e;
	String[] nodes = {"A", "B", "C", "D"};
	int[] labels = {4, 7, 2, 5};
	
	@Before
	public void setUp() {
		e = new Edges<String, Integer>();
	}
	
	/**
	 * CONSTRUCTOR ***********************************
	 * checkRep inside Edge(Node child, String label) will also help to check if the 
	 * constructor is valid
	 */
	@Test
	public void testConstructor() {
		new Edges<String, Integer>();
		assertTrue(e.getChildren().size() == 0);
	}
	
	/**
	 * ADD EDGE ***************************************
	 * 
	 */
	@Test
	public void testAddEdge_existing_edge() {
		e.addEdge(nodes[0], labels[0]);
		e.addEdge(nodes[0], labels[0]);
		assertTrue(e.containsLabel(nodes[0], labels[0]));
		assertTrue(e.getLabels(nodes[0]).size() == 1);
		
	}
	
	@Test
	public void testAddEdge_different_edge() {
		e.addEdge(nodes[0], labels[0]);
		assertTrue(e.containsLabel(nodes[0], labels[0]));
		e.addEdge(nodes[0], labels[1]);
		assertTrue(e.containsLabel(nodes[0], labels[1]));
		assertTrue(e.getLabels(nodes[0]).size() == 2);
	}
	
	@Test
	public void testAddEdge_multiple_edge() {
		e.addEdge(nodes[0], labels[0]);
		assertTrue(e.containsLabel(nodes[0], labels[0]));
		e.addEdge(nodes[1], labels[1]);
		assertTrue(e.containsLabel(nodes[1], labels[1]));
		e.addEdge(nodes[2], labels[1]);
		assertTrue(e.containsLabel(nodes[2], labels[1]));
		e.addEdge(nodes[3], labels[1]);
		assertTrue(e.containsLabel(nodes[3], labels[1]));
		assertTrue(e.getChildren().size() == 4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddEdge_child_null() {
		e.addEdge(null, labels[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddEdge_label_null() {
		e.addEdge(nodes[0], null);
	}
	
	/**
	 * REMOVE EDGE ****************************************
	 */
	@Test
	public void testRemoveEdge_existing_edge() {
		e.addEdge(nodes[0], labels[0]);
		e.addEdge(nodes[0], labels[1]);
		e.removeEdge(nodes[0], labels[0]);
		assertFalse(e.containsLabel(nodes[0], labels[0]));
		assertTrue(e.getLabels(nodes[0]).size() == 1);
	}
	
	@Test
	public void testRemoveEdge_removechild_when_set_empty() {
		e.addEdge(nodes[0], labels[0]);
		e.removeEdge(nodes[0], labels[0]);
		assertTrue(e.getChildren().size() == 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveEdge_child_not_exist() {
		e.addEdge(nodes[0], labels[0]);
		e.removeEdge(nodes[1], labels[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveEdge_label_not_exist() {
		e.addEdge(nodes[0], labels[0]);
		e.removeEdge(nodes[0], labels[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveEdge_label_null() {
		e.addEdge(nodes[0], labels[0]);
		e.removeEdge(nodes[0], null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveEdge_child_null() {
		e.addEdge(nodes[0], labels[0]);
		e.removeEdge(null, labels[1]);
	}
	
	/**
	 * REMOVE CHILD
	 */
	@Test
	public void testRemoveChild() {
		e.addEdge(nodes[0], labels[0]);
		e.removeChild(nodes[0]);
		assertFalse(e.containsChild(nodes[0]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveChild_child_null() {
		e.removeChild(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveChild_child_not_exist() {
		e.removeChild(nodes[0]);
	}
	
	/**
	 * CONTAINS ********************************************
	 */
	@Test
	public void testContainsLabel() {
		e.addEdge(nodes[0], labels[0]);
		assertTrue(e.containsLabel(nodes[0], labels[0]));
		e.addEdge(nodes[3], labels[3]);
		assertTrue(e.containsLabel(nodes[3], labels[3]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_containsEdge_child_not_exist() {
		e.containsLabel(nodes[0], labels[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_containsEdge_label_null() {
		e.containsLabel(nodes[0], null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_containsEdge_child_null() {
		e.containsLabel(null, labels[0]);
	}
	
	@Test
	public void testContainsChild() {
		e.addEdge(nodes[0], labels[0]);
		assertTrue(e.containsChild(nodes[0]));
		e.addEdge(nodes[3], labels[3]);
		assertTrue(e.containsChild(nodes[3]));
	}
	
	/**
	 * GET ******************************************************
	 */
	@Test
	public void testGetChildren() {
		e.addEdge(nodes[0], labels[0]);
		assertTrue(e.getChildren().contains(nodes[0]));
		e.addEdge(nodes[1], labels[0]);
		assertTrue(e.getChildren().contains(nodes[1]));
	}
	
	@Test
	public void testGetLabels() {
		e.addEdge(nodes[0], labels[0]);
		e.addEdge(nodes[0], labels[1]);
		assertTrue(e.getLabels(nodes[0]).contains(labels[0]));
		assertTrue(e.getLabels(nodes[0]).contains(labels[1]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetLabels_child_null() {
		e.getLabels(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetLabels_child_not_exist() {
		e.getLabels(nodes[0]);
	}
	
	/**
	 * CLEAR ***************************************************
	 */
	@Test
	public void test_clear_empty_map() {
		e.clear();
		assertTrue(e.getChildren().size() == 0);
	}
	
	@Test
	public void test_clear_non_empty_map() {
		e.addEdge(nodes[0], labels[0]);
		e.addEdge(nodes[1], labels[1]);
		e.addEdge(nodes[2], labels[2]);
		e.addEdge(nodes[3], labels[3]);
		e.clear();
		assertTrue(e.getChildren().size() == 0);
	}
}
