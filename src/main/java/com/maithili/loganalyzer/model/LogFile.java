package com.maithili.loganalyzer.model;

import java.util.List;

public class LogFile {

    private String fileName;
    private List<String> lines;

    public LogFile(String fileName, List<String> lines) {
        this.fileName = fileName;
        this.lines = lines;
    }

    public String getFileName() {
        return fileName;
    }

    public List<String> getLines() {
        return lines;
    }
}