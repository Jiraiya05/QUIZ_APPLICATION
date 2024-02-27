package com.answer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.answer.entities.Answer;
import com.answer.service.AnswerService;

@RestController
@RequestMapping("/answer")
public class AnswerController {
	
	@Autowired
	private AnswerService service;
	
	@PostMapping()
	public Answer addAnswer(@RequestBody Answer answer) {
		
		return service.addAnswer(answer);
		
	}
	
	@GetMapping("/{questionId}")
	public Answer getAnswer(Long questionId) {
		return service.getAnswer(questionId);
	}

}
