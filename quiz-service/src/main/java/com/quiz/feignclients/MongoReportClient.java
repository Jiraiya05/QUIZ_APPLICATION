package com.quiz.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.quiz.utility.ReportData;

@FeignClient(name = "RESULT-SERVICE")
public interface MongoReportClient {
	
	@GetMapping("/mongo/{userId}/{quizId}")
	public ReportData getDataFromMongo(@PathVariable Long userId, @PathVariable Long quizId);
}
