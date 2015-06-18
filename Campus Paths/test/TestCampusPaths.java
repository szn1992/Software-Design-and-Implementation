package hw8.test;

import static org.junit.Assert.*;
import hw8.Building;
import hw8.CampusPaths;
import hw8.CampusParser.MalformedDataException;

import org.junit.Before;
import org.junit.Test;

public class TestCampusPaths {
	
	/**
	 * This class tests the model UWpaths
	 */
	CampusPaths uw;
	
	Building bag = new Building("BAG", "Bagley Hall (East Entrance)", 1914.5103, 1709.8816);
	Building ima = new Building("IMA", "Intramural Activities Building", 2722.3352, 1710.2859);
	Building end1 = new Building("", "", 1903.7201, 1952.4322);
	Building end3 = new Building("", "", 1906.1864, 1939.0633);

	@Before
	public void setUp() throws MalformedDataException {
		uw = new CampusPaths("campus_buildings.dat", "campus_paths.dat");
	}
	
	/**
	 * NODES ******************************************************
	 */
	@Test
	public void test_buildings() {
		assertTrue(uw.getBuildings().contains(bag));
		assertTrue(uw.getBuildings().contains(ima));
	}
	
	@Test
	public void test_buildsings_size() {
		assertEquals(51, uw.getBuildings().size());
	}
	
	/**
	 * GET SHORTEST PATH *******************************************************
	 */
	@Test
	public void test_paths() {
		assertEquals(2215, (int)Math.round(uw.getShortestPath("BAG", "IMA").getCost()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_start_null() {
		uw.getShortestPath(null, "IMA");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_dest_null() {
		uw.getShortestPath("IMA", null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_start_exist_no() {
		uw.getShortestPath("BAG", "o");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_dest_exist_no() {
		uw.getShortestPath("B", "IMA");
	}
	/**
	 * GET DISTANCE **********************************************************
	 */
	@Test
	public void test_getDistance() {
		assertEquals(26.583482327919597, uw.getDistance(end1, end3), 0.000000001);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_start_building_null() {
		uw.getDistance(null, end3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_destination_null() {
		uw.getDistance(end1, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_start_building_does_not_exist() {
		uw.getDistance(new Building("", "", 1, 2), end3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_destination_does_not_exist() {
		uw.getDistance(end1, new Building("", "", 1, 2));
	}
	
	/**
	 * FIND BUILDING *********************************************************
	 */
	@Test
	public void test_find_building() {
		assertEquals(bag, uw.findBuilding("BAG"));
		assertTrue(null == uw.findBuilding("BG"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_find_building_abbr_null() {
		uw.findBuilding(null);
	}
}
