package com.sbu.redistrictingserver.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sbu.redistrictingserver.controller.JobController;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.*;

@Entity

public class Job {
    @Id
    private long id;

    private String state;

    private double compactness_limit;

    private int slurmID;

    private String districtPlanPath;

    @Transient
    private final ArrayList<DistrictingPlan> districtingPlans;

    @Transient
    private final ArrayList<District> enacted;

    @Transient
    ArrayList<District> average;

    @Transient
    private ArrayList<DistrictingPlan> filtered;

    public Job() {
        this("MD");
    }

    public Job(String state) {
        this.state = state;
        this.districtingPlans = JobController.districtPlans.get(state);
        this.enacted = JobController.enacted.get(state);
        this.filtered = new ArrayList<>();
        this.calDevFromEnacted();
        this.calDevFromAverage();
    }

    @Override
    public String toString() {
        return "Job ID: " + this.id + " " + state + " " + compactness_limit + " " + slurmID + " " + districtPlanPath;
    }

    public void run() {

    }

    public int filtered(JsonObject cons) {
        this.filtered = new ArrayList<>();
        for(DistrictingPlan plan : this.districtingPlans) {
            // get Majority minority type
            District.MM type;
            if(cons.get("MM_Type").getAsString().equals("BVAP")) {
                type = District.MM.BVAP;
            }
            else {
                type = District.MM.AVAP;
            }
            if(plan.popEqual < cons.get("dev").getAsInt() &&
                    plan.gc < cons.get("gc").getAsDouble() &&
                    plan.mm.get(type) < cons.get("MM_Limit").getAsInt()) {
                this.filtered.add(plan);
            }
        }
        return this.filtered.size();
    }

    public ArrayList<DistrictingPlan> getTop10() {
        ArrayList<DistrictingPlan> top10 = new ArrayList<>();
        Collections.sort(this.districtingPlans, Comparator.comparingDouble(o -> o.objectiveFxnScore));
        for(int i = 0; i < 10; i++) {
            top10.add(this.districtingPlans.get(i));
        }
        return top10;
    }

    public void calDevFromEnacted() {
        for(DistrictingPlan plan: this.districtingPlans) {
            plan.calDevFromEnacted(this.enacted);
        }
    }

    public void calDevFromAverage() {
        Collections.sort(this.districtingPlans, Comparator.comparingDouble(o -> o.popEqual));
        this.average = this.districtingPlans.get(this.districtingPlans.size() / 2).districts;
        for(DistrictingPlan plan: this.districtingPlans) {
            plan.calDevFromAverage(this.enacted);
        }
    }

    public String getBoxandWhiskerPlot(String minority) {
        int districtNum = districtingPlans.get(0).districts.size();
        ArrayList<ArrayList<Integer>> plotData = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> plot = new HashMap<>();
        for(int i = 0; i < districtNum; i++) {
            plotData.add(new ArrayList<>());
        }
        int cur;
        for(DistrictingPlan plan: districtingPlans) {
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
            int q1 = districtingPlans.size() / 4;
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
