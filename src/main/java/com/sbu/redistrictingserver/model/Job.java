package com.sbu.redistrictingserver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity

public class Job {
    @Id
    private long id;

    private String state;

    private double compactness_limit;

    private int slurmID;

    private String districtPlanPath;

    @Transient
    private ArrayList<DistrictPlan> districtPlans;

    public void loadDistrictPlans() {

    }

    @Override
    public String toString() {
        return "Job ID: " + this.id + " " + state + " " + compactness_limit + " " + slurmID + " " + districtPlanPath;
    }
}
