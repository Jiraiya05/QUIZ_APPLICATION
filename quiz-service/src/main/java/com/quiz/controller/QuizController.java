package com.quiz.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entities.Quiz;
import com.quiz.request.UploadRequest;
import com.quiz.response.GenericResponse;
import com.quiz.response.GenericResponseBody;
import com.quiz.services.QuizService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/quiz")
@Slf4j
public class QuizController {
	
	private QuizService quizService;

	public QuizController(QuizService quizService) {
		super();
		this.quizService = quizService;
	}
	
	@PostMapping
	public GenericResponse<Quiz> create(@RequestHeader("X-userId") String userId, @RequestHeader("X-userName") String userName, @RequestBody Quiz quiz) throws Exception {
		
		Quiz add = quizService.add(quiz, userId, userName);
		
		return new GenericResponse<Quiz>(GenericResponseBody.successBody("QUIZ ADDED"), add);
	}
	
	@GetMapping
	@CircuitBreaker(name = "FETCH_ALL_QUIZES", fallbackMethod = "getAllQuizFallback")
	public GenericResponse<List<Quiz>> get(){
		List<Quiz> list = quizService.get();
		
		return new GenericResponse<List<Quiz>>(GenericResponseBody.successBody("QUIZES FECTHED"), list);
	}
	
	@GetMapping("/{id}")
	@CircuitBreaker(name = "FETCH_QUIZ_BY_ID", fallbackMethod = "getAllQuizFallback")
	public GenericResponse<Quiz> getOne(@PathVariable Long id) throws Exception {
		Quiz quiz = quizService.get(id);
		return new GenericResponse<Quiz>(GenericResponseBody.successBody("QUIZ FETCHED WITH ID : "+id), quiz);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/upload")
	@CircuitBreaker(name = "UPLOAD_ANSWERS", fallbackMethod = "uploadAnswersFallback")
	public GenericResponse uploadAnswers(@RequestBody UploadRequest request, @RequestHeader("X-userId") String userId) throws Exception {
		quizService.uploadAnswers(Long.parseLong(userId), request.getQuizId(), request.getFilePath());
		
		return new GenericResponse(GenericResponseBody.successBody("ANSWERS UPLOADED"), HttpStatus.ACCEPTED);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/evaluate")
	@CircuitBreaker(name = "EVALUATE_ANSWERS", fallbackMethod = "evaluateAnswersFallback")
	public GenericResponse evaluateAnswers(@RequestHeader("X-userId") String userId, @RequestParam Long quizId) throws Exception {
		quizService.evaluateAnswers(Long.parseLong(userId), quizId);
		
		return new GenericResponse(GenericResponseBody.successBody("ANSWERS EVALUATED"), HttpStatus.OK);
	}
	
	//CIRCUIT-BREAKERS
	
	public GenericResponse<List<Quiz>> getAllQuizFallback(Exception exception){
		
		log.error("FALLBACK EXECUTED | MESSAGE => "+exception.getMessage());
		
		List<Quiz> quizes = new ArrayList<>();
		
		return new GenericResponse<List<Quiz>>(GenericResponseBody.failBody("Something went wrong"), quizes);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericResponse uploadAnswersFallback(@RequestBody UploadRequest request, @RequestHeader("X-userId") String userId, Exception exception) {
		
		log.error("FALLBACK EXECUTED | MESSAGE => "+exception.getMessage());
		
		return new GenericResponse(GenericResponseBody.failBody("Something went wrong"), HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericResponse evaluateAnswersFallback(@RequestHeader("X-userId") String userId, @RequestParam Long quizId, Exception exception) {
		
		log.error("FALLBACK EXECUTED | MESSAGE => "+exception.getMessage());
		
		return new GenericResponse(GenericResponseBody.failBody("Something went wrong"), HttpStatus.SERVICE_UNAVAILABLE);
		
	}

}
