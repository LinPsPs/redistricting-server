package com.sbu.redistrictingserver.model;

import javax.persistence.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Entity

public class Job {
    @Id
    private long id;

    private String state;

    private double compactness_limit;

    private int slurmID;

    @Override
    public String toString() {
        return "Job ID: " + this.id + " " + state + " " + compactness_limit + " " + slurmID;
    }
}
