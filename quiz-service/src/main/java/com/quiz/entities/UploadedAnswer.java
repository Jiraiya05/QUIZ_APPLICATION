package com.quiz.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "submitted_answers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadedAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long submissionId;
	private Long quizId;
	private Long userId;
	private List<Integer>  submittedAnswers;

}
