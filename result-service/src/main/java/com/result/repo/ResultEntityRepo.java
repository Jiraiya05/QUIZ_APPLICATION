package com.result.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.result.entities.ResultEntity;

public interface ResultEntityRepo extends JpaRepository<ResultEntity, Integer>{
	
	Optional<ResultEntity> findByUserIdAndQuizId(Long userId, Long quizId);

}
