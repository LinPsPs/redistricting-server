package com.sbu.redistrictingserver.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="job")
@Getter
@Setter

public class Job {
    @Id
    private long id;

    @Column(name="state")
    private String state;

    @Column(name="compactness_limit")
    private double compactness_limit;

    @Column(name="slurm_DI")
    private int slurmID;

    // more to add
    public Job() {}

    @Override
    public String toString() {
        return "Job ID: " + this.id;
    }
}
