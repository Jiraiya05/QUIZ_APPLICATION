package com.result.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.result.entities.ResultData;
import com.result.response.ResultResponse;
import com.result.service.ResultService;

@RestController
@RequestMapping("/result")
public class ResultController {
	
	@Autowired
	private ResultService service;
	
	@GetMapping("/{userId}/{quizId}/{cutOff}/{markPerQuestion}")
	public ResultResponse getResult(@PathVariable Long userId, @PathVariable Long quizId, @PathVariable Integer cutOff, @PathVariable Integer markPerQuestion) {
		return service.processResponse(userId, quizId, cutOff, markPerQuestion);
	}
	
	@GetMapping("/{userId}/{quizId}/{cutOff}/{markPerQuestion}")
	public void downloadResultFile(@PathVariable Long userId, @PathVariable Long quizId, @PathVariable Integer cutOff, @PathVariable Integer markPerQuestion) {
		service.downloadResult(userId, quizId, cutOff, markPerQuestion);
	}
	
	@GetMapping("/mongo/{userId}/{quizId}")
	public ResultData getDataFromMongo(@PathVariable Long userId, @PathVariable Long quizId) {
		return service.getDataFromMongo(userId, quizId);
	}

}
