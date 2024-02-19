package com.kit.job.controller;


import com.kit.job.model.request.JobRequest;
import com.kit.job.model.response.CommonResponse;
import com.kit.job.model.response.product.JobPostResponse;
import com.kit.job.service.JobPostService;
import com.kit.job.utils.UserValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Job Posts", value = "Job Posts")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class JobPostController {

    private final UserValidation userValidation;

    private final JobPostService jobPostService;


    @PostMapping
    public JobPostResponse createPost(@RequestHeader String uniqueInteractionId,
                                         @RequestBody JobRequest request,
                                         @RequestHeader(name = "user-name") String userName,
                                         @RequestHeader(name = "password") String password) throws Exception {
        log.info("interactionId :: [{}] request : {} create the content config", uniqueInteractionId, request);
        userValidation.validate(userName, password);
        return jobPostService.createPost(request, uniqueInteractionId);
    }

    @PutMapping
    public JobPostResponse updatePost(@RequestHeader String uniqueInteractionId,
                                         @RequestBody JobRequest request,
                                         @RequestHeader(name = "user-name") String userName,
                                         @RequestHeader(name = "password") String password) throws Exception {
        log.info("interactionId :: [{}] request : {} update the product request", uniqueInteractionId, request);
        userValidation.validate(userName, password);
        return jobPostService.updatePost(request, uniqueInteractionId);
    }

    @DeleteMapping
    public CommonResponse deletePost(@RequestHeader String uniqueInteractionId,
                                        @RequestParam String jobId,
                                        @RequestHeader(name = "user-name") String userName,
                                        @RequestHeader(name = "password") String password) throws Exception {
        log.info("interactionId :: [{}] content-name : {} delete the product based on id", uniqueInteractionId, jobId);
        userValidation.validate(userName, password);
        return jobPostService.deletePost(jobId);
    }

    @GetMapping
    public JobPostResponse getPost(
            @RequestParam(required = false, value = "job-name", name = "job-name") @ApiParam(name = "job-name") String jobName,
            @RequestParam(required = false, value = "job-id", name = "job-id") @ApiParam(name = "job-id") String jobId,   /* separate with , */
            @RequestHeader(name = "user-name") String userName,
            @RequestHeader(name = "password") String password) throws Exception {
        userValidation.validate(userName, password);
        return jobPostService.getPost(jobId , jobName);
    }


}
