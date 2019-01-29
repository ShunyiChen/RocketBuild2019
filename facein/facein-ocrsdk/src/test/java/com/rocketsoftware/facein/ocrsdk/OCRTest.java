package com.rocketsoftware.facein.ocrsdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * @author chens
 *
 */
public class OCRTest  extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OCRTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( OCRTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testPassportNo()
    {
    	long a = System.currentTimeMillis();
    	String passportNo = "";
		OCR ocrsdk = new OCR();
		File imgFile = new File("C:\\Users\\chens\\Downloads\\tess4j\\img\\QQ截图20190129151231.jpg");
		FileInputStream fis;
		try {
			fis = new FileInputStream(imgFile);
			passportNo = ocrsdk.passportNo(fis);
			long b = System.currentTimeMillis();
			
			System.out.println("passportNo:"+passportNo);
			System.out.println("Spent:"+(b-a));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        assertTrue(passportNo.equals("E61169299"));
    }
}
