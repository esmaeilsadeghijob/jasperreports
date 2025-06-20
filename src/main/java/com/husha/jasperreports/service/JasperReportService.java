package com.husha.jasperreports.service;

import com.husha.jasperreports.entity.JasperReportParam;
import com.husha.jasperreports.entity.JasperReports;
import com.husha.jasperreports.repository.JasperReportParamRepository;
import com.husha.jasperreports.repository.JasperReportRepository;
import com.husha.jasperreports.repository.ReportTemplateRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

import net.sf.jasperreports.engine.JRParameter;

@Service
public class JasperReportService {
    private final JasperReportRepository jasperReportRepository;
    private final JasperReportParamRepository jasperReportParamRepository;
    private final ReportTemplateRepository reportTemplateRepository;
    public JasperReportService(JasperReportRepository jasperReportRepository, JasperReportParamRepository jasperReportParamRepository, ReportTemplateRepository reportTemplateRepository) {
        this.jasperReportRepository = jasperReportRepository;
        this.jasperReportParamRepository = jasperReportParamRepository;
        this.reportTemplateRepository = reportTemplateRepository;
    }

    // ذخیره فایل گزارش همراه با پارامترهای مرتبط
    public UUID uploadReport(byte[] fileContent, int cid, int sid, int reportCode, List<JasperReportParam> params, String filename) {

        JasperReports report = new JasperReports();
        report.setCid(cid);
        report.setSid(sid);
        report.setReportCode(reportCode);
        report.setReportContent(fileContent);
        report.setFixed(false);
        report.setName(filename);
        jasperReportRepository.save(report);

        for (JasperReportParam param : params) {
            param.setReport(report);
            jasperReportParamRepository.save(param);
        }

        return report.getId();
    }

    public List<JasperReportParam> extractParams(byte[] fileContent) {
        try {
            InputStream reportStream = new ByteArrayInputStream(fileContent);
            JasperDesign jasperDesign = JRXmlLoader.load(reportStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            List<JasperReportParam> extractedParams = new ArrayList<>();
            for (JRParameter param : jasperReport.getParameters()) {
                if (!param.isSystemDefined()) { // حذف پارامترهای سیستمی
                    JasperReportParam extractedParam = new JasperReportParam();
                    extractedParam.setParamName(param.getName());
                    extractedParam.setParamType(determineParamType(param.getValueClassName()));
                    extractedParams.add(extractedParam);
                }
            }
            return extractedParams;
        } catch (Exception e) {
            throw new RuntimeException("خطا در استخراج پارامترها: " + e.getMessage());
        }
    }

    // **شناسایی نوع پارامتر**
    private int determineParamType(String className) {
        return switch (className) {
            case "java.lang.Integer" -> 1;
            case "java.lang.String" -> 2;
            case "java.util.Date" -> 3;
            default -> 0;
        };
    }

    // دریافت لیست پارامترهای مرتبط با گزارش
    public List<JasperReportParam> getReportParams(UUID reportId) {
        return jasperReportParamRepository.findByReportId(reportId);
    }

    // تولید گزارش بر اساس فرمت موردنظر (PDF یا Excel)
    public byte[] generateReport(UUID reportId, Map<String, Object> userParams, String format) throws Exception {
        Optional<JasperReports> report = jasperReportRepository.findById(reportId);
        if (report.isEmpty() || report.get().getReportContent() == null) {
            throw new RuntimeException("گزارش یافت نشد یا ناقص است.");
        }

        // بارگذاری و کامپایل فایل گزارش
        InputStream reportStream = new ByteArrayInputStream(report.get().getReportContent());
        JasperDesign jasperDesign = JRXmlLoader.load(reportStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        // مقداردهی پارامترها
        List<JasperReportParam> params = getReportParams(reportId);
        Map<String, Object> parameterMap = new HashMap<>();
        for (JasperReportParam param : params) {
            Object value = userParams.get(param.getParamName());
            if (value != null) {
                parameterMap.put(param.getParamName(), value);
            }
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, new JREmptyDataSource());

        // خروجی PDF
        if ("pdf".equalsIgnoreCase(format)) {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }

        // خروجی Excel
        ByteArrayOutputStream xlsStream = new ByteArrayOutputStream();
        JRXlsExporter xlsExporter = new JRXlsExporter();
        xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsStream));
        xlsExporter.exportReport();

        return xlsStream.toByteArray();
    }

    // دریافت گزارش بر اساس ID
    public Optional<JasperReports> getReport(UUID id) {
        return jasperReportRepository.findById(id);
    }

    // دریافت لیست تمامی گزارش‌ها
    public List<JasperReports> getAllReports() {
        return jasperReportRepository.findAll();
    }

    // حذف گزارش بر اساس ID
    public void deleteReport(UUID id) {
        jasperReportRepository.deleteById(id);
    }

    // تبدیل فایل JRXML به PDF
    public byte[] generatePdf(UUID id) throws Exception {
        Optional<JasperReports> report = jasperReportRepository.findById(id);
        if (report.isEmpty() || report.get().getReportContent() == null) {
            throw new RuntimeException("گزارش یافت نشد یا ناقص است!");
        }

        try {
            // بارگذاری گزارش
            InputStream jrxmlStream = new ByteArrayInputStream(report.get().getReportContent());
            JasperDesign jasperDesign = JRXmlLoader.load(jrxmlStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // مقداردهی پارامترهای اصلی
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cid", report.get().getCid());
            parameters.put("sid", report.get().getSid());
            parameters.put("reportCode", report.get().getReportCode());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException("خطا در پردازش گزارش: " + e.getMessage());
        }
    }
}
