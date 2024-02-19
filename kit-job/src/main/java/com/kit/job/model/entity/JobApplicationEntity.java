package com.kit.job.model.entity;

import com.kit.job.model.common.*;
import com.kit.job.model.enums.JobApplicationStatus;
import com.kit.job.model.enums.JobStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "job-application")
public class JobApplicationEntity {
    @Id
    private String jobApplicationId;
    private String jobId;
    private String email;
    private PersonalInfo personalInfo;
    private Map<String, Education> education = new HashMap<>();
    private Map<String, Experience> experience = new HashMap<>();
    private Map<String, Skills> skills = new HashMap<>();
    private JobApplicationStatus nextRoute;
    private String comment;   /* job comment for admin */
    private JobStatus jobStatus;
    private List<LifeCycle> lifeCycles = new ArrayList<>();
    private Audit audit;
}
