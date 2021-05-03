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

public class DistrictPlan {

    public String state;
    public ArrayList<District> districts;
    public HashMap<District.MM, Integer> mm;
    public double popdiff;
    public double deviation;

    public DistrictPlan(JsonElement districtsJson) {
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
                    districtObject.get("wvap").getAsInt(), districtObject.get("bvap").getAsInt(), districtObject.get("asianvap").getAsInt(), precincts));
            vap.add(districtObject.get("vap").getAsInt());
        }
        long sum = 0;
        for(int pop: vap) {
            sum += pop;
        }
        this.popdiff = (Collections.max(vap) - Collections.min(vap)) / (1.0 * sum / vap.size());
        findMM();
    }

    public void findMM() {
        int count = 0;
        for(District d: this.districts) {
            if (d.mm == District.MM.BVAP) {
                if(!this.mm.containsKey(District.MM.BVAP)) {
                    this.mm.put(District.MM.BVAP, 0);
                }
                else {
                    this.mm.put(District.MM.BVAP, this.mm.get(District.MM.BVAP));
                }
            }
        }
    }

    public void calDeviation(ArrayList<District> enacted) {
        double diff = 0;
        for(int i = 0; i < enacted.size(); i++) {
            diff += Math.pow(enacted.get(i).VAP - districts.get(i).VAP, 2);
        }
        this.deviation = Math.pow(diff / enacted.size(), 0.5);
    }
}
