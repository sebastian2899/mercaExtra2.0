package com.mercaextra.app.config;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Calendar;

public final class Utilities {
	
	public static final String validateDate(Instant date) {
		Instant instant = Instant.now();
		String instantFormat;
		
		LocalTime localTime = LocalTime.now();
		String localFormat = localTime.toString().substring(0, 2);
		
		int timeInt = Integer.parseInt(localFormat);
		
		if(timeInt >= 19) {
			Calendar calendar = Calendar.getInstance();
			
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			
			Instant daySubtract = calendar.toInstant();
			instantFormat  = daySubtract.toString().substring(0, 10);
		}else {
			instantFormat = instant.toString().substring(0, 10);
		}
		
		return instantFormat;
	}

}
