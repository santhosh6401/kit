package com.kit.job.service;

import com.kit.job.constant.AppConstant;
import com.kit.job.model.entity.JobPostEntity;
import com.kit.job.model.request.JobRequest;
import com.kit.job.model.response.CommonResponse;
import com.kit.job.model.response.product.JobPost;
import com.kit.job.model.response.product.JobPostResponse;
import com.kit.job.repository.JobRepository;
import com.kit.job.utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostService {

    private final JobRepository repository;

    private final HelperUtils utils;

    private final MongoTemplate mongoTemplate;

    public JobPostResponse createPost(JobRequest request, String uniqueInteractionId) {

        JobPostResponse response = new JobPostResponse();

        Optional<JobPostEntity> productOptional = repository.findByJobId(request.getJobId());

        if (productOptional.isPresent()) {
            response.setJobPosts(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + "  post job id is already exists .... ");
            return response;
        }
        try {
            JobPostEntity jobPostEntity = new JobPostEntity();
            BeanUtils.copyProperties(request, jobPostEntity);
            jobPostEntity.setAudit(utils.createAudit(uniqueInteractionId));
            jobPostEntity.setLifeCycles(utils.upsertLifeCycles("job post created ", jobPostEntity.getLifeCycles()));
            repository.save(jobPostEntity);
            JobPost product = new JobPost();
            BeanUtils.copyProperties(jobPostEntity, product);
            response.setJobPosts(Collections.singletonList(product));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setJobPosts(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }

        return response;

    }

    public JobPostResponse updatePost(JobRequest request, String uniqueInteractionId) {

        JobPostResponse response = new JobPostResponse();

        Optional<JobPostEntity> productOptional = repository.findByJobId(request.getJobId());

        if (productOptional.isEmpty()) {
            response.setJobPosts(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + "  job post id is not found .... ");
            return response;
        }
        try {
            JobPostEntity jobPostEntity = productOptional.get();
            BeanUtils.copyProperties(request, jobPostEntity);
            jobPostEntity.setAudit(utils.updateAudit(uniqueInteractionId, jobPostEntity.getAudit()));
            jobPostEntity.setLifeCycles(utils.upsertLifeCycles("job post  updated", jobPostEntity.getLifeCycles()));
            repository.save(jobPostEntity);
            JobPost product = new JobPost();
            BeanUtils.copyProperties(jobPostEntity, product);
            response.setJobPosts(Collections.singletonList(product));
            response.setResponse(AppConstant.SUCCESS);
        } catch (Exception e) {
            response.setJobPosts(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }
        return response;

    }

    public CommonResponse deletePost(String jobId) {
        CommonResponse response = new CommonResponse();

        if (Objects.isNull(jobId)) {
            response.setResponse(AppConstant.FAILED + "  jobId is missing .... ");
            return response;
        }

        Optional<JobPostEntity> productOptional = repository.findByJobId(jobId);
        if (productOptional.isEmpty()) {
            response.setResponse(AppConstant.FAILED + "  job post not found .... ");
            return response;
        }

        try {
            repository.delete(productOptional.get());
            response.setResponse(AppConstant.SUCCESS);
        } catch (Exception e) {
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }

        return response;

    }

    public JobPostResponse getPost(String jobId, String jobName) {

        Query query = new Query();

        if (Objects.nonNull(jobId)) {
            query.addCriteria(Criteria.where("jobId").is(jobId));
        }
        if (Objects.nonNull(jobName)) {
            query.addCriteria(Criteria.where("jobName").in(jobName));
        }

        query.with(Sort.by(Sort.Order.desc("audit.createdDate")));

        List<JobPostEntity> jobPostEntities = mongoTemplate.find(query, JobPostEntity.class);

        if (jobPostEntities.isEmpty()) {
            return JobPostResponse.builder()
                    .jobPosts(new ArrayList<>())
                    .response(AppConstant.FAILED + " no record found")
                    .build();
        }

        List<JobPost> jobPosts = new ArrayList<>();

        for (JobPostEntity productEntity : jobPostEntities) {
            JobPost jobPost = new JobPost();
            BeanUtils.copyProperties(productEntity, jobPost);
            jobPosts.add(jobPost);
        }

        return JobPostResponse.builder()
                .jobPosts(jobPosts)
                .response(AppConstant.SUCCESS)
                .build();
    }
}
