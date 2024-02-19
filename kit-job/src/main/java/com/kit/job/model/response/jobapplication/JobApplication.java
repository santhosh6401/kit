package com.kit.job.model.response.jobapplication;


import com.kit.job.model.common.*;
import com.kit.job.model.enums.JobApplicationStatus;
import com.kit.job.model.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {

    private String jobApplicationId;
    private String jobId;
    private String email;
    private PersonalInfo personalInfo;
    private Map<String, Education> education;
    private Map<String, Experience> experience;
    private Map<String, Skills> skills;
    private JobApplicationStatus nextRoute;
    private JobStatus jobStatus;
    private List<LifeCycle> lifeCycles = new ArrayList<>();
    private Audit audit;
}
