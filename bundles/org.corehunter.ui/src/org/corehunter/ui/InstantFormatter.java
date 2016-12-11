package org.corehunter.ui;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class InstantFormatter {
	
	private static final SimpleDateFormat timeAndDataFormat = new SimpleDateFormat("HH:mm:ss (dd-MMM-yyyy)");
	
	public static final String format(Instant instant) {
		
		Date myDate = Date.from(instant);
	
		return timeAndDataFormat.format(myDate);
	}

}
