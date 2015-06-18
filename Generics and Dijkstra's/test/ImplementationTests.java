package hw7.test;

import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Suite.*;

/**
 * ImplementationTests is a test suite used to encapsulate all
 * tests specific to your implementation of this problem set.
 *
 * For instance, unit tests for your individual methods would
 * go here.
 */

@RunWith(Suite.class)
@SuiteClasses({ TestGetShortestPath.class, TestGraphBuilding.class, 
						TestPath.class, TestPathComparator.class })

public final class ImplementationTests
{
    // This class is a placeholder for the suite, so it has no members.
}

