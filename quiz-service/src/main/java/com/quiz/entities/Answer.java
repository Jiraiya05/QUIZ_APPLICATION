package com.quiz.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer {
	
	private Long answerId;
	private Long questionId;
	private List<String> options;
	private int answer;

}
