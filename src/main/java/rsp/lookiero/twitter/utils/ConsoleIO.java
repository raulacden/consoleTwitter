package rsp.lookiero.twitter.utils;

import java.util.Scanner;

public class ConsoleIO {
	
	public String readLine() {
		Scanner in = new Scanner(System.in);
		return in.nextLine();
	}
	
	public void printLine(String text) {
		System.out.println("[Lookiero] - " + text);
	}

}
