package com.rocketsoftware.facein.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * @author chens
 *
 */
public class CoreTest  extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CoreTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CoreTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testCompareFaces()
    {
    	Core scriptPython = new Core();
		String sameperson = scriptPython.compareFaces("images/daniel-radcliffe_2.jpg", "images/daniel-radcliffe_4.jpg");
		System.out.println(sameperson);
        assertTrue(sameperson.equals("same person"));
    }
}
