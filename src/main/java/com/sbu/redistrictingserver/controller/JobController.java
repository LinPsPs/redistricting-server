package com.sbu.redistrictingserver.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sbu.redistrictingserver.geotools.GeoFile;
import com.sbu.redistrictingserver.model.District;
import com.sbu.redistrictingserver.model.DistrictingPlan;
import com.sbu.redistrictingserver.model.Job;
import com.sbu.redistrictingserver.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@RequestMapping(path="/")
public class JobController {
    @Autowired
    private JobRepository jobrepo;
    private final Gson gson = new Gson();
    public static HashMap<String, ArrayList<DistrictingPlan>> districtPlans = new HashMap<>();
    public static HashMap<String, ArrayList<District>> enacted = new HashMap<>();
    public static HashMap<String, GeoFile> precinctData = new HashMap<>();

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

    @GetMapping(path="/job/getPlot/{state}")
    public ResponseEntity getPlot(@PathVariable String state) {
        Job job = new Job(state);
        return new ResponseEntity(job.getBoxandWhiskerPlot("BVAP"), HttpStatus.OK);
    }

    @PostMapping(path="/job/{id}/filter")
    public ResponseEntity filter(@PathVariable Long id, @RequestBody String constraints) {
        JsonObject cons = gson.fromJson(constraints, JsonObject.class);
        int dev = cons.get("dev").getAsInt();
        String MM = "BVAP";
        int MM_limit = 3;
//        Job job = jobrepo.findById(id).get();
        Job job = new Job("MD");
        return new ResponseEntity(job.filtered(cons), HttpStatus.OK);
    }

    public static void loadEnactedPlans(String state) {
        String path = "src/main/resources/Districts/" + state + "/" + state + "_enacted.json";
        ArrayList<District> enacted_districts = new ArrayList<>();
        try {
            JsonObject jobj = new Gson().fromJson(new FileReader(path), JsonObject.class);
            int cur = 0;
            for(String key: jobj.keySet()) {
                JsonObject districtObject = jobj.get(key).getAsJsonObject();
                JsonArray precinctArray = districtObject.getAsJsonArray("precincts");
                ArrayList<Integer> precincts = new ArrayList<>();
                if (precinctArray != null) {
                    for (int i=0;i<precinctArray.size();i++){
                        precincts.add(precinctArray.get(i).getAsInt());
                    }
                }
                enacted_districts.add(new District(cur, districtObject.get("vap").getAsInt(), districtObject.get("hvap").getAsInt(),
                        districtObject.get("wvap").getAsInt(), districtObject.get("bvap").getAsInt(), districtObject.get("asainvap").getAsInt(), precincts));
                cur ++;
            }
            System.out.println(enacted_districts.size() + " districts loaded from enacted plan");
            enacted.put(state, enacted_districts);
        }
        catch(Exception e) {
            System.out.println("Error " + e);
        }
    }

    public static void loadPlans(String state) {
         String path = "src/main/resources/Districts/" + state + "/" + state + "_plans.json";
//        String path = "src/main/resources/Districts/GA/Georgia-50.json";
        ArrayList<DistrictingPlan> district_plans = new ArrayList<>();
        try {
            JsonObject jobj = new Gson().fromJson(new FileReader(path), JsonObject.class);
            JsonArray arr = jobj.getAsJsonObject().getAsJsonArray("plans");
            for(JsonElement element: arr) {
                DistrictingPlan plan = new DistrictingPlan(element);
                district_plans.add(plan);
            }
            System.out.println("Found " + district_plans.size() + " plans...");
            districtPlans.put(state, district_plans);
        }
        catch(Exception e) {
            System.out.println("Error " + e);
        }
    }
}
