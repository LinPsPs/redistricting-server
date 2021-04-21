package com.sbu.redistrictingserver.handler;

import com.sbu.redistrictingserver.model.Job;
import com.sbu.redistrictingserver.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class JobHandler {
    @Autowired
    private JobRepository jobRepository;

    public void createJob(Job newJob) throws IOException {
        jobRepository.save(newJob);
    }

    public Job getJob(long id) {
        return jobRepository.findById(id).get();
    }
}
