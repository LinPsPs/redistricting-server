package com.sbu.redistrictingserver.geotools;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeoFile {

    private Map<Long, FeatureItem> featureGeometryMap = new HashMap<>();

    public GeoFile() {

    }

    public GeoFile(List<FeatureItem> featureItems) {
        for (FeatureItem featureItem : featureItems) {
            long id = featureItem.getId();
            featureGeometryMap.put(id, featureItem);
        }
    }

    public void loadFile(String filename) {
        try {
            JSONArray features = GeoUtils.openGeoJson(filename);
            for (int i = 0; i < features.size(); i++) {
                JSONObject feature = (JSONObject) features.get(i);
                JSONObject properties = (JSONObject) feature.get("properties");
                JSONObject geometry = (JSONObject) feature.get("geometry");
                long id = Long.parseLong(properties.get("ID").toString());
                long cd = Long.parseLong(properties.get("CD").toString());
                Geometry geometryObj = GeoUtils.jsonToGeometry(geometry.toJSONString());
                if (geometryObj != null) {
                    featureGeometryMap.put(id, new FeatureItem(geometryObj, id, cd));
                }
            }
        } catch (IOException | ParseException e) {
        }
    }


    public JSONObject createFeature(long id, long cd, Geometry geometry) {
        JSONObject jsonObject = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONObject geometryJson = GeoUtils.geometryToJson(geometry);
        properties.put("CD", String.valueOf(cd));
        properties.put("ID", (id));
        jsonObject.put("properties", properties);
        jsonObject.put("type", "Feature");
        jsonObject.put("geometry", geometryJson);
        return jsonObject;
    }

    public void writeFile(String filename) {
        String template = "{\n" +
                "  \"type\": \"FeatureCollection\",\n" +
                "  \"features\": [\n" +
                "  ]\n" +
                "}";
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(template);
            JSONArray features = (JSONArray) root.get("features");
            for (long id : featureGeometryMap.keySet()) {
                FeatureItem feature = featureGeometryMap.get(id);
                JSONObject featureJson = createFeature(feature.getId(), feature.getCd(), feature.getGeometry());
                features.add(featureJson);
            }
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(root.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (ParseException | IOException e) {
        }
    }

    public Map<Long, FeatureItem> getFeatureGeometryMap() {
        return featureGeometryMap;
    }

}
