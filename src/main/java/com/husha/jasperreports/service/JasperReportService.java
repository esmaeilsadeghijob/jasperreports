package com.husha.jasperreports.service;

import com.husha.jasperreports.entity.JasperReports; // تغییر نام کلاس
import com.husha.jasperreports.repository.JasperReportRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class JasperReportService {
    private final JasperReportRepository jasperReportRepository;

    public JasperReportService(JasperReportRepository jasperReportRepository) {
        this.jasperReportRepository = jasperReportRepository;
    }

    // ذخیره فایل JRXML در پایگاه داده
    public UUID uploadReport(byte[] fileContent, int cid, int sid, int reportCode) {
        JasperReports report = new JasperReports(); // تغییر نام کلاس به JasperReports
        report.setCid(cid);
        report.setSid(sid);
        report.setReportCode(reportCode);
        report.setReportContent(fileContent);
        report.setFixed(false);
        jasperReportRepository.save(report);
        return report.getId();
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
            throw new RuntimeException("گزارش یافت نشد یا محتوا ناقص است!");
        }

        try {
            // تبدیل داده‌ی JRXML به InputStream
            InputStream jrxmlStream = new ByteArrayInputStream(report.get().getReportContent());

            // کامپایل فایل JRXML به JASPER
            JasperDesign jasperDesign = JRXmlLoader.load(jrxmlStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // تنظیم پارامترهای گزارش
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cid", report.get().getCid());
            parameters.put("sid", report.get().getSid());
            parameters.put("reportCode", report.get().getReportCode());

            // پر کردن گزارش و ایجاد خروجی JasperPrint
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            // تبدیل JasperPrint به PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException("خطا در پردازش گزارش: " + e.getMessage());
        }
    }
}
