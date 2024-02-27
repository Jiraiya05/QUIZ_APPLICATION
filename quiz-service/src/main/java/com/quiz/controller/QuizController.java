package com.quiz.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quiz.entities.Quiz;
import com.quiz.request.UploadRequest;
import com.quiz.services.QuizService;

@RestController
@RequestMapping("/quiz")
public class QuizController {
	
	private QuizService quizService;

	public QuizController(QuizService quizService) {
		super();
		this.quizService = quizService;
	}
	
	@PostMapping
	public Quiz create(@RequestBody Quiz quiz) throws JsonProcessingException {
		return quizService.add(quiz);
	}
	
	@GetMapping
	public List<Quiz> get(){
		return quizService.get();
	}
	
	@GetMapping("/{id}")
	public Quiz getOne(@PathVariable Long id) {
		return quizService.get(id);
	}
	
	@PostMapping("/upload")
	public void uploadAnswers(@RequestBody UploadRequest request) throws FileNotFoundException, IOException {
		quizService.uploadAnswers(request.getUserId(), request.getQuizId(), request.getFilePath());
	}
	
	@GetMapping("/evaluate")
	public void evaluateAnswers(@RequestParam Long userId, @RequestParam Long quizId) throws JsonProcessingException {
		quizService.evaluateAnswers(userId, quizId);
	}

}
