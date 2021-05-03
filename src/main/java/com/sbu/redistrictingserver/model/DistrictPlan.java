package com.sbu.redistrictingserver.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;

public class DistrictPlan {

    public String state;
    public ArrayList<District> districts;
    public double popdiff;
    public double deviation;

    public DistrictPlan(JsonElement districtsJson) {
        this.districts = new ArrayList<>();
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
                    districtObject.get("wvap").getAsInt(), districtObject.get("bvap").getAsInt(), districtObject.get("asianvap").getAsInt(), precincts));
            vap.add(districtObject.get("vap").getAsInt());
        }
        long sum = 0;
        for(int pop: vap) {
            sum += pop;
        }
        this.popdiff = (Collections.max(vap) - Collections.min(vap)) / (1.0 * sum / vap.size());
    }

    public void calDeviation(ArrayList<District> enacted) {
        double diff = 0;
        for(int i = 0; i < enacted.size(); i++) {
            diff += Math.pow(enacted.get(i).VAP - districts.get(i).VAP, 2);
        }
        this.deviation = diff / enacted.size();
    }
}
