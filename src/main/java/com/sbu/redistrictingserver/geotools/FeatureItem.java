package com.sbu.redistrictingserver.geotools;
import com.vividsolutions.jts.geom.Geometry;

public class FeatureItem {
    private Geometry geometry;
    private long id;
    private long cd;

    public FeatureItem(Geometry geometry, long id, long cd) {
        this.geometry = geometry;
        this.id = id;
        this.cd = cd;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCd() {
        return cd;
    }

    public void setCd(long cd) {
        this.cd = cd;
    }

    public FeatureItem cloneItem() {
        return new FeatureItem(geometry, id, cd);
    }


}
