package com.sbu.redistrictingserver.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class District {

    enum MM {
        HVAP,
        WVAP,
        BVAP,
        AVAP
    }

    public int districtNum;
    public int VAP;
    public int HVAP;
    public int WVAP;
    public int BVAP;
    public int AVAP;
    public MM mm;
    public int MMVAP;
    public ArrayList<Integer> precincts;

    public District(int districtNum, int VAP, int HVAP, int WVAP, int BVAP, int AVAP, ArrayList<Integer> precincts) {
        this.districtNum = districtNum;
        this.VAP = VAP;
        this.HVAP = HVAP;
        this.WVAP = WVAP;
        this.BVAP = BVAP;
        this.AVAP = AVAP;
        this.precincts = precincts;
    }

    @Override
    public String toString() {
        return this.districtNum + " " + this.HVAP + " " + this.WVAP + " " + this.BVAP + " " + this.precincts.toString();
    }
}
