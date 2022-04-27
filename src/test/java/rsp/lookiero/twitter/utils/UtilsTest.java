package rsp.lookiero.twitter.utils;


import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class UtilsTest {
	
	Utils utils = new Utils();
	
	@Test
	public void testComparatorSeconds() {
		
		LocalDateTime date =  LocalDateTime.now().minusSeconds(30);
		
		Assertions.assertEquals(" (30 seconds ago)",utils.dateComparator(date));		
		
	}
	
	@Test
	public void testComparatorMinutes() {
		
		LocalDateTime date =  LocalDateTime.now().minusMinutes(30);
		
		Assertions.assertEquals(" (30 minutes ago)",utils.dateComparator(date));		
		
	}
	
	@Test
	public void testComparatorHours() {
		
		LocalDateTime date =  LocalDateTime.now().minusHours(1);
		
		Assertions.assertEquals(" (1 hour ago)",utils.dateComparator(date));		
		
	}
	
	@Test
	public void testComparatorDays() {
		
		LocalDateTime date =  LocalDateTime.now().minusDays(15);
		
		Assertions.assertEquals(" (15 days ago)",utils.dateComparator(date));		
		
	}

}
