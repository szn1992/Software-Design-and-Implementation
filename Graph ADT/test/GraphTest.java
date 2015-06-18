package hw5.test;

import static org.junit.Assert.*;

import hw5.Graph;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {
	
	Graph<String, Integer> g;
	String[] nodes = {"A", "B", "C", "D"};
	Integer[] labels = {4, 7, 2, 5};
	
	@Before
	public void setUp() {
		g = new Graph<String, Integer>();
	}
	
	/**
	 * Constructor *****************************************
	 * checkRep will do the tests on constructors
	 */
	@Test
	public void testConstructor_no_argument() {
		new Graph<String, Integer>();
		assertTrue(g.getNodes().size() == 0);
	}

	/**
	 * ADD NODE *********************************************
	 */
	@Test
	public void test_addNode_existingNode() {
		g.addNode(nodes[0]);
		g.addNode(nodes[0]);
		assertTrue(g.containsNode(nodes[0]));
		assertTrue(g.getNodes().size() == 1);
	}
	
	@Test
	public void test_addNode_differentNode() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		assertTrue(g.containsNode(nodes[0]));
		assertTrue(g.containsNode(nodes[1]));
		assertTrue(g.getNodes().size() == 2);
	}

	@Test
	public void test_addNode_multipleNodes() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addNode(nodes[2]);
		g.addNode(nodes[2]);
		g.addNode(nodes[3]);
		assertTrue(g.containsNode(nodes[0]));
		assertTrue(g.containsNode(nodes[1]));
		assertTrue(g.containsNode(nodes[2]));
		assertTrue(g.containsNode(nodes[3]));
		assertTrue(g.getNodes().size() == 4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_addNode_parent_null() {
		g.addNode(null);
	}
	
	/**
	 * ADD EDGE *********************************************
	 */
	@Test
	public void test_addEdge_existingEdge() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		assertTrue(g.containsEdge(nodes[0], labels[0], nodes[1]));
		assertTrue(g.getChildren(nodes[0]).size() == 1);
	}
	
	@Test
	public void test_addEdge_differentEdge() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		g.addEdge(nodes[0], labels[1], nodes[1]);
		assertTrue(g.containsEdge(nodes[0], labels[0], nodes[1]));
		assertTrue(g.containsEdge(nodes[0], labels[1], nodes[1]));
		assertTrue(g.getChildren(nodes[0]).size() == 1);
		assertTrue(g.getLabels(nodes[0], nodes[1]).size() == 2);
	}
	
	@Test
	public void test_addEdge_multipleEdges() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addNode(nodes[2]);
		g.addNode(nodes[3]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[2]);
		g.addEdge(nodes[0], labels[0], nodes[3]);
		g.addEdge(nodes[0], labels[3], nodes[3]);
		assertTrue(g.getChildren(nodes[0]).size() == 4);
		assertTrue(g.getLabels(nodes[0], nodes[3]).size() == 2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_addEdge_throw_exception_parent_not_exist() {
		g.addEdge(nodes[0], labels[0], nodes[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_addEdge_throw_exception_child_not_exist() {
		g.addNode(nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_addEdge_throw_exception_child_null() {
		g.addEdge(nodes[0], labels[0], null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_addEdge_throw_exception_parent_null() {
		g.addEdge(null, labels[0], nodes[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_addEdge_throw_exception_label_null() {
		g.addEdge(nodes[0], null, nodes[0]);
	}
	
	
	/**
	 * REMOVE NODE *******************************************
	 */
	@Test
	public void test_removeNode_existingNode() {
		g.addNode(nodes[0]);
		g.removeNode(nodes[0]);
		assertFalse(g.containsNode(nodes[0]));
	}
	
	@Test
	public void test_removeNode_remove_all_edges_to_the_node() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addNode(nodes[2]);
		g.addNode(nodes[3]);
		g.addEdge(nodes[0], labels[0], nodes[3]);
		g.addEdge(nodes[1], labels[1], nodes[3]);
		g.addEdge(nodes[2], labels[2], nodes[3]);
		g.addEdge(nodes[3], labels[3], nodes[3]);
		g.removeNode(nodes[3]);
		assertTrue(g.getNodes().size() == 3);
		assertTrue(g.getChildren(nodes[0]).size() == 0);
		assertTrue(g.getChildren(nodes[1]).size() == 0);
		assertTrue(g.getChildren(nodes[2]).size() == 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_removeNode_node_not_exist() {
		g.removeNode(nodes[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_removeNode_null_argument() {
		g.removeNode(null);
	}
	
	@Test
	public void test_removeNode_multipleNodes() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addNode(nodes[2]);
		g.addNode(nodes[3]);
		g.removeNode(nodes[0]);
		g.removeNode(nodes[1]);
		g.removeNode(nodes[2]);
		g.removeNode(nodes[3]);
		assertTrue(g.getNodes().size() == 0);
	}
	
	/**
	 * REMOVE EDGE *********************************************
	 */
	@Test
	public void test_removeEdge_existingEdge() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		g.removeEdge(nodes[0], labels[0], nodes[1]);
		assertFalse(g.containsEdge(nodes[0], labels[0], nodes[1]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_removeEdge_parent_not_exist() {
		g.addEdge(nodes[0], labels[0], nodes[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_removeEdge_child_not_exist() {
		g.addNode(nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_removeEdge_parent_null() {
		g.addEdge(null, labels[0], nodes[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_removeEdge_label_null() {
		g.addEdge(nodes[0], null, nodes[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_removeEdge_child_null() {
		g.addEdge(nodes[0], labels[0], null);
	}
	
	@Test
	public void test_removeEdge_multipleEdges() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addNode(nodes[2]);
		g.addNode(nodes[3]);
		g.addEdge(nodes[1], labels[0], nodes[2]);
		g.addEdge(nodes[2], labels[2], nodes[3]);
		g.addEdge(nodes[0], labels[1], nodes[3]);
		g.addEdge(nodes[2], labels[2], nodes[2]);
		g.removeEdge(nodes[1], labels[0], nodes[2]);
		g.removeEdge(nodes[2], labels[2], nodes[3]);
		g.removeEdge(nodes[0], labels[1], nodes[3]);
		g.removeEdge(nodes[2], labels[2], nodes[2]);
		assertTrue(g.getChildren(nodes[0]).isEmpty());
		assertTrue(g.getChildren(nodes[1]).isEmpty());
		assertTrue(g.getChildren(nodes[2]).isEmpty());
		assertTrue(g.getChildren(nodes[3]).isEmpty());
	}
	
	/**
	 * REMOVE ALL EDGES ******************************************
	 */
	@Test
	public void test_removeAllEdges_parent_exists() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addNode(nodes[2]);
		g.addNode(nodes[3]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.addEdge(nodes[0], labels[2], nodes[1]);
		g.addEdge(nodes[0], labels[1], nodes[2]);
		g.addEdge(nodes[0], labels[2], nodes[3]);
		g.removeAllEdges(nodes[0]);
		assertTrue(g.getChildren(nodes[0]).isEmpty());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_removeAllEdges_parent_not_exists() {
		g.removeAllEdges(nodes[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_removeAllEdges_parent_null() {
		g.removeAllEdges(null);
	}
	
	@Test
	public void test_removeAllEdges_parent_has_no_edges() {
		g.addNode(nodes[0]);
		g.removeAllEdges(nodes[0]);
		assertTrue(g.getChildren(nodes[0]).isEmpty());
	}
	
	/**
	 * CLEAR *****************************************************
	 */
	@Test
	public void test_clear_empty_map() {
		g.clear();
		assertTrue(g.getNodes().size() == 0);
	}
	
	@Test
	public void test_clear_non_empty_map() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addNode(nodes[2]);
		g.addNode(nodes[3]);
		g.clear();
		assertTrue(g.getNodes().size() == 0);
	}
	
	/**
	 * CONTAINS *********************************************
	 */
	public void test_containsNode() {
		g.addNode(nodes[0]);
		assertTrue(g.containsNode(nodes[0]));
		assertFalse(g.containsNode(nodes[1]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_containsNode_null_parent() {
		g.containsNode(null);
	}
	
	@Test
	public void test_containsEdge() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		assertTrue(g.containsEdge(nodes[0], labels[0], nodes[1]));
		assertFalse(g.containsEdge(nodes[1], labels[0], nodes[1]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_containsEdge_parent_not_exist() {
		g.addNode(nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.addEdge(nodes[1], labels[0], nodes[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_containsEdge_child_not_exist() {
		g.addNode(nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.containsEdge(nodes[0], labels[0], nodes[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_containsEdge_null_parent() {
		g.addNode(nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.containsEdge(null, labels[0], nodes[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_containsEdge_null_label() {
		g.addNode(nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.containsEdge(nodes[0], null, nodes[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_containsEdge_null_child() {
		g.addNode(nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.containsEdge(nodes[0], labels[0], null);
	}
	
	/**
	 * GET ***************************************************
	 */
	@Test
	public void test_getNodes() {
		g.addNode(nodes[0]);
		g.getNodes().contains(nodes[0]);
		g.addNode(nodes[1]);
		g.getNodes().contains(nodes[1]);
	}
	
	@Test
	public void test_getChildren() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		assertTrue(g.getChildren(nodes[0]).size() == 2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_getChildren_parent_null() {
		g.getChildren(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_getChildren_parent_not_exist() {
		g.addNode(nodes[1]);
		g.getChildren(nodes[0]);
	}
	
	@Test
	public void test_getLabels() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		assertTrue(g.getLabels(nodes[0], nodes[0]).size() == 1);
		assertTrue(g.getLabels(nodes[0], nodes[1]).size() == 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_getLabels_parent_null() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		g.getLabels(null, nodes[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_getLabels_child_null() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		g.getLabels(nodes[0], null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_getLabels_parent_not_exist() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		g.getLabels(nodes[3], nodes[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_getLabels_child_not_exist() {
		g.addNode(nodes[0]);
		g.addNode(nodes[1]);
		g.addEdge(nodes[0], labels[0], nodes[0]);
		g.addEdge(nodes[0], labels[0], nodes[1]);
		g.getLabels(nodes[0], nodes[3]);
	}
}
