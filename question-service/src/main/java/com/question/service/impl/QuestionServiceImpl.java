package com.question.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.question.entities.Question;
import com.question.repo.QuestionRepository;
import com.question.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService{
	
	private QuestionRepository questionRepository;
	
	

	public QuestionServiceImpl(QuestionRepository questionRepository) {
		super();
		this.questionRepository = questionRepository;
	}

	@Override
	public Question create(Question question) {
		
		return questionRepository.save(question);
	}

	@Override
	public List<Question> get() {
		
		return questionRepository.findAll();
	}

	@Override
	public Question getOne(Long id) {
		
		return questionRepository.findById(id).orElseThrow(()->new RuntimeException("Question not found"));
	}

	@Override
	public List<Question> getQuestionOfQuiz(Long quizId) {
		
		return questionRepository.findByQuizId(quizId);
	}

}
