package com.husha.jasperreports.service;

import com.husha.jasperreports.entity.JasperReport;
import com.husha.jasperreports.repository.JasperReportRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class JasperReportService {
    private final JasperReportRepository jasperReportRepository;

    public JasperReportService(JasperReportRepository jasperReportRepository) {
        this.jasperReportRepository = jasperReportRepository;
    }

    public JasperReport uploadReport(byte[] fileContent, int cid, int sid, int reportCode) {
        JasperReport report = new JasperReport();
        report.setCid(cid);
        report.setSid(sid);
        report.setReportCode(reportCode);
        report.setReportContent(fileContent);
        report.setFixed(false);
        return jasperReportRepository.save(report);
    }

    public Optional<JasperReport> getReport(UUID id) {
        return jasperReportRepository.findById(id);
    }
}
