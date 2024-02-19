package com.kit.job.model.request;


import com.kit.job.model.common.PersonalInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationPersonalRequest {
    private String jobId;
    private PersonalInfo personalInfo;
}

