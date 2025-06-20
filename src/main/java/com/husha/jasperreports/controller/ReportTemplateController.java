package com.husha.jasperreports.controller;

import com.husha.jasperreports.service.ReportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/reports/jrxml/templates")
public class ReportTemplateController {

    @Autowired
    private ReportTemplateService templateService;

    @Autowired
    private ReportTemplateService  reportTemplateService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTemplate(@RequestParam("name") String name,
                                                 @RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        templateService.saveTemplate(name, content);
        return ResponseEntity.ok("تمپلیت با موفقیت ذخیره شد!");
    }

    @GetMapping("/select/{name}")
    public ResponseEntity<String> getTemplate(@PathVariable String name) {
        try {
            String templateContent = templateService.getTemplateContent(name);
            return ResponseEntity.ok(templateContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("تمپلیت یافت نشد!");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTemplate(@RequestParam(required = false) String templateName,
                                                 @RequestBody(required = false) Map<String, String> fields) {
        try {
            reportTemplateService.createJrxmlTemplate(templateName, fields);
            return ResponseEntity.ok("تمپلیت '" + templateName + "' با موفقیت ایجاد شد!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("خطا در ایجاد تمپلیت: " + e.getMessage());
        }
    }

}
