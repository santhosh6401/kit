package com.kit.job.model.request;

import lombok.Data;

@Data
public class JobRequest {
    private String jobId;
    private String jobName;
    private String jobImage;
    private String jobTitle;
    private String jobDescription;
}
