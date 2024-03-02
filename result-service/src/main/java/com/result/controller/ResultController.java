package com.result.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("/{quizId}")
	public GenericResponse<ResultResponse> getResult(@RequestHeader("X-userId") String userId, @PathVariable Long quizId, @RequestParam Integer cutOff, @RequestParam Integer markPerQuestion) throws Exception {
		
		ResultResponse processResponse = service.processResponse(Long.parseLong(userId), quizId, cutOff, markPerQuestion);
		
		return new GenericResponse<>(GenericResponseBody.successBody("Result Fetched successfully"), processResponse);
	}
	
	@GetMapping("/download/{quizId}")
	public GenericResponse<String> downloadResultFile(@RequestHeader("X-userId") String userId, @PathVariable Long quizId, @RequestParam Integer cutOff, @RequestParam Integer markPerQuestion) throws Exception {
		service.downloadResult(Long.parseLong(userId), quizId, cutOff, markPerQuestion);
		
		return new GenericResponse<>(GenericResponseBody.successBody("Result file downloaded successfully"), "File downloaded");
	}
	
	@GetMapping("/mongo/{quizId}")
	public GenericResponse<ResultData> getDataFromMongo(@RequestHeader("X-userId") String userId, @PathVariable Long quizId) {
		
		ResultData dataFromMongo = service.getDataFromMongo(Long.parseLong(userId), quizId);
		
		return new GenericResponse<ResultData>(GenericResponseBody.successBody("Data from mongo fetched successfully"), dataFromMongo);
	}

}
