package com.kit.job.model.entity;

import com.kit.job.model.common.Audit;
import com.kit.job.model.common.LifeCycle;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "job-post")
public class JobPostEntity {
    @Id
    private String jobId;
    private String jobName;
    private String jobImage;
    private String jobTitle;
    private String jobDescription;
    private List<LifeCycle> lifeCycles = new ArrayList<>();
    private Audit audit;
}
