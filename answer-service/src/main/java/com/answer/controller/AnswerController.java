package com.answer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.answer.entities.Answer;
import com.answer.response.GenericResponse;
import com.answer.response.GenericResponseBody;
import com.answer.service.AnswerService;

@RestController
@RequestMapping("/answer")
public class AnswerController {
	
	@Autowired
	private AnswerService service;
	
	@PostMapping()
	public GenericResponse<Answer> addAnswer(@RequestBody Answer answer) {
		Answer addAnswer = service.addAnswer(answer);
		return new GenericResponse<Answer>(GenericResponseBody.successBody("Answer added successfully"), addAnswer);
		
	}
	
	@GetMapping("/{questionId}")
	public GenericResponse<Answer> getAnswer(Long questionId) throws Exception {
		Answer answer = service.getAnswer(questionId);
		return new GenericResponse<Answer>(GenericResponseBody.successBody("Answer Fetched for Question ID : "+questionId), answer);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/delete/{questionId}")
	public GenericResponse deleteAnswer(@PathVariable Long questionId) throws Exception {
		
		service.deleteAnswer(questionId);
		
		return new GenericResponse(GenericResponseBody.successBody("Answers deleted for QuestionId : "+questionId), HttpStatus.OK);
	}

}
