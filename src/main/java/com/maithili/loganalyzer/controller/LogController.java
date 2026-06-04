package com.maithili.loganalyzer.controller;

import com.maithili.loganalyzer.model.Issue;
import com.maithili.loganalyzer.service.LogIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
@CrossOrigin(origins = "*")
public class LogController {

    @Autowired
    private LogIndexService logIndexService;

    @GetMapping("/summary")
    public Map<Integer, Integer> getSummary() {
        return logIndexService.getCodeSummary();
    }
    
    @GetMapping("/code/{code}")
    public List<String> getFilesByCode(@PathVariable("code") Integer code) {
        return logIndexService.getFilesByCode(code);
    }

    @GetMapping("/file/{fileName}")
    public String getFileContent(@PathVariable("fileName") String fileName) {
        return logIndexService.getFileContent(fileName);
    }

    @GetMapping("/file/{fileName}/issues")
    public List<Issue> getFileIssues(@PathVariable("fileName") String fileName) {
        return logIndexService.getIssuesByFile(fileName);
    }
    
    @GetMapping("/files")
    public Map<String, String> getAllFiles() {
        return logIndexService.getAllFiles();
    }
    @GetMapping("/severity/{level}")
    public List<Issue> getBySeverity(@PathVariable String level) {
        return logIndexService.getIssuesBySeverity(level);
    }
    @GetMapping("/search")
    public List<Issue> search(@RequestParam String q) {

        List<Issue> result = new ArrayList<>();

        for (List<Issue> issues : logIndexService.getAllIssues().values()) {
            for (Issue i : issues) {
                if (i.getLogLine() != null &&
                    i.getLogLine().toLowerCase().contains(q.toLowerCase())) {
                    result.add(i);
                }
            }
        }

        return result;
    }
}