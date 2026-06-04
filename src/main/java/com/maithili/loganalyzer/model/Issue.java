package com.maithili.loganalyzer.model;

/**
 * Represents a single issue found in log file
 */
public class Issue {

    // Original log line
    private String logLine;

    // Extracted return/error code
    private int errorCode;

    // Human-readable explanation
    private String message;

    // Severity level (LOW / MEDIUM / HIGH / CRITICAL / UNKNOWN)
    private String severity;
    
    private String fileName;

    // -------------------------
    // Getters and Setters
    // -------------------------

    public String getLogLine() {
        return logLine;
    }

    public void setLogLine(String logLine) {
        this.logLine = logLine;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

	public void setFileName(String fileName) {
		// TODO Auto-generated method stub
		this.fileName=fileName;
	}
}