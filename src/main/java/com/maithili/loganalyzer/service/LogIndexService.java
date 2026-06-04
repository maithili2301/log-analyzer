package com.maithili.loganalyzer.service;

import com.maithili.loganalyzer.model.Issue;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LogIndexService {

    /**
     * errorCode → set of fileIds
     */
    private final Map<Integer, Set<String>> codeToFiles = new HashMap<>();

    /**
     * fileId → issues
     */
    private final Map<String, List<Issue>> fileToIssues = new HashMap<>();

    /**
     * fileId → full content
     */
    private final Map<String, String> fileContent = new HashMap<>();

    /**
     * fileId → original file name (for UI)
     */
    private final Map<String, String> fileNameMap = new HashMap<>();

    /**
     * Index file data
     */
    public void indexFile(String fileId,
                          List<String> lines,
                          List<Issue> issues) {

        if (fileId == null) return;

        // store full content
        if (lines != null) {
            fileContent.put(fileId, String.join("\n", lines));
        }

        // store issues + build reverse index
        if (issues != null) {

            fileToIssues.put(fileId, issues);

            for (Issue issue : issues) {

                if (issue == null) continue;

                int code = issue.getErrorCode();

                // ignore success logs
                if (code == 0) continue;

                codeToFiles
                        .computeIfAbsent(code, k -> new HashSet<>())
                        .add(fileId);
            }
        }
    }

    /**
     * Map fileId → original file name
     */
    public void mapFileName(String fileId, String fileName) {
        if (fileId == null || fileName == null) return;
        fileNameMap.put(fileId, fileName);
    }

    /**
     * Summary: errorCode → number of files
     */
    public Map<Integer, Integer> getCodeSummary() {

        Map<Integer, Integer> summary = new HashMap<>();

        for (Map.Entry<Integer, Set<String>> entry : codeToFiles.entrySet()) {
            summary.put(entry.getKey(), entry.getValue().size());
        }

        return summary;
    }

    /**
     * Get files for error code (returns original names)
     */
    public List<String> getFilesByCode(Integer code) {

        if (code == null) return Collections.emptyList();

        Set<String> fileIds = codeToFiles.getOrDefault(code, Collections.emptySet());

        List<String> result = new ArrayList<>();

        for (String fileId : fileIds) {
            result.add(fileNameMap.getOrDefault(fileId, fileId));
        }

        return result;
    }

    /**
     * Get full file content using file name (UI friendly)
     */
    public String getFileContent(String fileName) {

        if (fileName == null) return null;

        String fileId = getFileIdByName(fileName);

        if (fileId == null) return null;

        return fileContent.get(fileId);
    }

    /**
     * Get issues of a file
     */
    public List<Issue> getIssuesByFile(String fileName) {

        if (fileName == null) return Collections.emptyList();

        String fileId = getFileIdByName(fileName);

        if (fileId == null) return Collections.emptyList();

        return fileToIssues.getOrDefault(fileId, Collections.emptyList());
    }

    /**
     * Reverse lookup: filename → fileId
     */
    private String getFileIdByName(String fileName) {

        for (Map.Entry<String, String> entry : fileNameMap.entrySet()) {
            if (entry.getValue().equals(fileName)) {
                return entry.getKey();
            }
        }

        return null;
    }
    public Map<String, String> getAllFiles() {
        return fileNameMap; // fileId → originalName
    }
    public List<Issue> getIssuesBySeverity(String level) {

        List<Issue> result = new ArrayList<>();

        for (List<Issue> list : fileToIssues.values()) {
            for (Issue i : list) {
                if (i.getSeverity().equalsIgnoreCase(level)) {
                    result.add(i);
                }
            }
        }

        return result;
    }
    public Map<String, List<Issue>> getAllIssues() {
        return fileToIssues;
    }
    public void clear() {
        codeToFiles.clear();
        fileToIssues.clear();
        fileContent.clear();
        fileNameMap.clear();
    }
}