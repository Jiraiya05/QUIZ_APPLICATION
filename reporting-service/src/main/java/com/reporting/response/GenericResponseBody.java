package com.reporting.response;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericResponseBody {
	
	private String date;
	private String status;
	private String description;
	
	public static GenericResponseBody successBody(String description) {
		return GenericResponseBody.builder()
				.date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.status("SUCCESS")
				.description(description)
				.build();
	}
	
	public static GenericResponseBody failBody(String description) {
		return GenericResponseBody.builder()
				.date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.description(description)
				.build();
	}

}
