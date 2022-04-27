package rsp.lookiero.twitter.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Utils {
	
	public String dateComparator(LocalDateTime posted) {		
		
		LocalDateTime now = LocalDateTime.now();
		String finalString = "";

		long diffSeconds = ChronoUnit.SECONDS.between(posted, now);
		long diffMin = ChronoUnit.MINUTES.between(posted, now);
		long diffHours = ChronoUnit.HOURS.between(posted, now);
		long diffDays = ChronoUnit.DAYS.between(posted, now);
		
		if( diffDays > 0) {
			finalString = diffDays > 1 ? " ("+ diffDays + " days ago)" : " ("+ diffDays + " day ago)";
		}else if(diffHours > 0) {
			finalString = diffHours > 1 ? " ("+ diffHours + " hours ago)" : " ("+ diffHours + " hour ago)";
		}else if(diffMin > 0) {
			finalString = diffMin > 1 ? " ("+ diffMin + " minutes ago)" : " ("+ diffMin + " minute ago)";
		}else {
			finalString = diffSeconds > 1 ? " ("+ diffSeconds + " seconds ago)" : " ("+ diffSeconds + " second ago)";
		}
		
		return finalString;
		
	}

}
