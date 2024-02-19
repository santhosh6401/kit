package com.kit.job.model.request;


import com.kit.job.model.common.Experience;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationExperienceRequest {
    private Map<String, Experience> experience;
}
