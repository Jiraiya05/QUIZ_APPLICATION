package com.question.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.question.entities.Question;
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
	public Question createQuestion(@RequestBody Question question) {
		return questionService.create(question);
	}
	
	@GetMapping
	public List<Question> getAll(){
		return questionService.get();
	}
	
	@RequestMapping("/{questionId}")
	public Question getOne(Long questionId) {
		return questionService.getOne(questionId);
	}
	
	@GetMapping("/quiz/{quizId}")
	public List<Question> getQuestionsOfQuiz(@PathVariable Long quizId){
		return questionService.getQuestionOfQuiz(quizId);
	}

}
