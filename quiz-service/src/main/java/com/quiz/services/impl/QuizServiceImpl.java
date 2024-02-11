package com.quiz.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.entities.Quiz;
import com.quiz.repo.QuizRepository;
import com.quiz.services.QuestionClient;
import com.quiz.services.QuizService;

@Service
public class QuizServiceImpl implements QuizService{
	
	private QuizRepository quizRepository;
	
	private QuestionClient questionClient;

	

	public QuizServiceImpl(QuizRepository quizRepository, QuestionClient questionClient) {
		super();
		this.quizRepository = quizRepository;
		this.questionClient = questionClient;
	}

	@Override
	public Quiz add(Quiz quiz) {
		
		return quizRepository.save(quiz);
	}

	@Override
	public List<Quiz> get() {
		
		List<Quiz> quizzes = quizRepository.findAll();
		
		List<Quiz> newQuizList = quizzes.stream().map(a -> {
			a.setQuestions(questionClient.getQuestionOfQuiz(a.getId()));
			return a;
		}).collect(Collectors.toList());
		
		return newQuizList;
	}

	@Override
	public Quiz get(Long id) {
		
		Quiz quiz = quizRepository.findById(id).orElseThrow(()->new RuntimeException("Quiz not found"));
		
		quiz.setQuestions(questionClient.getQuestionOfQuiz(quiz.getId()));
		
		return quiz;
	}

}
