package com.quiz.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
	public Quiz add(Quiz quiz, String userId, String userName) throws JsonProcessingException {
		
		log.debug("Add quiz called => DATA : "+quiz.toString());
		
		Quiz save = quizRepository.save(quiz);
		
		log.info("Quiz saved to database");
		
		ReportData reportData = ReportData.builder()
		.userId(Long.parseLong(userId))
		.user(userName)
		.quizId(save.getId())
		.quizName(quiz.getTitle())
		.totalQuestions(null)
		.correctQuestions(null)
		.status("NEW QUIZ CREATED")
		.quizCreationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		.lastUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		.build();
		
		log.debug("Sending report data to kafka. => DATA : "+reportData.toString());
		
		kafkaTemplate.send(Topics.REPORT_STATUS_TOPIC, objectMapper.writeValueAsString(reportData));
		
		log.info("Report data successfully sent to kafka topic");
		
		return quizRepository.save(quiz);
	}

	@Override
	public List<Quiz> get() {
		
		log.debug("Getting all quizes");
		
		List<Quiz> quizzes = quizRepository.findAll();
		
		log.debug("Setting questions into the quiz");
		
		List<Quiz> newQuizList = quizzes.stream().map(a -> {
			a.setQuestions(questionClient.getQuestionOfQuiz(a.getId()).getData());
			return a;
		}).collect(Collectors.toList());
		
		log.info("Quizes fetched successfully");
		
		return newQuizList;
	}

	@Override
	public Quiz get(Long id) throws Exception {
		
		log.debug("Getting Quiz by id => "+id);
		
		Quiz quiz = quizRepository.findById(id).orElseThrow(()->new Exception("No quiz with ID : "+id+" found"));
		
		quiz.setQuestions(questionClient.getQuestionOfQuiz(quiz.getId()).getData());
		
		log.info("Quiz successfully fetched with id : "+id);
		
		return quiz;
	}

	@Override
	public void uploadAnswers(Long userId, Long quizId, String filepath) throws Exception {
		
		log.debug("Uploading answers | Quiz ID : "+quizId+" | User ID : "+userId+" | File Path : "+filepath);
		
		List<Integer> answerList = new ArrayList<>();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)))){
			String line;
			while((line=reader.readLine())!=null) {
				if(!line.trim().isEmpty()) {
				answerList.add(Integer.parseInt(line.trim()));
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error while reading the answer file");
		}
		
		log.info("Answer file read successfully");
		
		UploadedAnswer uploadedAnswer = UploadedAnswer.builder()
		.quizId(quizId)
		.userId(userId)
		.submittedAnswers(answerList)
		.build();
		
		uploadedAnswersRepo.save(uploadedAnswer);
		
		log.info("Answers saved to database");
		
		ReportData dataFromMongo = reportClient.getDataFromMongo(String.valueOf(userId), quizId).getData();
		dataFromMongo.setStatus("ANSWERS UPLOADED");
		dataFromMongo.setLastUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		log.debug("Sending report data to kafka | DATA => "+dataFromMongo.toString());
		
		kafkaTemplate.send(Topics.REPORT_STATUS_TOPIC, objectMapper.writeValueAsString(dataFromMongo));
		
		log.info("Report data successfully sent to kafka topic");
		
	}

	@Override
	public void evaluateAnswers(Long userId, Long quizId) throws Exception {
		
		log.debug("Evaluating answers | User ID : "+userId+" | Quiz ID : "+quizId);
		
		int totalQuestions=0;
		int correctQuestions=0;
		
		UploadedAnswer uploadedAnswer = uploadedAnswersRepo.findByUserIdAndQuizId(userId, quizId).orElseThrow(()->new Exception("No answers uploaded yet"));
		
//		Quiz quiz = quizRepository.findById(quizId).orElseThrow(()->new Exception("No such quiz found"));
//		
//		System.out.println("quiz => "+quiz.toString());
		
//		List<Question> questions = quiz.getQuestions();
		
		List<Question> questions = questionClient.getQuestionOfQuiz(quizId).getData();
		
		System.out.println("questions => "+questions);
		
		List<Integer> answerList = new ArrayList<>();
		for(Question ques: questions) {
			answerList.add(answerClient.getAnswerOfQuestion(ques.getQuestionId()).getData().getAnswer());
		}
		
		if(uploadedAnswer.getSubmittedAnswers().size()==questions.size()) {
			
			for(int i=0; i<questions.size(); i++) {
				System.out.println("up : "+uploadedAnswer.getSubmittedAnswers().get(i)+"\t|\tans : "+answerList.get(i));
				if(uploadedAnswer.getSubmittedAnswers().get(i).equals(answerList.get(i))) {
					totalQuestions++;
					correctQuestions++;
				}else {
					totalQuestions++;
				}
			}
			
		}else {
			throw new Exception("Invalid answer sheet for the quiz");
		}
		
		log.info("Answers successfully evaluated");
		
		ReportData dataFromMongo = reportClient.getDataFromMongo(String.valueOf(userId), quizId).getData();
		dataFromMongo.setStatus("QUESTIONS EVALUATED");
		dataFromMongo.setLastUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		dataFromMongo.setTotalQuestions(totalQuestions);
		dataFromMongo.setCorrectQuestions(correctQuestions);
		
		log.debug("Sending report data to kafka | DATA => "+dataFromMongo.toString());
		
		kafkaTemplate.send(Topics.REPORT_STATUS_TOPIC, objectMapper.writeValueAsString(dataFromMongo));
		
		log.info("Data successfully sent to kafka topic");
		
	}

}
