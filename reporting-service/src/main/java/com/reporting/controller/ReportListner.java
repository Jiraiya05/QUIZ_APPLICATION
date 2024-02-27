package com.reporting.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reporting.entities.ReportData;
import com.reporting.kafka.Topics;
import com.reporting.repo.ReportRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ReportListner {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ReportRepository repository;
	
	public ReportListner(ReportRepository repository) {
		super();
		this.repository = repository;
	}

	@KafkaListener(topics = Topics.REPORT_STATUS_TOPIC, groupId = "quiz")
	public void updateDataToMongo(String json) throws JsonMappingException, JsonProcessingException {
		
		JsonNode node = objectMapper.readTree(json);
		
		ReportData data = ReportData.builder()
				.userId(node.get(null).asLong(0))
				.user(node.get(null).asText("NA"))
				.quizId(node.get(null).asLong(0))
				.quizName(node.get(null).asText("NA"))
				.totalQuestions(node.get(null).asInt(0))
				.status(node.get(null).asText("NA"))
				.quizCreationTime(node.get(null).asText("NA"))
				.lastUpdated(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
				.build();
		
		repository.save(data);
				
		log.info("Status updated => USER ID : "+data.getUserId()+" | QUIZ ID : "+data.getQuizId()+"QUIZ NAME"+data.getQuizName());
	}
	
	

}
