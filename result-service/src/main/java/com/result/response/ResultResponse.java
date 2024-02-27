package com.result.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultResponse {
	
	private Long userId;
	private String userName;
	private Long quizId;
	private String quizName;
	private String quizCreationTime;
	private String resultTime;
	private Integer totalMarks;
	private Integer receivedMarks;
	private String resultStatus;

}
