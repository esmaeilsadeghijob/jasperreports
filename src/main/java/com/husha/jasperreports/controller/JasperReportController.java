package com.husha.jasperreports.controller;

import com.husha.jasperreports.entity.JasperReports;
import com.husha.jasperreports.service.JasperReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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

    // آپلود فایل JRXML و ذخیره در پایگاه داده
    @PostMapping("/upload")
    public ResponseEntity<?> uploadReport(@RequestParam("file") MultipartFile file,
                                          @RequestParam int cid,
                                          @RequestParam int sid,
                                          @RequestParam int reportCode) {
        try {
            UUID reportId = jasperReportService.uploadReport(file.getBytes(), cid, sid, reportCode);
            return ResponseEntity.ok("Report uploaded successfully! ID: " + reportId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed.");
        }
    }

    // تبدیل فایل JRXML به PDF و نمایش آن
    @GetMapping("/preview/{id}")
    public ResponseEntity<?> previewReport(@PathVariable UUID id) {
            try {
            byte[] pdfBytes = jasperReportService.generatePdf(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf")
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("خطا در نمایش گزارش: " + e.getMessage());
        }
    }

    // دریافت لیست تمامی گزارش‌ها
    @GetMapping("/all")
    public ResponseEntity<List<JasperReports>> getAllReports() {
        List<JasperReports> reports = jasperReportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    // حذف گزارش بر اساس ID
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
