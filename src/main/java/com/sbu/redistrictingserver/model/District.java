package com.sbu.redistrictingserver.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class District {

    public int districtNum;
    public int HVAP;
    public int WVAP;
    public int BVAP;
    public ArrayList<Integer> precincts;

    public District(int districtNum, int HVAP, int WVAP, int BVAP, ArrayList<Integer> precincts) {
        this.districtNum = districtNum;
        this.HVAP = HVAP;
        this.WVAP = WVAP;
        this.BVAP = BVAP;
        this.precincts = precincts;
    }

    @Override
    public String toString() {
        return this.districtNum + " " + this.HVAP + " " + this.WVAP + " " + this.BVAP + " " + this.precincts.toString();
    }
}
