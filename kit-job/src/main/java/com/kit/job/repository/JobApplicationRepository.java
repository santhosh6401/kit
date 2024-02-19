package com.kit.job.repository;

import com.kit.job.model.entity.JobApplicationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobApplicationRepository extends MongoRepository<JobApplicationEntity, String> {
    Optional<JobApplicationEntity> findByJobApplicationId(String jobApplicationId);

    Optional<JobApplicationEntity> findByEmail(String email);
}
