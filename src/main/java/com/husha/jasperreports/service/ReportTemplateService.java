package com.husha.jasperreports.service;

import com.husha.jasperreports.entity.ReportTemplate;
import com.husha.jasperreports.repository.ReportTemplateRepository;
import com.husha.jasperreports.util.ReportExporter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
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

//    public void createJrxmlTemplate(String templateName, List<Map<String, String>> fields) throws Exception {
//        // ایجاد طرح JasperDesign
//        JasperDesign jasperDesign = new JasperDesign();
//        jasperDesign.setName(templateName);
//        jasperDesign.setPageWidth(595);
//        jasperDesign.setPageHeight(842);
//        // اضافه کردن فیلدها به طراحی
//        for (Map.Entry<String, String> entry : fields.entrySet()) {
//            JRDesignField field = new JRDesignField();
//            field.setName(entry.getKey());
//            field.setValueClass(Class.forName(entry.getValue()));
//            jasperDesign.addField(field);
//        }
//        // تنظیم مسیر ذخیره‌سازی فایل JRXML
//        String outputPath = "src/main/resources/templates/" + templateName + ".jrxml";
//        try {
//            JasperCompileManager.compileReportToFile(jasperDesign, outputPath);
//            System.out.println("تمپلیت JRXML با نام " + templateName + " با موفقیت ایجاد شد و در مسیر ذخیره شد: " + outputPath);
//        } catch (JRException e) {
//            throw new Exception("خطا در ایجاد فایل JRXML: " + e.getMessage());
//        }
//    }

    public String createJrxmlTemplate(String templateName, List<Map<String, String>> fields) throws Exception {
        JasperDesign jasperDesign = new JasperDesign();

//        JRDesignBand detialBand = (JRDesignBand) jasperDesign.getDetailSection().getBands()[0];
//        detialBand.setHeight(20 * fields.size());

        jasperDesign.setName(templateName);
        jasperDesign.setPageWidth(595); // Example: A4 width
        jasperDesign.setPageHeight(842); // Example: A4 height
        jasperDesign.setColumnWidth(555);
        jasperDesign.setLeftMargin(20);
        jasperDesign.setRightMargin(20);
        jasperDesign.setTopMargin(20);
        jasperDesign.setBottomMargin(20);

        // Add fields to the report design
        for (Map<String, String> fieldMap : fields) {
            String fieldName = fieldMap.get("name");
            String fieldClass = fieldMap.getOrDefault("class", "java.lang.String"); // Default to String
            JRDesignField field = new JRDesignField();
            field.setName(fieldName);
            field.setValueClassName(fieldClass);
            jasperDesign.addField(field);
        }

        // Example: Add a title band and a text element for each field
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(30);
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setX(0);
        titleText.setY(0);
        titleText.setWidth(555);
        titleText.setHeight(30);
        titleText.setText("Report: " + templateName);
        titleBand.addElement(titleText);
        jasperDesign.setTitle(titleBand);

        // Create a Page Header band
        JRDesignBand pageHeaderBand = new JRDesignBand();
        pageHeaderBand.setHeight(30); // Set the height of the band
        // Add a static text element to the Page Header
        JRDesignStaticText headerText = new JRDesignStaticText();
        headerText.setX(0);
        headerText.setY(0);
        headerText.setWidth(500);
        headerText.setHeight(20);
        headerText.setText("My Report - Page Header");
        pageHeaderBand.addElement(headerText);
        // Set the Page Header band in the JasperDesign
        jasperDesign.setPageHeader(pageHeaderBand);



        // Create the column header band
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(30); // Set the height of the band
        // Add a static text element for a column header
        JRDesignStaticText staticText = new JRDesignStaticText();
        staticText.setX(0);
        staticText.setY(0);
        staticText.setWidth(100);
        staticText.setHeight(20);
        staticText.setText("Product Name");
        columnHeaderBand.addElement(staticText);
        // Add a dynamic text field for another column header (e.g., from a parameter)
        JRDesignTextField dynamicHeader = new JRDesignTextField();
        dynamicHeader.setX(110);
        dynamicHeader.setY(0);
        dynamicHeader.setWidth(100);
        dynamicHeader.setHeight(20);
        JRExpression expression = new JRDesignExpression();
//        expression.setText("$P{dynamicColumnHeader}"); // Assuming a parameter named "dynamicColumnHeader"
        dynamicHeader.setExpression(expression);
        columnHeaderBand.addElement(dynamicHeader);
        jasperDesign.setColumnHeader(columnHeaderBand);



        // Detail band for displaying field values
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);
        int yPos = 0;
        for (Map<String, String> fieldMap : fields) {
            String fieldName = fieldMap.get("name");
            JRDesignTextField textField = new JRDesignTextField();
            textField.setX(0);
            textField.setY(yPos);
            textField.setWidth(200);
            textField.setHeight(20);
            textField.setExpression(new JRDesignExpression("$F{" + fieldName + "}"));
            detailBand.addElement(textField);
            yPos += 20; // Move down for the next field
        }
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);


        // Write the JasperDesign object to JRXML XML
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRXmlWriter.writeReport(jasperDesign, outputStream, "UTF-8");
        saveTemplate(templateName, outputStream.toString("UTF-8"));
        return outputStream.toString("UTF-8");
    }

}
