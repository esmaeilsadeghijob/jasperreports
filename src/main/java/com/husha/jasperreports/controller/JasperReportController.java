package com.husha.jasperreports.controller;

import com.husha.jasperreports.entity.JasperReportParam;
import com.husha.jasperreports.entity.JasperReports;
import com.husha.jasperreports.service.JasperReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Reports", description = "Operations related to JasperReports")
@RestController
@RequestMapping("/api/reports")
//@RequestMapping
public class JasperReportController {
    private final JasperReportService jasperReportService;

    public JasperReportController(JasperReportService jasperReportService) {
        this.jasperReportService = jasperReportService;
    }

    // آپلود فایل JRXML و ذخیره در پایگاه داده همراه با پارامترها
    @PostMapping("/upload")
    public ResponseEntity<?> uploadReport(@RequestParam("file") MultipartFile file,
                                          @RequestParam int cid,
                                          @RequestParam int sid,
                                          @RequestParam int reportCode,
                                          @RequestParam(required = false) String filename,
                                          @RequestParam(required = false) List<String> paramNames,
                                          @RequestParam(required = false) List<Integer> paramTypes) {
        try {
            // تبدیل لیست پارامترها به `JasperReportParam`
            List<JasperReportParam> params = new ArrayList<>();
            if (paramNames != null) {
                for (int i = 0; i < paramNames.size(); i++) {
                    JasperReportParam param = new JasperReportParam();
                    param.setParamName(paramNames.get(i));
                    param.setParamType(paramTypes.get(i));
                    params.add(param);
                }
            }
            if (filename == null || filename.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                if (originalFilename != null) {
                    int lastDotIndex = originalFilename.lastIndexOf('.');
                    if (lastDotIndex != -1) {
                        filename = originalFilename.substring(0, lastDotIndex);
                    }
                }
            }
            UUID reportId = jasperReportService.uploadReport(file.getBytes(), cid, sid, reportCode, params, filename);
            return ResponseEntity.ok("Report uploaded successfully! ID: " + reportId);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed.");
        }
    }

    @PostMapping("/extract-params")
    public ResponseEntity<List<JasperReportParam>> extractParams(@RequestParam("file") MultipartFile file) {
        try {
            //todo
//            Map<String, String> parameters = new HashMap<>();
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(file.getInputStream());
//
//            NodeList parameterNodes = doc.getElementsByTagName("parameter");
//            for (int i = 0; i < parameterNodes.getLength(); i++) {
//                Element parameterElement = (Element) parameterNodes.item(i);
//                String name = parameterElement.getAttribute("name");
//                String className = parameterElement.getAttribute("class");
//                parameters.put(name, className);
//            }


            List<JasperReportParam> params = jasperReportService.extractParams(file.getBytes());
            // todo
//            JasperReportParam param = new JasperReportParam();
//            String filename = null;
//            String originalFilename = file.getOriginalFilename();
//            if (originalFilename != null) {
//                int lastDotIndex = originalFilename.lastIndexOf('.');
//                if (lastDotIndex != -1) {
//                    filename = originalFilename.substring(0, lastDotIndex);
//                }
//            }
//            param.setParamName(filename);
//            params.add(param);
            //todo
            return ResponseEntity.ok(params);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // دریافت لیست پارامترهای یک گزارش
    @GetMapping("/params/{id}")
    public ResponseEntity<List<JasperReportParam>> getReportParams(@PathVariable UUID id) {
        List<JasperReportParam> params = jasperReportService.getReportParams(id);
        return ResponseEntity.ok(params);
    }

    // مقداردهی پارامترها و نمایش گزارش PDF
    @GetMapping("/preview/{id}")
    public ResponseEntity<?> previewReport(@PathVariable UUID id, @RequestParam Map<String, Object> params) {
        try {
            byte[] reportBytes = jasperReportService.generateReport(id, params, "pdf");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf")
                    .body(reportBytes);
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
