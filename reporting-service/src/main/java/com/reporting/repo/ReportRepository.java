package com.reporting.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.reporting.entities.ReportData;

public interface ReportRepository extends MongoRepository<ReportData, Long>{
	
	Optional<ReportData> findByUserIdAndQuizId(Long userId, Long quizId);

}
