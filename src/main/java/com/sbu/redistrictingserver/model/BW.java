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
@Table(name="BW")
@Getter
@Setter

public class BW {
    @Id
    private long bwId;

    @Column(name="jobid")
    private long jobid;
}
