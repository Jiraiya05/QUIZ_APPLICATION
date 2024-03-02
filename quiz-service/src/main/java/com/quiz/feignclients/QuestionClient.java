package com.quiz.feignclients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.quiz.entities.Question;
import com.quiz.response.GenericResponse;

//@FeignClient(url = "http://localhost:8082", value = "Question-Client")
@FeignClient(name = "QUESTION-SERVICE")
public interface QuestionClient {
	
	@GetMapping("/question/quiz/{quizId}")
	GenericResponse<List<Question>> getQuestionOfQuiz(@PathVariable Long quizId);

}
