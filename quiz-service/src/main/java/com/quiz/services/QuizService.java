package com.quiz.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quiz.entities.Quiz;
import com.quiz.entities.UploadedAnswer;

public interface QuizService {
	
	Quiz add(Quiz quiz) throws JsonProcessingException;
	
	List<Quiz> get();
	
	Quiz get(Long id);
	
	void uploadAnswers(Long userId, Long quizId, String filepath) throws FileNotFoundException, IOException;
	
	void evaluateAnswers(Long userId, Long quizId) throws JsonProcessingException;

}
