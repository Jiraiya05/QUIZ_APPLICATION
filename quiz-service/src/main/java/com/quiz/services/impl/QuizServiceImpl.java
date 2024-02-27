package com.quiz.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.entities.Question;
import com.quiz.entities.Quiz;
import com.quiz.entities.UploadedAnswer;
import com.quiz.feignclients.AnswerClient;
import com.quiz.feignclients.MongoReportClient;
import com.quiz.feignclients.QuestionClient;
import com.quiz.kafka.Topics;
import com.quiz.repo.QuizRepository;
import com.quiz.repo.UploadedAnswersRepo;
import com.quiz.services.QuizService;
import com.quiz.utility.ReportData;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuizServiceImpl implements QuizService{
	
	private QuizRepository quizRepository;
	
	private QuestionClient questionClient;
	
	private AnswerClient answerClient;
	
	private UploadedAnswersRepo uploadedAnswersRepo;
	
	private MongoReportClient reportClient;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	

	public QuizServiceImpl(QuizRepository quizRepository, QuestionClient questionClient, AnswerClient answerClient,
			UploadedAnswersRepo uploadedAnswersRepo, MongoReportClient reportClient) {
		super();
		this.quizRepository = quizRepository;
		this.questionClient = questionClient;
		this.answerClient = answerClient;
		this.uploadedAnswersRepo = uploadedAnswersRepo;
		this.reportClient = reportClient;
	}

	@Override
	public Quiz add(Quiz quiz) throws JsonProcessingException {
		
		
		Quiz save = quizRepository.save(quiz);
		
		ReportData reportData = ReportData.builder()
		.userId(null)
		.user(null)
		.quizId(save.getId())
		.quizName(quiz.getTitle())
		.totalQuestions(null)
		.correctQuestions(null)
		.status("NEW QUIZ CREATED")
		.quizCreationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		.lastUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		.build();
		
		kafkaTemplate.send(Topics.REPORT_STATUS_TOPIC, objectMapper.writeValueAsString(reportData));
		
		return quizRepository.save(quiz);
	}

	@Override
	public List<Quiz> get() {
		
		List<Quiz> quizzes = quizRepository.findAll();
		
		List<Quiz> newQuizList = quizzes.stream().map(a -> {
			a.setQuestions(questionClient.getQuestionOfQuiz(a.getId()));
			return a;
		}).collect(Collectors.toList());
		
		return newQuizList;
	}

	@Override
	public Quiz get(Long id) {
		
		Quiz quiz = quizRepository.findById(id).orElseThrow(()->new RuntimeException("Quiz not found"));
		
		quiz.setQuestions(questionClient.getQuestionOfQuiz(quiz.getId()));
		
		return quiz;
	}

	@Override
	public void uploadAnswers(Long userId, Long quizId, String filepath) throws FileNotFoundException, IOException {
		
		List<Integer> answerList = new ArrayList<>();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)))){
			String line;
			while(!(line=reader.readLine()).isBlank() && (line=reader.readLine())!=null) {
				answerList.add(Integer.parseInt(line));
			}
			
		}catch (Exception e) {
			log.error("Error while uploading the answer file...."+e.getMessage());
		}
		
		UploadedAnswer uploadedAnswer = UploadedAnswer.builder()
		.quizId(quizId)
		.userId(userId)
		.submittedAnswers(answerList)
		.build();
		
		uploadedAnswersRepo.save(uploadedAnswer);
		
		ReportData dataFromMongo = reportClient.getDataFromMongo(userId, quizId);
		dataFromMongo.setStatus("ANSWERS UPLOADED");
		dataFromMongo.setLastUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		kafkaTemplate.send(Topics.REPORT_STATUS_TOPIC, objectMapper.writeValueAsString(dataFromMongo));
		
	}

	@Override
	public void evaluateAnswers(Long userId, Long quizId) throws JsonProcessingException {
		
		int totalQuestions=0;
		int correctQuestions=0;
		
		UploadedAnswer uploadedAnswer = uploadedAnswersRepo.findByUserIdAndQuizId(userId, quizId).orElseThrow(()->new RuntimeException("No answers uploaded yet"));
		
		Quiz quiz = quizRepository.findById(quizId).orElseThrow(()->new RuntimeException("No such quiz found"));
		
		List<Question> questions = quiz.getQuestions();
		
		List<Integer> answerList = new ArrayList<>();
		for(Question ques: questions) {
			answerList.add(answerClient.getAnswerOfQuestion(ques.getQuestionId()).getAnswer());
		}
		
		if(uploadedAnswer.getSubmittedAnswers().size()==questions.size()) {
			
			for(int i=0; i<questions.size(); i++) {
				if(uploadedAnswer.getSubmittedAnswers().get(i)==answerList.get(i)) {
					totalQuestions++;
					correctQuestions++;
				}else {
					totalQuestions++;
				}
			}
			
		}else {
			throw new RuntimeException("Invalid answer sheet for the quiz");
		}
		
		ReportData dataFromMongo = reportClient.getDataFromMongo(userId, quizId);
		dataFromMongo.setStatus("QUESTIONS EVALUATED");
		dataFromMongo.setLastUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		dataFromMongo.setTotalQuestions(totalQuestions);
		dataFromMongo.setCorrectQuestions(correctQuestions);
		
		kafkaTemplate.send(Topics.REPORT_STATUS_TOPIC, objectMapper.writeValueAsString(dataFromMongo));
		
	}

}
