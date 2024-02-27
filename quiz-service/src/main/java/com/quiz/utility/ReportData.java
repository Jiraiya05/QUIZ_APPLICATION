package com.quiz.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportData {
	
	private Long userId;
	private String user;
	private Long quizId;
	private String quizName;
	private Integer totalQuestions;
	private Integer correctQuestions;
	private String status;
	private String quizCreationTime; 
	private String lastUpdated;

}
