// Zhuonan Sun, HW7, CSE331 AC
// 5/22/2014
package hw7.test;

import static org.junit.Assert.*;
import hw7.MarvelPaths2.Path;
import hw7.MarvelPaths2.PathComparator;

import org.junit.Before;
import org.junit.Test;

public class TestPathComparator {
	
	// Test INNER CLASS PathComparator
	Path p1, p2;
	PathComparator c;
	@Before
	public void setUp() {
		c = new PathComparator();
		p1 = new Path();
		p2 = new Path();
		p1 = p1.add("A", 4.7);
		p2 = p2.add("V", 90.0);
	}
	
	/**
	 * COMPARE ************************************************************
	 */
	@Test
	public void test_compare_equal() {
		assertEquals(0, c.compare(p1, p1));
		p1 = p1.add("B", 85.3);
		assertEquals(0, c.compare(p1, p2));
	}
	
	@Test
	public void test_compare_less() {
		assertEquals(-1, c.compare(p1, p2));
	}
	
	@Test
	public void test_compare_more() {
		assertEquals(1, c.compare(p2, p1));
	}
	
	/**
	 * Exception ********************************************************
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_p1_null() {
		c.compare(null, p2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_p2_null() {
		c.compare(p1, null);
	}
}
