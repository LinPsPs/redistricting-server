package com.sbu.redistrictingserver.controller;

import com.sbu.redistrictingserver.handler.JobHandler;
import com.sbu.redistrictingserver.model.Job;
import com.sbu.redistrictingserver.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins="http://localhost:4000")
@RestController()
public class JobController {

    @Autowired
    JobHandler jobHandler = new JobHandler();

    @PostMapping("/job")
    public ResponseEntity createJob(@RequestBody Job newJob) throws IOException {
        System.out.println("Receive job " + newJob.toString());
        jobHandler.createJob(newJob);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/job/{id}")
    public ResponseEntity getJob(@PathVariable Long id) {
        Job job = jobHandler.getJob(id);
        return new ResponseEntity(job, HttpStatus.OK);
    }
}
