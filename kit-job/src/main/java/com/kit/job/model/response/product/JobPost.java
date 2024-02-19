package com.kit.job.model.response.product;

import lombok.Data;

@Data
public class JobPost {
    private String jobId;
    private String jobName;
    private String jobImage;
    private String jobTitle;
    private String jobDescription;
}
