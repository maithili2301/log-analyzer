package com.maithili.loganalyzer.model;

public class Issue {

    private String logLine;
    private int errorCode;
    private String message;
    private String severity;
    private String fileName;

    // ✅ NEW FIELDS
    private String className;
    private String exceptionType;

    // Getters & Setters

    public String getLogLine() { return logLine; }
    public void setLogLine(String logLine) { this.logLine = logLine; }

    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getExceptionType() { return exceptionType; }
    public void setExceptionType(String exceptionType) { this.exceptionType = exceptionType; }
}