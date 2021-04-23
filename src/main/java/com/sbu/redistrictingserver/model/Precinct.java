package com.sbu.redistrictingserver.model;

import javax.persistence.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Entity

public class Precinct {
    @Id
    private int id;

    private double size;
}
