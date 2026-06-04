package com.maithili.loganalyzer.model;

import java.util.List;

/**
 * Final response sent to UI / Postman
 */
public class AnalysisResponse {

    private List<Issue> issues;
    private AnalysisSummary summary;

    public AnalysisResponse(List<Issue> issues, AnalysisSummary summary) {
        this.issues = issues;
        this.summary = summary;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public AnalysisSummary getSummary() {
        return summary;
    }
}