package com.sbu.redistrictingserver.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class DistrictingPlan {

    public String state;
    public ArrayList<District> districts;
    public HashMap<District.MM, Integer> mm;
    public double totalVap;
    public double popEqual;
    public double devFromAverage;
    public double devFromEnacted;
    public double areaDeviationSum;
    public double gcSum;
    public double objectiveFxnScore;

    public DistrictingPlan(JsonElement districtsJson) {
        this.districts = new ArrayList<>();
        this.mm = new HashMap<>();
        ArrayList<Integer> vap = new ArrayList<>();
        JsonArray districtArray = districtsJson.getAsJsonObject().getAsJsonArray("districts");
        for(JsonElement district: districtArray) {
            JsonObject districtObject = district.getAsJsonObject();
            JsonArray precinctArray = districtObject.getAsJsonArray("precincts");
            ArrayList<Integer> precincts = new ArrayList<>();
            if (precinctArray != null) {
                for (int i=0;i<precinctArray.size();i++){
                    precincts.add(precinctArray.get(i).getAsInt());
                }
            }
            districts.add(new District(districtObject.get("renumber").getAsInt(), districtObject.get("vap").getAsInt(), districtObject.get("hvap").getAsInt(),
                    districtObject.get("wvap").getAsInt(), districtObject.get("bvap").getAsInt(), districtObject.get("asianvap").getAsInt(),
                    districtObject.get("aredev").getAsDouble(), districtObject.get("gc").getAsDouble(), precincts));
            vap.add(districtObject.get("vap").getAsInt());
            this.areaDeviationSum +=  districtObject.get("aredev").getAsDouble();
            this.gcSum += districtObject.get("gc").getAsDouble();
        }
        for(int pop: vap) {
            this.totalVap += pop;
        }
        calEqualPopulation();
        findMM();
    }

    public void findMM() {
        int count = 0;
        for(District d: this.districts) {
            if (d.mm == District.MM.BVAP) {
                this.mm.put(District.MM.BVAP, this.mm.getOrDefault(District.MM.BVAP, 0));
            }
        }
    }

    public void calEqualPopulation() {
        double ideaPop = (int) (this.totalVap / this.districts.size());
        double popVar = 0;
        for(District d: this.districts) {
            popVar += Math.pow((int) (d.VAP / ideaPop) - 1, 2);
        }
        this.popEqual = Math.pow(popVar, 0.5);
    }

    public void calDevFromEnacted(ArrayList<District> enacted) {
        double diff = 0;
        for(int i = 0; i < enacted.size(); i++) {
            diff += Math.pow(enacted.get(i).VAP - districts.get(i).VAP, 2);
        }
        this.devFromEnacted = Math.pow(diff / enacted.size(), 0.5);
    }

    public void calDevFromAverage(ArrayList<District> ave) {
        double diff = 0;
        for(int i = 0; i < ave.size(); i++) {
            diff += Math.pow((districts.get(i).VAP - ave.get(i).VAP) * 1.0 / ave.get(i).VAP, 2);
        }
        this.devFromAverage = Math.pow(diff / ave.size(), 0.5);
    }

    /**
     * Json popEq; devFromAve; devFromEnacted; gc
     */
    public void getObjectiveFxnScore(JsonElement weight) {
        double popEqW = weight.getAsJsonObject().get("popEq").getAsDouble();
        double devFromAveW = weight.getAsJsonObject().get("devFromAve").getAsDouble();
        double devFromEnactedW = weight.getAsJsonObject().get("devFromEnacted").getAsDouble();
        double gcW = weight.getAsJsonObject().get("gc").getAsDouble();
        this.objectiveFxnScore = popEqW * this.popEqual + devFromEnactedW * this.devFromEnacted + devFromAveW * this.devFromAverage + gcW * this.gcSum;
    }
}
