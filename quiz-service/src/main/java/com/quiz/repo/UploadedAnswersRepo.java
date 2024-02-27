package com.quiz.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quiz.entities.UploadedAnswer;

public interface UploadedAnswersRepo extends JpaRepository<UploadedAnswer, Long>{
	
	Optional<UploadedAnswer> findByUserIdAndQuizId(Long userId, Long quizId);

}
