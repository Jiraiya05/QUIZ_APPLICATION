package com.answer.service;

import org.springframework.stereotype.Service;

import com.answer.entities.Answer;
import com.answer.repo.AnswerRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnswerService {
	
	private AnswerRepo repo;

	public AnswerService(AnswerRepo repo) {
		super();
		this.repo = repo;
	}
	
	public Answer addAnswer(Answer answer) {
		log.debug("Adding answer | DATA => "+answer.toString());
		
		Answer save = repo.save(answer);
		
		log.info("Answer saved successfully");
		
		return save;
	}
	
	public Answer getAnswer(Long questionId) throws Exception {
		
		log.debug("Getting Answer for Question ID : "+questionId);
		
		Answer answer = repo.findByQuestionId(questionId).orElseThrow(()->new Exception("No answer found for the given question id"));
		
		log.info("Answer successfully fetched for Question ID : "+questionId);
		
		return answer;
	}
	
	public void deleteAnswer(Long questionId) throws Exception {
		
		log.debug("Deleting Answer for Question ID : "+questionId);
		
		Answer answer = repo.findByQuestionId(questionId).orElseThrow(()->new Exception("No such question found"));
		
		repo.delete(answer);
		
		log.info("Answer successfully deleted for Question ID : "+questionId);
	}

}
