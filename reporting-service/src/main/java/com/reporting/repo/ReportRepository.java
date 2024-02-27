package com.reporting.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.reporting.entities.ReportData;

public interface ReportRepository extends MongoRepository<ReportData, Long>{

}
