package com.kit.job.repository;

import com.kit.job.model.entity.JobPostEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends MongoRepository<JobPostEntity, String> {
    Optional<JobPostEntity> findByJobId(String jobId);
}
