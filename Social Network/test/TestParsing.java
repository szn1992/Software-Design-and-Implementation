package hw6.test;

import static org.junit.Assert.*;
import hw6.MarvelParser;
import hw6.MarvelParser.MalformedDataException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TestParsing {

	/**
	 * This class tests Parsing of MarvelParser, which is used in buildGraph of MarvelPath
	 */
	Set<String> characters;
	Map<String, List<String>> books;
	
	@Before
	public void setUp() throws MalformedDataException {
		characters = new HashSet<String>();
		books= new HashMap<String, List<String>>();
		MarvelParser.parseData("src/hw6/data/staffSuperheroes.tsv", characters, books);
	}
	
	/**
	 * CHARACTERS *****************************************************
	 */
	@Test
	public void test_characters() {
		assertTrue(characters.contains("Ernst-the-Bicycling-Wizard"));
		assertTrue(characters.contains("Notkin-of-the-Superhuman-Beard"));
	}
	
	@Test
	public void test_characters_size() {
		assertEquals(3, characters.size());
	}
	
	/**
	 * BOOKS KEYS**********************************************************
	 */
	@Test
	public void test_books_keys() {
		assertTrue(books.containsKey("CSE331"));
		assertTrue(books.containsKey("CSE403"));
	}
	
	@Test
	public void test_books_keys_size() {
		assertEquals(3, books.keySet().size());
	}
	
	/**
	 * BOOKS VALUES ******************************************************
	 */
	@Test
	public void test_books_values() {
		assertTrue(books.get("CSE331").contains("Ernst-the-Bicycling-Wizard"));
		assertTrue(books.get("CSE331").contains("Notkin-of-the-Superhuman-Beard"));
		
			
		assertTrue(books.containsKey("CSE403"));
		assertTrue(books.get("CSE403").contains("Ernst-the-Bicycling-Wizard"));
		assertTrue(books.get("CSE403").contains("Notkin-of-the-Superhuman-Beard"));
	
	
		assertTrue(books.containsKey("CSE401"));
		assertTrue(books.get("CSE401").contains("Perkins-the-Magical-Singing-Instructor"));
		assertEquals(1, books.get("CSE401").size());
	}
	
	@Test
	public void test_books_values2() {
		assertTrue(books.containsKey("CSE401"));
		assertTrue(books.get("CSE401").contains("Perkins-the-Magical-Singing-Instructor"));
	}
	
	@Test
	public void test_books_values_size() {
		assertEquals(2, books.get("CSE403").size());
		assertEquals(1, books.get("CSE401").size());
	}
	
	/**
	 * IllegalArgumentException ********************************************************
	 */
	
	@Test(expected = IllegalArgumentException.class)
	public void test_characters_null() throws MalformedDataException{
		MarvelParser.parseData("src\\hw6\\data\\staffSuperHeroes.tsv", null, books);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_books_null() throws MalformedDataException{
		MarvelParser.parseData("src\\hw6\\data\\staffSuperHeroes.tsv", characters, null);
	}
	
	/**
	 *MalFormedDataException **********************************************************
	 */
	@Test(expected = MalformedDataException.class)
	public void test_badform() throws MalformedDataException {
		MarvelParser.parseData("src/hw6/data/badform.tsv", characters, books);
	}
	
	@Test(expected = MalformedDataException.class)
	public void test_no_quotation() throws MalformedDataException {
		MarvelParser.parseData("src/hw6/data/noQuotation.tsv", characters, books);
	}
	
	@Test(expected = MalformedDataException.class)
	public void test_no_tab() throws MalformedDataException {
		MarvelParser.parseData("src/hw6/data/noTab.tsv", characters, books);
	}
}
