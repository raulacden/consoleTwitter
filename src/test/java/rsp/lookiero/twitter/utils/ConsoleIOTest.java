package rsp.lookiero.twitter.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ConsoleIOTest {
	
	ConsoleIO consoleIO;
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    
	private ByteArrayInputStream testIn;
	private ByteArrayOutputStream testOut = new ByteArrayOutputStream();

    @BeforeEach                               
    public void setUp() {
    	consoleIO = new ConsoleIO();
    	System.setOut(new PrintStream(testOut));
    }
    
    @AfterEach
    public void tearDown() {
    	System.setIn(systemIn);
        System.setOut(systemOut);
    }
    
    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
    
    
    @Test
    public void testScannerLine() {
    	final String testString = "Testing scanner line";
    	provideInput(testString);    	
    	
    	Assertions.assertEquals(testString, consoleIO.readLine());
    }
	
	@Test
    public void testPrintLine()
    {
		String printText = "Testing printLine";
		consoleIO.printLine(printText);
		Assertions.assertEquals("[Lookiero] - Testing printLine", testOut.toString().trim());
    }

}
