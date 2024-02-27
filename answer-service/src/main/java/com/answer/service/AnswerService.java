package com.answer.service;

import org.springframework.stereotype.Service;

import com.answer.entities.Answer;
import com.answer.repo.AnswerRepo;

@Service
public class AnswerService {
	
	private AnswerRepo repo;

	public AnswerService(AnswerRepo repo) {
		super();
		this.repo = repo;
	}
	
	public Answer addAnswer(Answer answer) {
		return repo.save(answer);
	}
	
	public Answer getAnswer(Long questionId) {
		return repo.findByQuestionId(questionId).orElseThrow(()->new RuntimeException("No answer found for the given question id"));
	}
	
	public void deleteAnswer(Long questionId) {
		Answer answer = repo.findByQuestionId(questionId).orElseThrow(()->new RuntimeException("No such question found"));
		
		repo.delete(answer);
	}

}
