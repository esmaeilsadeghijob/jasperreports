package com.husha.jasperreports.service;

import com.husha.jasperreports.entity.ReportTemplate;
import com.husha.jasperreports.repository.ReportTemplateRepository;
import com.husha.jasperreports.util.ReportExporter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class ReportTemplateService {

    private final ReportTemplateRepository repository;

    public ReportTemplateService(ReportTemplateRepository repository) {
        this.repository = repository;
    }

    public void saveTemplate(String templateName, String jrxmlContent) {
        ReportTemplate template = new ReportTemplate();
        template.setName(templateName);
        template.setJrxmlContent(jrxmlContent);
        repository.save(template);
    }

    public String getTemplateContent(String templateName) throws Exception {
        return repository.findByName(templateName)
                .orElseThrow(() -> new Exception("تمپلیت یافت نشد"))
                .getJrxmlContent();
    }

    public byte[] generateReport(String templateName, Map<String, Object> parameters, String format) throws Exception {
        String templateContent = getTemplateContent(templateName);
        InputStream inputStream = new ByteArrayInputStream(templateContent.getBytes(StandardCharsets.UTF_8));

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        return ReportExporter.exportReport(jasperPrint, format);
    }

    public void createJrxmlTemplate(String templateName, Map<String, String> fields) throws Exception {
        // ایجاد طرح JasperDesign
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName(templateName);
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);
        // اضافه کردن فیلدها به طراحی
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            JRDesignField field = new JRDesignField();
            field.setName(entry.getKey());
            field.setValueClass(Class.forName(entry.getValue()));
            jasperDesign.addField(field);
        }
        // تنظیم مسیر ذخیره‌سازی فایل JRXML
        String outputPath = "src/main/resources/templates/" + templateName + ".jrxml";
        try {
            JasperCompileManager.compileReportToFile(jasperDesign, outputPath);
            System.out.println("تمپلیت JRXML با نام " + templateName + " با موفقیت ایجاد شد و در مسیر ذخیره شد: " + outputPath);
        } catch (JRException e) {
            throw new Exception("خطا در ایجاد فایل JRXML: " + e.getMessage());
        }
    }

}
