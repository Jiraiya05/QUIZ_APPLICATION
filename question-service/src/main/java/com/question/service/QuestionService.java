package com.question.service;

import java.util.List;

import com.question.entities.Question;

public interface QuestionService {
	
	Question create(Question question);
	
	List<Question> get();
	
	Question getOne(Long id) throws Exception;
	
	List<Question> getQuestionOfQuiz(Long quizId);

}
