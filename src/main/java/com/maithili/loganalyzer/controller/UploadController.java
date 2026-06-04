package com.maithili.loganalyzer.controller;

import com.maithili.loganalyzer.model.AnalysisResponse;
import com.maithili.loganalyzer.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private LogService logService;

    /**
     * Upload multiple log files
     */
    @PostMapping("/files")
    public AnalysisResponse uploadFiles(
            @RequestParam("files") List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            throw new RuntimeException("No files uploaded");
        }

        return logService.analyzeFiles(files);
    }

    /**
     * Upload log file from URL
     */
    @PostMapping("/url")
    public AnalysisResponse uploadFromUrl(@RequestParam String fileUrl) {
        return logService.analyzeFromUrl(fileUrl);
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public String health() {
        return "Log Analyzer Running 🚀";
    }
}