package com.sbu.redistrictingserver.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.persistence.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

@Entity

public class Job {
    @Id
    private long id;

    private String state;

    private double compactness_limit;

    private int slurmID;

    private String districtPlanPath;

    @Transient
    private final ArrayList<DistrictPlan> districtPlans;

    public Job() {
        this.districtPlans = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Job ID: " + this.id + " " + state + " " + compactness_limit + " " + slurmID + " " + districtPlanPath;
    }

    public void run() {

    }

    public void loadPlans(String path) {
//        DistrictPlan p = new DistrictPlan("src/main/resources/Districts/GA/ga-demo.json");
        try {
            JsonObject jobj = new Gson().fromJson(new FileReader(path), JsonObject.class);
            JsonArray arr = jobj.getAsJsonArray("plans");
            for(JsonElement districtsJson: arr) {
                DistrictPlan plan = new DistrictPlan(districtsJson);
                districtPlans.add(plan);
            }
            System.out.println("Found " + districtPlans.size() + " plans...");
        }
        catch(Exception e) {
            System.out.println("Error " + e);
        }
    }

    public String getBoxandWhiskerPlot(String minority) {
        int districtNum = districtPlans.get(0).districts.size();
        ArrayList<ArrayList<Integer>> plotData = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> plot = new HashMap<>();
        for(int i = 0; i < districtNum; i++) {
            plotData.add(new ArrayList<>());
        }
        int cur;
        for(DistrictPlan plan: districtPlans) {
            cur = 0;
            for(District district: plan.districts) {
                int pop = 0;
                switch (minority) {
                    case "BVAP":
                        pop = district.BVAP;
                        break;
                    case "WVAP":
                        pop = district.WVAP;
                        break;
                    case "HVAP":
                        pop = district.HVAP;
                        break;
                    default:
                        break;
                }
                plotData.get(cur).add(pop);
                cur ++;
            }
        }
        System.out.println("Finish adding pop data. Check " + plotData.get(0).size());
        cur = 0;
        for(ArrayList<Integer> id: plotData) {
            int min = Collections.min(id);
            int max = Collections.max(id);
            int q1 = districtPlans.size() / 4;
            int q2 = q1 * 2;
            int q3 = q1 * 3;
            Collections.sort(id);
            plot.put(cur, new ArrayList<>(Arrays.asList(min, id.get(q1), id.get(q2), id.get(q3), max)));
            cur ++;
        }
        System.out.println("Plot is ready, check " + plot.size());
        return new Gson().toJson(plot);
    }
}
