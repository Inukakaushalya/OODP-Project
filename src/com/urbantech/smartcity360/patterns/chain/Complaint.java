package com.urbantech.smartcity360.patterns.chain;

import java.util.ArrayList;
import java.util.List;

/** The request that travels along the chain (Part C). It also records where it has been (trace). */
public class Complaint {

    private final int id;
    private final String citizen;
    private final String category;
    private final int severity;
    private final String description;
    private String handledBy = "Not handled";
    private final List<String> trace = new ArrayList<>();

    public Complaint(int id, String citizen, String category, int severity, String description) {
        this.id = id;
        this.citizen = citizen;
        this.category = category;
        this.severity = severity;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getCitizen() {
        return citizen;
    }

    public String getCategory() {
        return category;
    }

    public int getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public void addTrace(String line) {
        trace.add(line);
    }

    public String getTraceText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trace.size(); i++) {
            sb.append(i + 1).append(". ").append(trace.get(i)).append("\n");
        }
        return sb.toString();
    }
}
