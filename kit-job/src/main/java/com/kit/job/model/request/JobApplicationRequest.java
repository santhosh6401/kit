package com.kit.job.model.request;

import com.kit.job.model.response.jobapplication.JobApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationRequest {
    private JobApplication jobApplications;
}
