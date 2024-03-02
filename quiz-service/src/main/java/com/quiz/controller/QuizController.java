package com.quiz.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entities.Quiz;
import com.quiz.request.UploadRequest;
import com.quiz.response.GenericResponse;
import com.quiz.response.GenericResponseBody;
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
	public GenericResponse<Quiz> create(@RequestBody Quiz quiz) throws Exception {
		
		Quiz add = quizService.add(quiz);
		
		return new GenericResponse<Quiz>(GenericResponseBody.successBody("QUIZ ADDED"), add);
	}
	
	@GetMapping
	public GenericResponse<List<Quiz>> get(){
		List<Quiz> list = quizService.get();
		
		return new GenericResponse<List<Quiz>>(GenericResponseBody.successBody("QUIZES FECTHED"), list);
	}
	
	@GetMapping("/{id}")
	public GenericResponse<Quiz> getOne(@PathVariable Long id) throws Exception {
		Quiz quiz = quizService.get(id);
		return new GenericResponse<Quiz>(GenericResponseBody.successBody("QUIZ FETCHED WITH ID : "+id), quiz);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/upload")
	public GenericResponse uploadAnswers(@RequestBody UploadRequest request) throws Exception {
		quizService.uploadAnswers(request.getUserId(), request.getQuizId(), request.getFilePath());
		
		return new GenericResponse(GenericResponseBody.successBody("ANSWERS UPLOADED"), HttpStatus.ACCEPTED);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/evaluate")
	public GenericResponse evaluateAnswers(@RequestParam Long userId, @RequestParam Long quizId) throws Exception {
		quizService.evaluateAnswers(userId, quizId);
		
		return new GenericResponse(GenericResponseBody.successBody("ANSWERS EVALUATED"), HttpStatus.OK);
	}

}
