package com.result.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.result.entities.ResultData;
import com.result.genericresponse.GenericResponse;
import com.result.genericresponse.GenericResponseBody;
import com.result.response.ResultResponse;
import com.result.service.ResultService;

@RestController
@RequestMapping("/result")
public class ResultController {
	
	@Autowired
	private ResultService service;
	
	@GetMapping("/{userId}/{quizId}/{cutOff}/{markPerQuestion}")
	public GenericResponse<ResultResponse> getResult(@PathVariable Long userId, @PathVariable Long quizId, @PathVariable Integer cutOff, @PathVariable Integer markPerQuestion) throws Exception {
		
		ResultResponse processResponse = service.processResponse(userId, quizId, cutOff, markPerQuestion);
		
		return new GenericResponse<>(GenericResponseBody.successBody("Result Fetched successfully"), processResponse);
	}
	
	@GetMapping("/{userId}/{quizId}/{cutOff}/{markPerQuestion}")
	public GenericResponse<String> downloadResultFile(@PathVariable Long userId, @PathVariable Long quizId, @PathVariable Integer cutOff, @PathVariable Integer markPerQuestion) throws Exception {
		service.downloadResult(userId, quizId, cutOff, markPerQuestion);
		
		return new GenericResponse<>(GenericResponseBody.successBody("Result file downloaded successfully"), "File downloaded");
	}
	
	@GetMapping("/mongo/{userId}/{quizId}")
	public GenericResponse<ResultData> getDataFromMongo(@PathVariable Long userId, @PathVariable Long quizId) {
		
		ResultData dataFromMongo = service.getDataFromMongo(userId, quizId);
		
		return new GenericResponse<ResultData>(GenericResponseBody.successBody("Data from mongo fetched successfully"), dataFromMongo);
	}

}
