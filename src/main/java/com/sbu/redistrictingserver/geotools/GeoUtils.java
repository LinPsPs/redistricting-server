package com.sbu.redistrictingserver.geotools;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class GeoUtils {
    // geojson formatter
    static GeometryJSON gjson = new GeometryJSON(15);


    /**
     * open geojson file
     *
     * @param filename geojson file name
     * @return json array of features
     */
    public static JSONArray openGeoJson(String filename) throws IOException, ParseException {
        FileReader reader = new FileReader(filename);
        JSONParser parser = new JSONParser();
        JSONObject jsonRoot = (JSONObject) parser.parse(reader);
        if (jsonRoot.containsKey("md_f")) {
            JSONObject md_f = (JSONObject) jsonRoot.get("md_f");
            JSONArray features = (JSONArray) md_f.get("features");
            reader.close();
            return features;
        } else {
            JSONArray features = (JSONArray) jsonRoot.get("features");
            reader.close();
            return features;

        }
    }

    /**
     * convert geojson to Geometry
     * @param geoJson geojson string
     * @return Geometry
     */
    public static Geometry jsonToGeometry(String geoJson) {
        Reader reader = new StringReader(geoJson);
        try {
            return gjson.read(reader);
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * convert geometry to geojson
     * @param geometry geometry
     * @return geojson feature
     */
    public static JSONObject geometryToJson(Geometry geometry) {
        StringWriter stringWriter = new StringWriter();
        try {
            gjson.write(geometry, stringWriter);
            String jsonStr = stringWriter.toString();
            JSONParser jsonParser = new JSONParser();
            return (JSONObject) jsonParser.parse(jsonStr);
        } catch (IOException | ParseException e) {
        }
        return null;
    }

}
