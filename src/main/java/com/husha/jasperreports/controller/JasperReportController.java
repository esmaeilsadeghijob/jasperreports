package com.husha.jasperreports.controller;

import com.husha.jasperreports.entity.JasperReport;
import com.husha.jasperreports.service.JasperReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Reports", description = "Operations related to JasperReports")
@RestController
@RequestMapping("/api/reports")
public class JasperReportController {
    private final JasperReportService jasperReportService;

    public JasperReportController(JasperReportService jasperReportService) {
        this.jasperReportService = jasperReportService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadReport(@RequestParam("file") MultipartFile file,
                                          @RequestParam int cid,
                                          @RequestParam int sid,
                                          @RequestParam int reportCode) {
        try {
            JasperReport report = jasperReportService.uploadReport(file.getBytes(), cid, sid, reportCode);
            return ResponseEntity.ok("Report uploaded successfully! ID: " + report.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed.");
        }
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<?> previewReport(@PathVariable UUID id) {
        Optional<JasperReport> report = jasperReportService.getReport(id);
        return report.map(r -> ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(r.getReportContent())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<JasperReport>> getAllReports() {
        List<JasperReport> reports = jasperReportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable UUID id) {
        try {
            jasperReportService.deleteReport(id);
            return ResponseEntity.ok("Report deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed.");
        }
    }

}
