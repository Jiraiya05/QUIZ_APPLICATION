package com.result.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "quiz-report-status")
public class ResultData {
	
	@Id
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
