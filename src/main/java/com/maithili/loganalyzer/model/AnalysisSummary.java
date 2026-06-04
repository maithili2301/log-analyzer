package com.maithili.loganalyzer.model;

import java.util.Map;

/**
 * Summary of analysis
 */
public class AnalysisSummary {

    private int totalFiles;
    private int totalIssues;

    // Count based on severity (HIGH, LOW, etc.)
    private Map<String, Integer> severityCount;

    // Count based on return codes (45, 99, 111, etc.)
    private Map<Integer, Integer> codeCount;

    public AnalysisSummary(int totalFiles, int totalIssues,
                           Map<String, Integer> severityCount,
                           Map<Integer, Integer> codeCount) {
        this.totalFiles = totalFiles;
        this.totalIssues = totalIssues;
        this.severityCount = severityCount;
        this.codeCount = codeCount;
    }

    // Getters
    public int getTotalFiles() { return totalFiles; }
    public int getTotalIssues() { return totalIssues; }
    public Map<String, Integer> getSeverityCount() { return severityCount; }
    public Map<Integer, Integer> getCodeCount() { return codeCount; }
}