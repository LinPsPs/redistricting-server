package com.sbu.redistrictingserver.controller;

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

    @PostMapping(path="/job")
    public ResponseEntity createJob(@RequestBody String newJob) throws IOException {
        System.out.println("Receive job " + newJob);
//        Gson g = new Gson();
//        jobrepo.save(newJob);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(path="/job/{id}")
    public ResponseEntity getJob(@PathVariable Long id) {
        Job job = jobrepo.findById(id).get();
        System.out.println("Find job " + job.toString());
        return new ResponseEntity(job.toString(), HttpStatus.OK);
    }
}
