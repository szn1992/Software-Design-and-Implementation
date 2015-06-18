package hw8.test;

import static org.junit.Assert.*;
import hw8.Building;
import hw8.CampusParser;
import hw8.CampusParser.MalformedDataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class TestParsing {
	
	/**
	 * This class tests parsing functions in CampusParser
	 */
	
	Set<Building> s; 
	Map<Building, HashMap<Building, TreeSet<Double>>> m;
	Building bag = new Building("BAG", "Bagley Hall (East Entrance)", 1914.5103, 1709.8816);
	Building thai = new Building("T65", "Thai 65", 1370.6408, 807.35188);
	Building ima = new Building("IMA", "Intramural Activities Building", 2722.3352, 1710.2859);
	Building end1 = new Building("", "", 1903.7201, 1952.4322);
	Building end2 = new Building("", "", 1987.3674, 1721.6673);
	Building end3 = new Building("", "", 1906.1864, 1939.0633);
	Building end4 = new Building("", "", 2003.4508, 1722.6567);
	
	@Before
	public void setUp() throws MalformedDataException {
		s = CampusParser.parseBuildings("src/hw8/data/campus_buildings.dat");
		m = CampusParser.parsePaths("src/hw8/data/campus_paths.dat");
	}
	
	/**
	 * parsing building ****************************************************************
	 */
	
	@Test
	public void test_parseBuildings_elements() {
		assertTrue(s.contains(bag));
		assertTrue(s.contains(thai));
	}
	
	@Test
	public void test_parseBuildings_size() {
		assertEquals(51, s.size());
	}
	
	/**
	 * parsing paths *********************************************************************
	 */
	@Test
	public void test_parsePaths_keys() {
		assertTrue(m.containsKey(end1));
		assertTrue(m.containsKey(end2));
	}

	@Test
	public void test_parsePaths_values() {
		assertEquals(26.583482327919597, m.get(end1).get(end3).first(), 0.00000000001);
		assertEquals(33.60115442254878, m.get(end2).get(end4).first(), 0.00000000001);
	}

}
