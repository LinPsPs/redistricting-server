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

    private String state;
    private ArrayList<District> districts;

    public DistrictPlan(String state, String path) {
        this.state = state;
        this.districts = new ArrayList<>();
        try {
            JsonObject jobj = new Gson().fromJson(new FileReader(path), JsonObject.class);
            JsonArray arr = jobj.getAsJsonArray("plans");
            for(JsonElement districtsJson: arr) {
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
                    districts.add(new District(districtObject.get("districtNumber").getAsInt(), districtObject.get("HVAP").getAsInt(),
                            districtObject.get("WVAP").getAsInt(), districtObject.get("BVAP").getAsInt(), precincts));
                }
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e);
        }
    }
}
