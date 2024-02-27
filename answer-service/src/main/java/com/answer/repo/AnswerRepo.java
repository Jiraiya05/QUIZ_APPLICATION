package com.answer.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.answer.entities.Answer;

public interface AnswerRepo extends JpaRepository<Answer, Long>{
	
	public Optional<Answer> findByQuestionId(Long questionId);

}
