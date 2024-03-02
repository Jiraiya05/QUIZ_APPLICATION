package com.question.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.question.entities.Question;
import com.question.repo.QuestionRepository;
import com.question.service.QuestionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService{
	
	private QuestionRepository questionRepository;
	
	

	public QuestionServiceImpl(QuestionRepository questionRepository) {
		super();
		this.questionRepository = questionRepository;
	}

	@Override
	public Question create(Question question) {
		
		log.debug("Creating question | DATA => "+question.toString());
		
		Question save = questionRepository.save(question);
		
		log.info("Question saved successfully");
		
		return save;
	}

	@Override
	public List<Question> get() {
		
		log.debug("Getting all questions");
		
		List<Question> findAll = questionRepository.findAll();
		
		log.info("All questions fetched successfully");
		
		return findAll;
	}

	@Override
	public Question getOne(Long id) throws Exception {
		
		log.debug("Fetching question for id : "+id);
		
		Question question = questionRepository.findById(id).orElseThrow(()->new Exception("Question not found"));
		
		log.info("Question successfully fetched with ID : "+id);
		
		return question;
	}

	@Override
	public List<Question> getQuestionOfQuiz(Long quizId) {
		log.debug("Getting Questions for Quiz ID : "+quizId);
		
		List<Question> findByQuizId = questionRepository.findByQuizId(quizId);
		
		log.info("Questions fetched for QuizID : "+quizId);
		
		return findByQuizId;
	}

}
