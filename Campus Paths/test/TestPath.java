package hw8.test;

import static org.junit.Assert.*;

import org.junit.Test;

import hw8.Path;

import java.util.ArrayList;

import org.junit.Before;

public class TestPath {
	
	/**
	 * Test Inner class Path
	 */
	Path<String> p;
	@Before
	public void setUp() {
		p = new Path<String>();
	}
	
	/**
	 * Test Constructor **************************************************
	 */
	@Test
	public void test_constructor_no_argument() {
		new Path<String>();
	}
	
	@Test
	public void test_constructor_two_arguments() {
		new Path<String>(new ArrayList<String>(), 0.0);
	}
	/**
	 * ADD *****************************************************************
	 */
	@Test
	public void test_add_cost_valid() {
		p = p.add("A", 0.0);
		p = p.add("B", 1.0);
		assertEquals(1.0, p.getCost(), 0.01);
		p = p.add("C", 4.0);
		assertEquals(5.0, p.getCost(), 0.01);
	}
	
	@Test
	public void test_add_path_valid() {
		p = p.add("A", 0.0);
		p = p.add("B", 1.0);
		assertTrue(p.getPath().contains("A"));
		assertTrue(p.getPath().contains("B"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_add_dest_null() {
		p.add(null, 0.0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_add_cost_negative() {
		p.add("a", -2.0);
	}
	
	/**
	 * GET ******************************************************************
	 */
	@Test
	public void test_getCost() {
		assertEquals(0.0, p.getCost(), 0.01);
		p = p.add("B", 1.0);
		assertEquals(1.0, p.getCost(), 0.01);
	}
	
	@Test
	public void test_getPath() {
		assertTrue(p.getPath().isEmpty());
		p = p.add("B", 1.0);
		assertTrue(p.getPath().contains("B"));
	}
}

