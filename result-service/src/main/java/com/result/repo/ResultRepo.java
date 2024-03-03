package com.result.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.result.entities.ResultData;

public interface ResultRepo extends MongoRepository<ResultData, Long>{
	
	Optional<ResultData> findByUserIdAndQuizId(Long userId, Long quizId);
	
}
