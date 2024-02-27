package com.result.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.result.entities.ResultData;
import com.result.repo.ResultRepo;
import com.result.response.ResultResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResultService {
	
	private ResultRepo repo;

	public ResultService(ResultRepo repo) {
		super();
		this.repo = repo;
	}
	
	public ResultResponse processResponse(Long userId, Long quizId, Integer cutOff, Integer markPerQuestion) {
		ResultData resultData = repo.findByUserIdAndQuizId(userId, quizId).orElseThrow(()->new RuntimeException("No result to be displayed"));
		
		return ResultResponse.builder()
				.quizId(quizId)
				.userId(userId)
				.userName(null)
				.quizName(resultData.getQuizName())
				.quizCreationTime(resultData.getQuizCreationTime())
				.resultTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.totalMarks(resultData.getTotalQuestions()*markPerQuestion)
				.receivedMarks(resultData.getCorrectQuestions()*markPerQuestion)
				.resultStatus((resultData.getCorrectQuestions()*markPerQuestion)>=cutOff ? "PASS" : "FAIL")
				.build();
	}
	
	public void downloadResult(Long userId, Long quizId, Integer cutOff, Integer markPerQuestion) {
		ResultResponse processResponse = this.processResponse(userId, quizId, cutOff, markPerQuestion);
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter("RESULT_"+userId+"_"+quizId+".txt"))){
			
			writer.write("RESULT FOR USER : "+processResponse.getUserName()+" AND QUIZ NAME : "+processResponse.getQuizName());
			writer.newLine();
			writer.write("USER ID\t:\t"+processResponse.getUserId());
			writer.newLine();
			writer.write("USERNAME\t:\t"+processResponse.getUserName());
			writer.newLine();
			writer.write("QUIZ ID\t:\t"+processResponse.getQuizId());
			writer.newLine();
			writer.write("QUIZ NAME\t:\t"+processResponse.getQuizName());
			writer.newLine();
			writer.write("QUIZ CREATION TIME\t:\t"+processResponse.getQuizCreationTime());
			writer.newLine();
			writer.write("RESULT TIME\t:\t"+processResponse.getResultTime());
			writer.newLine();
			writer.write("TOTAL MARKS\t:\t"+processResponse.getTotalMarks());
			writer.newLine();
			writer.write("MARKS OBTAINED\t:\t"+processResponse.getReceivedMarks());
			writer.newLine();
			writer.write("RESULT STATUS\t:\t"+processResponse.getResultStatus());
			
		}catch (Exception e) {
			log.error("Error while writing the result file...."+e.getMessage());
		}
	}
	
	public ResultData getDataFromMongo(Long userId, Long quizId) {
		ResultData resultData = repo.findByUserIdAndQuizId(userId, quizId).orElseThrow(()->new RuntimeException("No data found"));
		return resultData;
	}

}
