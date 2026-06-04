package com.maithili.loganalyzer.processor;

import com.maithili.loganalyzer.model.Issue;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LogProcessor {

    private static final Pattern RC_PATTERN =
            Pattern.compile("(?i)return\\s*code[^\\d]*(\\d+)");

    private static final Pattern CLASS_PATTERN =
            Pattern.compile("at\\s+([a-zA-Z0-9_.]+)\\.");

    private static final Pattern EXCEPTION_PATTERN =
            Pattern.compile("([a-zA-Z0-9_.]*Exception)");

    // ✅ NEW: E4ALL custom error detection
    private static final Pattern E4ALL_PATTERN =
            Pattern.compile(".*E4ALL configs list not found.*", Pattern.CASE_INSENSITIVE);

    public List<Issue> processLogs(List<String> lines,
                                    String fileName,
                                    Map<Integer, Integer> codeCount) {

        List<Issue> issues = new ArrayList<>();

        List<String> block = new ArrayList<>();
        Integer code = null;

        boolean capturing = false;

        for (String line : lines) {

            if (line == null || line.trim().isEmpty()) continue;

            Integer extractedCode = extractReturnCode(line);

            // ---------------------------------------------------
            // ✅ FIX 1: Handle standalone E4ALL error immediately
            // ---------------------------------------------------
            if (E4ALL_PATTERN.matcher(line).find()) {

                List<String> singleBlock = new ArrayList<>();
                singleBlock.add(line);

                Issue issue = buildIssue(singleBlock, fileName, 45);

                issues.add(issue);
                continue;
            }

            // ---------------------------------------------------
            // START BLOCK ON STACKTRACE
            // ---------------------------------------------------
            if (isStackLine(line)) {
                capturing = true;
                block.add(line);
                continue;
            }

            // ---------------------------------------------------
            // CAPTURE RETURN CODE (END BLOCK)
            // ---------------------------------------------------
            if (capturing && extractedCode != null) {

                code = extractedCode;

                block.add(line);

                issues.add(buildIssue(block, fileName, code));

                block.clear();
                capturing = false;
                code = null;

                continue;
            }

            // ---------------------------------------------------
            // CONTINUE BLOCK
            // ---------------------------------------------------
            if (capturing) {
                block.add(line);
            }
        }

        // ---------------------------------------------------
        // Update code counts
        // ---------------------------------------------------
        for (Issue i : issues) {
            codeCount.put(
                    i.getErrorCode(),
                    codeCount.getOrDefault(i.getErrorCode(), 0) + 1
            );
        }

        return issues;
    }

    // =====================================================
    // ISSUE BUILDER
    // =====================================================
    private Issue buildIssue(List<String> block,
                             String fileName,
                             Integer code) {

        Issue issue = new Issue();

        issue.setFileName(fileName);
        issue.setLogLine(String.join(" | ", block));

        int finalCode = (code != null) ? code : 999;

        issue.setErrorCode(finalCode);
        issue.setSeverity(mapSeverity(finalCode));

        String className = "Unknown";
        String exception = "None";

        for (String l : block) {

            Matcher cm = CLASS_PATTERN.matcher(l);
            if (cm.find()) {
                className = cm.group(1);
            }

            Matcher em = EXCEPTION_PATTERN.matcher(l);
            if (em.find()) {
                exception = em.group(1);
            }

            if (l.startsWith("Caused by")) {
                exception = l.replace("Caused by:", "").trim();
            }
        }

        issue.setClassName(className);
        issue.setExceptionType(exception);

        return issue;
    }

    // =====================================================
    // STACKTRACE DETECTION
    // =====================================================
    private boolean isStackLine(String line) {
        return line.startsWith("\tat ")
                || line.startsWith("Caused by")
                || line.contains(".java:");
    }

    // =====================================================
    // RETURN CODE EXTRACTION
    // =====================================================
    private Integer extractReturnCode(String line) {
        Matcher matcher = RC_PATTERN.matcher(line);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : null;
    }

    // =====================================================
    // SEVERITY MAPPING
    // =====================================================
    private String mapSeverity(int code) {
        switch (code) {
            case 45:
            	return "MEDIUM";
            case 88:
                return "MEDIUM";
            case 111:
            	return "CRITICAL";
            case 55:
                return "CRITICAL";
            default:
                return "UNKNOWN";
        }
    }
}