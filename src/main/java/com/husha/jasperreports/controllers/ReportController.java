package com.husha.jasperreports.controllers;

import com.husha.jasperreports.entities.JasperReportEntity;
import com.husha.jasperreports.entities.JasperReportParam;
import com.husha.jasperreports.repositories.JasperReportRepository;
import com.husha.jasperreports.repositories.JasperReportParamRepository;
import com.husha.jasperreports.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Tag(name = "Report Management", description = "API برای مدیریت گزارش‌ها")
@RestController
@RequestMapping("/reports")
public class  ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private JasperReportRepository jasperReportRepository;

    @Autowired
    private JasperReportParamRepository jasperReportParamRepository;

    //  دانلود گزارش با پارامترها
    @Operation(summary = "دریافت خروجی گزارش در قالب PDF", description = "دریافت گزارش JasperReports با مقداردهی پارامترها و خروجی PDF")
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadReport(
            @Parameter(description = "شناسه گزارش در دیتابیس", required = true)
            @RequestParam Long reportId,
            @RequestParam Map<String, Object> parameters) throws JRException {
        JasperReportEntity reportEntity = jasperReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("گزارش یافت نشد!"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                new ByteArrayInputStream(reportEntity.getReportContent()),
                parameters,
                new JREmptyDataSource()
        );
        byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .body(pdfData);
    }

    //  آپلود فایل گزارش و ذخیره در دیتابیس
    @Operation(summary = "آپلود گزارش JasperReport", description = "آپلود یک فایل .jrxml یا .jasper و ذخیره در دیتابیس")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadReport(
            @Parameter(description = "فایل گزارش JasperReport", required = true)
            @RequestParam("file") MultipartFile file) throws IOException, JRException {
        JasperReportEntity report = new JasperReportEntity();
        report.setReportContent(file.getBytes());
        report.setReportCode(new Random().nextInt(1000));
        report.setFixed(false);
        jasperReportRepository.save(report);
        extractAndSaveParams(report.getReportContent());
        return ResponseEntity.ok("Report uploaded successfully!");
    }

    //  استخراج و ذخیره پارامترهای گزارش
    private void extractAndSaveParams(byte[] reportContent) throws JRException {
        InputStream reportStream = new ByteArrayInputStream(reportContent);
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
        for (JRParameter param : jasperReport.getParameters()) {
            if (!param.isSystemDefined()) {
                JasperReportParam newParam = new JasperReportParam();
                newParam.setParamName(param.getName());
                try {
                    newParam.setParamType(Integer.parseInt(param.getValueClassName())); // اصلاح مقداردهی پارامتر
                } catch (NumberFormatException e) {
                    newParam.setParamType(-1); // مقدار پیش‌فرض برای نوع ناشناخته
                }
                jasperReportParamRepository.save(newParam);
            }
        }
    }

    //  پیش‌نمایش گزارش
    @Operation(summary = "پیش‌نمایش گزارش", description = "مشاهده گزارش قبل از دانلود در قالب PDF")
    @GetMapping("/preview")
    public ResponseEntity<byte[]> previewReport(
            @Parameter(description = "شناسه گزارش در دیتابیس", required = true)
            @RequestParam Long reportId,
            @RequestParam Map<String, Object> parameters) throws JRException {
        JasperReportEntity reportEntity = jasperReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("گزارش یافت نشد!"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                new ByteArrayInputStream(reportEntity.getReportContent()),
                parameters,
                new JREmptyDataSource()
        );
        byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=preview.pdf")
                .body(pdfData);
    }
}
