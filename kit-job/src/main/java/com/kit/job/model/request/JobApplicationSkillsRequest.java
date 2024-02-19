package com.kit.job.model.request;

import com.kit.job.model.common.Skills;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationSkillsRequest {
    private Map<String, Skills> skills;
}
