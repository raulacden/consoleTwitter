package rsp.lookiero.twitter.utils;

import java.util.Scanner;

public class ConsoleIO {
	
	private Scanner in;
	
	public String readLine() {
		try {
			in = new Scanner(System.in);		
			return in.nextLine();
		}finally {
			in.close();
		}
	}
	
	public void closeLine() {
		in.close();		
	}
	
	public void printLine(String text) {
		System.out.println("[Lookiero] - " + text);
	}

}
