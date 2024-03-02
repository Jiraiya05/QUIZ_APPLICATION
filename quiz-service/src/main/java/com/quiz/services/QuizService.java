package com.quiz.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quiz.entities.Quiz;

public interface QuizService {
	
	Quiz add(Quiz quiz, String userId, String userName) throws JsonProcessingException;
	
	List<Quiz> get();
	
	Quiz get(Long id) throws Exception;
	
	void uploadAnswers(Long userId, Long quizId, String filepath) throws FileNotFoundException, IOException, Exception;
	
	void evaluateAnswers(Long userId, Long quizId) throws JsonProcessingException, Exception;

}
