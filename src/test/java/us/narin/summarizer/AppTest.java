package us.narin.summarizer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Unit test for simple Summarizer.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
        try {
            Summarizer summarizer = new Summarizer(new Scanner(new File("./test.txt")).useDelimiter("\\Z").next());
            System.out.println(summarizer.summarize());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }
}
