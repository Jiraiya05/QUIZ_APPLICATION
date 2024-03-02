package com.quiz.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.quiz.response.GenericResponse;
import com.quiz.utility.ReportData;

@FeignClient(name = "RESULT-SERVICE")
public interface MongoReportClient {
	
	@GetMapping("/mongo/{quizId}")
	GenericResponse<ReportData> getDataFromMongo(@RequestHeader("X-userId") String userId, @PathVariable Long quizId);
}
