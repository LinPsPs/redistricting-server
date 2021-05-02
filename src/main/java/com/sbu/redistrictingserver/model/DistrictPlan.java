package com.sbu.redistrictingserver.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class DistrictPlan {

    public String state;
    public ArrayList<District> districts;

    public DistrictPlan(JsonElement districtsJson) {
        this.districts = new ArrayList<>();
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
            districts.add(new District(districtObject.get("districtNumber").getAsInt(), districtObject.get("hvap").getAsInt(),
                    districtObject.get("wvap").getAsInt(), districtObject.get("bvap").getAsInt(), precincts));
        }
    }
}
