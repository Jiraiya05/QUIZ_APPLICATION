package com.result.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.result.entities.ResultData;
import com.result.entities.ResultEntity;
import com.result.repo.ResultEntityRepo;
import com.result.repo.ResultRepo;
import com.result.response.ResultResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResultService {
	
	private ResultRepo repo;
	
	@Autowired
	private ResultEntityRepo resultEntityRepo;

	public ResultService(ResultRepo repo) {
		super();
		this.repo = repo;
	}
	
	public ResultResponse processResponse(Long userId, String userName, Long quizId, Integer cutOff, Integer markPerQuestion) throws Exception {
		
		log.debug("Processing Response for result | User ID : "+userId+" | Quiz ID : "+quizId);
		
		ResultData resultData = repo.findByUserIdAndQuizId(userId, quizId).orElseThrow(()->new Exception("No result to be displayed"));
		
		ResultResponse resultResponse = ResultResponse.builder()
		.quizId(quizId)
		.userId(userId)
		.userName(userName)
		.quizName(resultData.getQuizName())
		.quizCreationTime(resultData.getQuizCreationTime())
		.resultTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		.totalMarks(resultData.getTotalQuestions()*markPerQuestion)
		.receivedMarks(resultData.getCorrectQuestions()*markPerQuestion)
		.resultStatus((resultData.getCorrectQuestions()*markPerQuestion)>=cutOff ? "PASS" : "FAIL")
		.build();
		
		log.info("Result Fetched successfully");
		
		return resultResponse;
	}
	
	public void downloadResult(Long userId, String userName,  Long quizId, Integer cutOff, Integer markPerQuestion, String filePath) throws Exception {
		
		log.debug("Downloading Result | User ID : "+userId+" | Quiz ID : "+quizId);
		
		ResultResponse processResponse = this.processResponse(userId, userName, quizId, cutOff, markPerQuestion);
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
			
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
			throw new Exception("Error while writing Result file");
		}
		
		Optional<ResultEntity> findByUserIdAndQuizId = resultEntityRepo.findByUserIdAndQuizId(userId, quizId);
		
		ResultEntity resultEntity = ResultEntity.builder()
		.userId(processResponse.getUserId())
		.userName(processResponse.getUserName())
		.quizId(processResponse.getQuizId())
		.quizName(processResponse.getQuizName())
		.quizCreationTime(processResponse.getQuizCreationTime())
		.resultTime(processResponse.getResultTime())
		.totalMarks(processResponse.getTotalMarks())
		.receivedMarks(processResponse.getReceivedMarks())
		.resultStatus(processResponse.getResultStatus())
		.build();
		
		
		if(!findByUserIdAndQuizId.isPresent()) {
			resultEntityRepo.save(resultEntity);
		}else {
			resultEntity.setId(findByUserIdAndQuizId.get().getId());
			resultEntity.setResultStatus("UPDATED : "+processResponse.getResultStatus());
			resultEntityRepo.save(resultEntity);
		}
		
		log.info("Result file written successfully");
	}
	
	public ResultData getDataFromMongo(Long userId, Long quizId) {
		log.debug("Getting mongo data | User ID : "+userId+" | Quiz ID : "+quizId);
		ResultData resultData = repo.findByUserIdAndQuizId(userId, quizId).orElseThrow(()->new RuntimeException("No data found"));
		log.info("Mongo data fetched successfully");
		return resultData;
	}

}
