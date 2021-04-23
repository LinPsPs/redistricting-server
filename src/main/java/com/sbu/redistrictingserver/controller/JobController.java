package com.sbu.redistrictingserver.controller;

import com.google.gson.Gson;
import com.sbu.redistrictingserver.model.Job;
import com.sbu.redistrictingserver.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



import java.io.IOException;

@RestController
@RequestMapping(path="/")
public class JobController {
    @Autowired
    private JobRepository jobrepo;
    private final Gson gson = new Gson();

    @PostMapping(path="/job")
    public ResponseEntity createJob(@RequestBody String jobJson) throws IOException {
        System.out.println("Receive job " + jobJson);

        Job job = gson.fromJson(jobJson, Job.class);
        jobrepo.save(job);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(path="/job/{id}")
    public ResponseEntity getJob(@PathVariable Long id) {
        Job job = jobrepo.findById(id).get();
        System.out.println("Find job " + gson.toJson(job, Job.class));
        return new ResponseEntity(gson.toJson(job, Job.class), HttpStatus.OK);
    }
}
