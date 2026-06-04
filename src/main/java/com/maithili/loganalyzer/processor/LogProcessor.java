package com.maithili.loganalyzer.processor;

import com.maithili.loganalyzer.model.Issue;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LogProcessor {

    // return code pattern:
    // supports:
    // return code: 88
    // return code from java: 111
    private static final Pattern RC_PATTERN =
            Pattern.compile("(?i)return\\s*code[^\\d]*(\\d+)");

    public List<Issue> processLogs(List<String> lines,
                                   String fileName,
                                   Map<Integer, Integer> codeCount) {

        List<Issue> issues = new ArrayList<>();

        if (lines == null || lines.isEmpty()) {
            return issues;
        }

        for (String line : lines) {

            if (line == null) continue;

            Integer code = detectCustomErrorCode(line);

            // fallback to return-code extraction
            if (code == null) {
                code = extractReturnCode(line);
            }

            // ignore if nothing found or success
            if (code == null || code == 0) continue;

            // count occurrences
            codeCount.put(code, codeCount.getOrDefault(code, 0) + 1);

            Issue issue = new Issue();

            // 🔥 IMPORTANT: attach file name for UI drill-down
            issue.setFileName(fileName);

            issue.setLogLine(line);
            issue.setErrorCode(code);

            switch (code) {

                case 45:
                    issue.setSeverity("MEDIUM");
                    issue.setMessage("E4ALL Configuration Issue / Connection Issue");
                    break;

                case 88:
                    issue.setSeverity("MEDIUM");
                    issue.setMessage("Server Issue");
                    break;

                case 111:
                    issue.setSeverity("CRITICAL");
                    issue.setMessage("Code issue");
                    break;

                case 55:
                    issue.setSeverity("CRITICAL");
                    issue.setMessage("Code issue");
                    break;

                default:
                    issue.setSeverity("UNKNOWN");
                    issue.setMessage("Unhandled error code");
                    break;
            }

            issues.add(issue);
        }

        return issues;
    }

    /**
     * Custom rule-based detection (highest priority)
     */
    private Integer detectCustomErrorCode(String line) {

        if (line == null) return null;

        String lower = line.toLowerCase();

        // Rule: E4ALL config issue → 45
        if (lower.contains("e4all configs list not found")) {
            return 45;
        }

        return null;
    }

    /**
     * Extract return code from logs
     * Example:
     * return code: 88
     * return code from java: 111
     */
    private Integer extractReturnCode(String line) {

        Matcher matcher = RC_PATTERN.matcher(line);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }
}