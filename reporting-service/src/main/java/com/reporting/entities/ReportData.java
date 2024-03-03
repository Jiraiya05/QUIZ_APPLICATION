package com.reporting.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "quiz-report-status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportData {
	
	@Id
	private String id;
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
