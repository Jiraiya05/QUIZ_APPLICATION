package com.quiz.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.quiz.entities.Answer;

@FeignClient(name = "ANSWER-SERVICE")
public interface AnswerClient {
	
	@GetMapping("/answer/{questionId}")
	Answer getAnswerOfQuestion(@PathVariable Long questionId);

}
