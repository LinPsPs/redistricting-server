package com.sbu.redistrictingserver;


import com.sbu.redistrictingserver.controller.JobController;
import com.sbu.redistrictingserver.geotools.GeoFile;
import com.sbu.redistrictingserver.model.Job;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class RedistrictingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedistrictingServerApplication.class, args);
		System.out.println("Hello from Server...");
		System.out.println("Start loading plans...");
		ArrayList<String> states = new ArrayList<>(Arrays.asList("MD"));
		for(String state: states) {
			JobController.loadPlans(state);
			JobController.loadEnactedPlans(state);
			GeoFile newFile = new GeoFile();
			newFile.loadFile(state);
			JobController.precinctData.put(state, newFile);
		}
	}

}
