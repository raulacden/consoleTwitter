package rsp.lookiero.twitter.utils;


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
    
	
	@Test
    public void testPrintLine()
    {
		String printText = "Testing printLine";
		consoleIO.printLine(printText);
		Assertions.assertEquals("[Lookiero] - Testing printLine", testOut.toString().trim());
    }

}
