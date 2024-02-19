package com.kit.job.service;

import com.kit.job.constant.AppConstant;
import com.kit.job.model.common.PersonalInfo;
import com.kit.job.model.entity.JobApplicationEntity;
import com.kit.job.model.entity.ProfileEntity;
import com.kit.job.model.enums.JobApplicationStatus;
import com.kit.job.model.enums.JobStatus;
import com.kit.job.model.enums.ProfileType;
import com.kit.job.model.request.*;
import com.kit.job.model.response.CommonResponse;
import com.kit.job.model.response.jobapplication.JobApplication;
import com.kit.job.model.response.jobapplication.JobApplicationResponse;
import com.kit.job.repository.JobApplicationRepository;
import com.kit.job.repository.ProfileRepository;
import com.kit.job.utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplicationService {

    private final JobApplicationRepository repository;

    private final ProfileRepository profileRepository;

    private final HelperUtils utils;

    private final MongoTemplate mongoTemplate;

    public JobApplicationResponse personalDetails(String uniqueInteractionId, JobApplicationPersonalRequest request, String email) {

        JobApplicationResponse response = new JobApplicationResponse();

        if (!Objects.nonNull(request) || Objects.isNull(request.getPersonalInfo())) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " request missing");
            return response;
        }

        try {

            Optional<JobApplicationEntity> jobApplicationEntityOptional = repository.findByEmail(email);
            JobApplicationEntity jobApplicationEntity = new JobApplicationEntity();

            if (jobApplicationEntityOptional.isPresent()) {
                jobApplicationEntity = jobApplicationEntityOptional.get();

                if (jobApplicationEntity.getJobId().equalsIgnoreCase(request.getJobId()) &&
                        jobApplicationEntity.getNextRoute().equals(JobApplicationStatus.COMPLETED)) {
                    JobApplication jobApplication = new JobApplication();
                    BeanUtils.copyProperties(jobApplicationEntity, jobApplication);
                    response.setJobApplications(Collections.singletonList(jobApplication));
                    response.setResponse(AppConstant.FAILED + " Already for this job application submitted");
                    return response;
                }

            } else {
                jobApplicationEntity.setJobApplicationId(utils.generateId("JA"));
                BeanUtils.copyProperties(request, jobApplicationEntity);

                /* profile details */

                Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmail(email);

                if (profileEntityOptional.isEmpty()) {
                    response.setJobApplications(new ArrayList<>());
                    response.setResponse(AppConstant.FAILED + " customer profile not exist");
                    return response;
                }


                ProfileEntity profileEntity = profileEntityOptional.get();

                if (!Arrays.asList(ProfileType.JOB_SEEKER, ProfileType.CLIENT_AND_JOB_SEEKER).contains(profileEntity.getProfileType())) {
                    response.setJobApplications(new ArrayList<>());
                    response.setResponse(AppConstant.FAILED + " profile is not a job seeker profile ... ");
                    return response;
                }
            }
            PersonalInfo personalInfo = new PersonalInfo();
            BeanUtils.copyProperties(request.getPersonalInfo(), personalInfo);
            jobApplicationEntity.setEmail(email);
            jobApplicationEntity.setPersonalInfo(personalInfo);
            jobApplicationEntity.setLifeCycles(utils.upsertLifeCycles(" personal details updated ", jobApplicationEntity.getLifeCycles()));
            jobApplicationEntity.setAudit(utils.createAudit(uniqueInteractionId));

            jobApplicationEntity.setNextRoute(JobApplicationStatus.EDUCATION);
            jobApplicationEntity.setJobStatus(JobStatus.PENDING);
            repository.save(jobApplicationEntity);
            JobApplication jobApplication = new JobApplication();
            BeanUtils.copyProperties(jobApplicationEntity, jobApplication);
            response.setJobApplications(Collections.singletonList(jobApplication));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " " + e.getMessage());
        }
        return response;
    }


    public JobApplicationResponse education(String uniqueInteractionId, String jobApplicationId, JobApplicationEducationRequest request) {
        JobApplicationResponse response = new JobApplicationResponse();

        if (!Objects.nonNull(request) || Objects.isNull(request.getEducation()) || CollectionUtils.isEmpty(request.getEducation())) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " request missing");
            return response;
        }

        try {

            Optional<JobApplicationEntity> jobApplicationEntityOptional = repository.findByJobApplicationId(jobApplicationId);

            if (jobApplicationEntityOptional.isEmpty()) {
                response.setJobApplications(new ArrayList<>());
                response.setResponse(AppConstant.FAILED + " application not exist ");
                return response;
            }

            JobApplicationEntity jobApplicationEntity = jobApplicationEntityOptional.get();


            jobApplicationEntity.setEducation(request.getEducation());
            jobApplicationEntity.setLifeCycles(utils.upsertLifeCycles(" education details updated ", jobApplicationEntity.getLifeCycles()));
            jobApplicationEntity.setAudit(utils.createAudit(uniqueInteractionId));

            jobApplicationEntity.setNextRoute(JobApplicationStatus.EXPERIENCE);
            jobApplicationEntity.setJobStatus(JobStatus.PENDING);
            repository.save(jobApplicationEntity);
            JobApplication jobApplication = new JobApplication();
            BeanUtils.copyProperties(jobApplicationEntity, jobApplication);
            response.setJobApplications(Collections.singletonList(jobApplication));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " " + e.getMessage());
        }
        return response;
    }

    public JobApplicationResponse experience(String uniqueInteractionId, String jobApplicationId, JobApplicationExperienceRequest request) {
        JobApplicationResponse response = new JobApplicationResponse();

        if (!Objects.nonNull(request) || Objects.isNull(request.getExperience()) || CollectionUtils.isEmpty(request.getExperience())) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " request missing");
            return response;
        }

        try {

            Optional<JobApplicationEntity> jobApplicationEntityOptional = repository.findByJobApplicationId(jobApplicationId);

            if (jobApplicationEntityOptional.isEmpty()) {
                response.setJobApplications(new ArrayList<>());
                response.setResponse(AppConstant.FAILED + " application not exist ");
                return response;
            }

            JobApplicationEntity jobApplicationEntity = jobApplicationEntityOptional.get();


            jobApplicationEntity.setExperience(request.getExperience());
            jobApplicationEntity.setLifeCycles(utils.upsertLifeCycles(" experience details updated ", jobApplicationEntity.getLifeCycles()));
            jobApplicationEntity.setAudit(utils.createAudit(uniqueInteractionId));

            jobApplicationEntity.setNextRoute(JobApplicationStatus.SKILLS);
            jobApplicationEntity.setJobStatus(JobStatus.PENDING);
            repository.save(jobApplicationEntity);
            JobApplication jobApplication = new JobApplication();
            BeanUtils.copyProperties(jobApplicationEntity, jobApplication);
            response.setJobApplications(Collections.singletonList(jobApplication));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " " + e.getMessage());
        }
        return response;
    }

    public JobApplicationResponse skills(String uniqueInteractionId, String jobApplicationId, JobApplicationSkillsRequest request) {
        JobApplicationResponse response = new JobApplicationResponse();

        if (!Objects.nonNull(request) || Objects.isNull(request.getSkills()) || CollectionUtils.isEmpty(request.getSkills())) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " request missing");
            return response;
        }

        try {

            Optional<JobApplicationEntity> jobApplicationEntityOptional = repository.findByJobApplicationId(jobApplicationId);

            if (jobApplicationEntityOptional.isEmpty()) {
                response.setJobApplications(new ArrayList<>());
                response.setResponse(AppConstant.FAILED + " application not exist ");
                return response;
            }

            JobApplicationEntity jobApplicationEntity = jobApplicationEntityOptional.get();


            jobApplicationEntity.setSkills(request.getSkills());
            jobApplicationEntity.setLifeCycles(utils.upsertLifeCycles(" skills details updated & application submitted ", jobApplicationEntity.getLifeCycles()));
            jobApplicationEntity.setJobStatus(JobStatus.SUBMITTED);
            jobApplicationEntity.setAudit(utils.createAudit(uniqueInteractionId));

            jobApplicationEntity.setNextRoute(JobApplicationStatus.COMPLETED);

            repository.save(jobApplicationEntity);
            JobApplication jobApplication = new JobApplication();
            BeanUtils.copyProperties(jobApplicationEntity, jobApplication);
            response.setJobApplications(Collections.singletonList(jobApplication));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " " + e.getMessage());
        }
        return response;

    }

    public JobApplicationResponse updateJobStatus(String uniqueInteractionId, String jobApplicationId, JobStatus jobStatus, String comment) {

        JobApplicationResponse response = new JobApplicationResponse();

        if (!Objects.nonNull(jobApplicationId) || Objects.isNull(jobStatus)) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " request missing");
            return response;
        }

        try {

            Optional<JobApplicationEntity> jobApplicationEntityOptional = repository.findByJobApplicationId(jobApplicationId);

            if (jobApplicationEntityOptional.isEmpty()) {
                response.setJobApplications(new ArrayList<>());
                response.setResponse(AppConstant.FAILED + " application not exist ");
                return response;
            }

            JobApplicationEntity jobApplicationEntity = jobApplicationEntityOptional.get();

            jobApplicationEntity.setJobStatus(jobStatus);
            if (Objects.nonNull(comment))
                jobApplicationEntity.setComment(comment);
            jobApplicationEntity.setLifeCycles(utils.upsertLifeCycles(" job status updated to " + jobStatus.name(), jobApplicationEntity.getLifeCycles()));

            jobApplicationEntity.setAudit(utils.createAudit(uniqueInteractionId));

            repository.save(jobApplicationEntity);
            JobApplication jobApplication = new JobApplication();
            BeanUtils.copyProperties(jobApplicationEntity, jobApplication);
            response.setJobApplications(Collections.singletonList(jobApplication));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " " + e.getMessage());
        }
        return response;

    }

    public JobApplicationResponse updateJobApplication(String uniqueInteractionId, String jobApplicationId, JobApplicationRequest jobApplicationRequest) {

        JobApplicationResponse response = new JobApplicationResponse();


        if (!Objects.nonNull(jobApplicationId) || Objects.isNull(jobApplicationRequest)) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " request missing");
            return response;
        }

        try {

            Optional<JobApplicationEntity> jobApplicationEntityOptional = repository.findByJobApplicationId(jobApplicationId);

            if (jobApplicationEntityOptional.isEmpty()) {
                response.setJobApplications(new ArrayList<>());
                response.setResponse(AppConstant.FAILED + " application not exist ");
                return response;
            }

            JobApplicationEntity jobApplicationEntity = jobApplicationEntityOptional.get();

            BeanUtils.copyProperties(jobApplicationRequest.getJobApplications(), jobApplicationEntity);
            jobApplicationEntity.setLifeCycles(utils.upsertLifeCycles(" job application details updated ", jobApplicationEntity.getLifeCycles()));

            jobApplicationEntity.setAudit(utils.createAudit(uniqueInteractionId));

            repository.save(jobApplicationEntity);
            JobApplication jobApplication = new JobApplication();
            BeanUtils.copyProperties(jobApplicationEntity, jobApplication);
            response.setJobApplications(Collections.singletonList(jobApplication));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " " + e.getMessage());
        }
        return response;

    }

    public CommonResponse deleteJob(String uniqueInteractionId, String jobApplicationId) {
        CommonResponse response = new CommonResponse();

        if (Objects.isNull(jobApplicationId)) {
            response.setResponse(AppConstant.FAILED + "  orderId is missing .... ");
            return response;
        }

        Optional<JobApplicationEntity> optional = repository.findByJobApplicationId(jobApplicationId);
        if (optional.isEmpty()) {
            response.setResponse(AppConstant.FAILED + "  job application not found .... ");
            return response;
        }

        try {
            repository.delete(optional.get());
            response.setResponse(AppConstant.SUCCESS);
        } catch (Exception e) {
            log.info("interactionId : {} , error in delete application : {} ", uniqueInteractionId, e.getMessage());
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }

        return response;
    }

    public Page<JobApplication> getJobStatus(String uniqueInteractionId, String jobApplicationId, JobStatus jobStatus, JobApplicationStatus nextRoute, String email, int page, int size) {
        Query query = new Query();
        Pageable pageable = PageRequest.of(page, size);
        if (Objects.nonNull(jobApplicationId)) {
            query.addCriteria(Criteria.where("jobApplicationId").is(jobApplicationId));
        }
        if (Objects.nonNull(jobStatus)) {
            query.addCriteria(Criteria.where("jobStatus").is(jobStatus));
        }
        if (Objects.nonNull(nextRoute)) {
            query.addCriteria(Criteria.where("nextRoute").is(nextRoute));
        }
        if (Objects.nonNull(email)) {
            query.addCriteria(Criteria.where("email").is(email));
        }

        query.with(pageable);

        List<JobApplicationEntity> purchaseEntities = mongoTemplate.find(query, JobApplicationEntity.class);

        log.info("interactionId : {} , query : {} and result : {} ", uniqueInteractionId, query, purchaseEntities);

        if (purchaseEntities.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<JobApplication> jobApplication = new ArrayList<>();

        for (JobApplicationEntity profile : purchaseEntities) {
            JobApplication purchase = new JobApplication();
            BeanUtils.copyProperties(profile, purchase);
            jobApplication.add(purchase);
        }

        return new PageImpl<>(jobApplication, pageable, jobApplication.size());


    }
}
