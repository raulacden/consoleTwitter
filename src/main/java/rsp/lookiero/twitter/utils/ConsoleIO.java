package rsp.lookiero.twitter.utils;

import java.util.Scanner;

public class ConsoleIO {
	
	private Scanner in = new Scanner(System.in);	
	
	public String readLine() {		
		return in.nextLine();		
	}
	
	public void closeLine() {
		in.close();		
	}
	
	public void printLine(String text) {
		System.out.println("[Lookiero] - " + text);
	}

}
