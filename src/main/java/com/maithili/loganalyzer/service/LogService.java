package com.maithili.loganalyzer.service;

import com.maithili.loganalyzer.model.*;
import com.maithili.loganalyzer.processor.LogProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@Service
public class LogService {

    @Autowired
    private LogProcessor logProcessor;

    @Autowired
    private LogIndexService logIndexService;

    /**
     * Upload and analyze files
     */
    public AnalysisResponse analyzeFiles(List<MultipartFile> files) {

        List<Issue> allIssues = new ArrayList<>();
        Map<Integer, Integer> codeCount = new HashMap<>();

        try {
            for (MultipartFile file : files) {

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(file.getInputStream()));

                List<String> lines = new ArrayList<>();
                String line;

                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }

                String fileId = UUID.randomUUID().toString();
                String originalName = file.getOriginalFilename();

                // ✅ PROCESS LOGS USING fileId
                List<Issue> issues =
                        logProcessor.processLogs(lines, fileId, codeCount);

                allIssues.addAll(issues);

                // ✅ INDEX FILE CONTENT + ISSUES
                logIndexService.indexFile(fileId, lines, issues);

                // ✅ MAP fileId → fileName (for UI)
                logIndexService.mapFileName(fileId, originalName);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error processing files", e);
        }

        return buildResponse(allIssues, files.size(), codeCount);
    }

    /**
     * Upload from URL
     */
    public AnalysisResponse analyzeFromUrl(String fileUrl) {

        List<Issue> issues;
        //clean build if getting error
        Map<Integer,Integer> codeCount = new HashMap<>();

        try {
            URL url = new URL(fileUrl);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            String fileId = UUID.randomUUID().toString();

            issues = logProcessor.processLogs(lines, fileId, codeCount);

            logIndexService.indexFile(fileId, lines, issues);
            logIndexService.mapFileName(fileId, fileUrl);

        } catch (Exception e) {
            throw new RuntimeException("Error reading file from URL", e);
        }

        return buildResponse(issues, 1, codeCount);
    }

    /**
     * Build response
     */
    private AnalysisResponse buildResponse(List<Issue> issues,
                                           int totalFiles,
                                           Map<Integer, Integer> codeCount) {

        Map<String, Integer> severityCount = new HashMap<>();

        for (Issue issue : issues) {
            severityCount.put(
                    issue.getSeverity(),
                    severityCount.getOrDefault(issue.getSeverity(), 0) + 1
            );
        }

        AnalysisSummary summary = new AnalysisSummary(
                totalFiles,
                issues.size(),
                severityCount,
                codeCount
        );

        return new AnalysisResponse(issues, summary);
    }
}