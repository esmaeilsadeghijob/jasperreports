package com.husha.jasperreports.controller;

import com.husha.jasperreports.config.KeyConstant;
import com.husha.jasperreports.service.ReportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportGenerationController {

    @Autowired
    private ReportTemplateService templateService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateReport(@RequestParam String templateName,
                                                 @RequestParam String format,
                                                 @RequestBody Map<String, Object> parameters) throws Exception {
        if (!List.of(KeyConstant.REPO_TYPE_PDF, KeyConstant.REPO_TYPE_XLS, KeyConstant.REPO_TYPE_HTML,
                KeyConstant.REPO_TYPE_CSV, KeyConstant.REPO_TYPE_TXT).contains(format)) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] reportBytes = templateService.generateReport(templateName, parameters, format);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + templateName + "." + format.toLowerCase())
                .body(reportBytes);
    }

}

