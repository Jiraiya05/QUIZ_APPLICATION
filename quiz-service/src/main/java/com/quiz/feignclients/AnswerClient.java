package com.quiz.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.quiz.entities.Answer;
import com.quiz.response.GenericResponse;

@FeignClient(name = "ANSWER-SERVICE")
public interface AnswerClient {
	
	@GetMapping("/answer/{questionId}")
	GenericResponse<Answer> getAnswerOfQuestion(@PathVariable Long questionId);

}
