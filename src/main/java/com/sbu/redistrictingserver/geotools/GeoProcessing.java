package com.sbu.redistrictingserver.geotools;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeoProcessing {
    /**
     * process geojson file by precinct->district map
     *
     * @param input               the input file
     * @param precinctDistrictMap precinct->district map
     * @return new geojson file
     */
    public GeoFile processFile(GeoFile input, Map<Long, Long> precinctDistrictMap) {
        // all feature item
        List<FeatureItem> featureItems = new ArrayList<>();
        for (Map.Entry<Long, FeatureItem> entry : input.getFeatureGeometryMap().entrySet()) {
            Long id = entry.getKey();
            if (precinctDistrictMap.containsKey(id)) {
                // convert to district
                FeatureItem newItem = entry.getValue().cloneItem();
                // set new cd
                Long newcd = precinctDistrictMap.get(id);
                newItem.setCd(newcd);
                // add to featureItems
                featureItems.add(newItem);
            }
        }
        // build cd -> feature_set map
        Map<Long, Set<FeatureItem>> featureMap = new HashMap<>();
        // for each feature
        for (FeatureItem featureItem : featureItems) {
            // get cd
            long cd = featureItem.getCd();
            if (!featureMap.containsKey(cd)) {
                featureMap.put(cd, new HashSet<>());
            }
            // put feature to feature map
            featureMap.get(cd).add(featureItem);
        }

        // processed features
        List<FeatureItem> finalFeatures = new ArrayList<>();
        for (long cd : featureMap.keySet()) {
            // convert features to geometry
            Stream<Geometry> geolst = featureMap.get(cd).stream().map(FeatureItem::getGeometry);

//            UnaryUnionOp op = new UnaryUnionOp(geolst.collect(Collectors.toList()));
//            Geometry geometry = op.union();

            // union all geometry to one geometry
            GeometryFactory geoFac = new GeometryFactory();
            GeometryCollection geometryCollection = (GeometryCollection) geoFac.buildGeometry(geolst.collect(Collectors.toList()));
            Geometry geometry = geometryCollection.union();
            // add to final features
            finalFeatures.add(new FeatureItem(geometry, cd, cd));
        }
        // return a new file
        return new GeoFile(finalFeatures);

    }
}
