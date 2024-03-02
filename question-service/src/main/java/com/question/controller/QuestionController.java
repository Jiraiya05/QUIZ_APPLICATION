package com.question.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.question.entities.Question;
import com.question.response.GenericResponse;
import com.question.response.GenericResponseBody;
import com.question.service.QuestionService;

@RestController
@RequestMapping("/question")
public class QuestionController {
	
	private QuestionService questionService;
	
	public QuestionController(QuestionService questionService) {
		super();
		this.questionService = questionService;
	}

	@PostMapping
	public GenericResponse<Question> createQuestion(@RequestBody Question question) {
		Question create = questionService.create(question);
		return new GenericResponse<Question>(GenericResponseBody.successBody("Question created successfully"), create);
	}
	
	@GetMapping
	public GenericResponse<List<Question>> getAll(){
		List<Question> list = questionService.get();
		return new GenericResponse<List<Question>>(GenericResponseBody.successBody("All Questions Fetched"), list);
	}
	
	@RequestMapping("/{questionId}")
	public GenericResponse<Question> getOne(Long questionId) throws Exception {
		Question question = questionService.getOne(questionId);
		return new GenericResponse<Question>(GenericResponseBody.successBody("Question fetched for ID : "+questionId), question);
	}
	
	@GetMapping("/quiz/{quizId}")
	public GenericResponse<List<Question>> getQuestionsOfQuiz(@PathVariable Long quizId){
		List<Question> questionOfQuiz = questionService.getQuestionOfQuiz(quizId);
		return new GenericResponse<List<Question>>(GenericResponseBody.successBody("Questions fetched for Quiz Id : "+quizId), questionOfQuiz);
	}

}
