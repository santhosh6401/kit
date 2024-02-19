package com.kit.job.controller;


import com.kit.job.constant.AppConstant;
import com.kit.job.model.enums.JobApplicationStatus;
import com.kit.job.model.enums.JobStatus;
import com.kit.job.model.request.*;
import com.kit.job.model.response.CommonResponse;
import com.kit.job.model.response.jobapplication.JobApplication;
import com.kit.job.model.response.jobapplication.JobApplicationResponse;
import com.kit.job.service.JobApplicationService;
import com.kit.job.utils.UserValidation;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@Api(tags = "Job Application", value = "Job Application")
@Slf4j
@RestController
@RequiredArgsConstructor
public class JobApplicationController {

    private final UserValidation userValidation;

    private final JobApplicationService jobApplicationService;

    /* before payment client side */

    @PostMapping(value = "/initiate/personal", name = "request map add the product id and value")
    public JobApplicationResponse jobPersonaDetails(@RequestHeader String uniqueInteractionId,
                                                    @RequestHeader(name = "user-name") String userName,
                                                    @RequestHeader(name = "password") String password,
                                                    @RequestParam String email,
                                                    @RequestBody JobApplicationPersonalRequest request) throws Exception {
        log.info("interactionId :: [{}] request : initiate order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return jobApplicationService.personalDetails(uniqueInteractionId, request, email);
    }

    @PostMapping(value = "/initiate/education", name = "request map add the product id and value")
    public JobApplicationResponse jobEducation(@RequestHeader String uniqueInteractionId,
                                               @RequestHeader(name = "user-name") String userName,
                                               @RequestHeader(name = "password") String password,
                                               @RequestParam String jobApplicationId,
                                               @RequestBody JobApplicationEducationRequest request) throws Exception {
        log.info("interactionId :: [{}] request : initiate order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return jobApplicationService.education(uniqueInteractionId, jobApplicationId, request);
    }

    @PostMapping(value = "/initiate/experience", name = "request map add the product id and value")
    public JobApplicationResponse jobExperience(@RequestHeader String uniqueInteractionId,
                                                @RequestHeader(name = "user-name") String userName,
                                                @RequestHeader(name = "password") String password,
                                                @RequestParam String jobApplicationId,
                                                @RequestBody JobApplicationExperienceRequest request) throws Exception {
        log.info("interactionId :: [{}] request : initiate order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return jobApplicationService.experience(uniqueInteractionId, jobApplicationId, request);
    }

    @PostMapping(value = "/initiate/skill-submission", name = "request map add the product id and value")
    public JobApplicationResponse jobSkills(@RequestHeader String uniqueInteractionId,
                                            @RequestHeader(name = "user-name") String userName,
                                            @RequestHeader(name = "password") String password,
                                            @RequestParam String jobApplicationId,
                                            @RequestBody JobApplicationSkillsRequest request) throws Exception {
        log.info("interactionId :: [{}] request : initiate order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return jobApplicationService.skills(uniqueInteractionId, jobApplicationId, request);
    }

    /* update the status update on the admin inquiry */

    @PutMapping("/job/status")
    public JobApplicationResponse updateJobStatus(@RequestHeader String uniqueInteractionId,
                                                  @RequestHeader(name = "user-name") String userName,
                                                  @RequestHeader(name = "password") String password,
                                                  @RequestParam(name = "job-application-id") String jobApplicationId,
                                                  @RequestParam(name = "comment", required = false) String comment,
                                                  @RequestParam(name = "order-status", required = false) JobStatus jobStatus) throws Exception {
        log.info("interactionId :: [{}] update the order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return jobApplicationService.updateJobStatus(uniqueInteractionId, jobApplicationId, jobStatus, comment);
    }

    /* update the order related any issue => admin side */

    @PutMapping
    public JobApplicationResponse updateJob(@RequestHeader String uniqueInteractionId,
                                            @RequestHeader(name = "user-name") String userName,
                                            @RequestHeader(name = "password") String password,
                                            @RequestBody JobApplicationRequest jobApplicationRequest) throws Exception {
        log.info("interactionId :: [{}] update the order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        if(!Objects.nonNull(jobApplicationRequest) || !Objects.nonNull(jobApplicationRequest.getJobApplications())){
            JobApplicationResponse response = new JobApplicationResponse();
            response.setJobApplications(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " request missing");
            return response;
        }
        return jobApplicationService.updateJobApplication(uniqueInteractionId, jobApplicationRequest.getJobApplications().getJobApplicationId() ,jobApplicationRequest);
    }


    /* delete order admin side */

    @DeleteMapping
    public CommonResponse deleteJob(@RequestHeader String uniqueInteractionId,
                                    @RequestHeader(name = "user-name") String userName,
                                    @RequestHeader(name = "password") String password,
                                    @RequestParam(name = "job-application-id") String jobApplicationId) throws Exception {
        log.info("interactionId :: [{}] delete the order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return jobApplicationService.deleteJob(uniqueInteractionId, jobApplicationId);
    }


    /* common for all client & admin & merchant */

    @GetMapping
    public Page<JobApplication> getJobDetails(@RequestHeader String uniqueInteractionId,
                                              @RequestHeader(name = "user-name") String userName,
                                              @RequestHeader(name = "password") String password,
                                              @RequestParam(name = "email-id" ,required = false) String email,
                                              @RequestParam(name = "job-application-id", required = false) String jobApplicationId,
                                              @RequestParam(name = "job-status", required = false) JobStatus jobStatus,
                                              @RequestParam(name = "job-application-status", required = false) JobApplicationStatus nextRoute,
                                              @RequestParam int page,
                                              @RequestParam int size) throws Exception {
        log.info("interactionId :: [{}]  get order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return jobApplicationService.getJobStatus(uniqueInteractionId, jobApplicationId, jobStatus, nextRoute, email, page, size);
    }

}

