package com.husha.jasperreports.util;

import com.husha.jasperreports.config.KeyConstant;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReportExporter {

    public static byte[] exportReport(JasperPrint jasperPrint, String format) throws Exception {
        switch (format.toUpperCase()) {
            case KeyConstant.REPO_TYPE_PDF:
                return exportPdf(jasperPrint);
            case KeyConstant.REPO_TYPE_XLS:
                return exportXls(jasperPrint);
            case KeyConstant.REPO_TYPE_CSV:
                return exportCsv(jasperPrint);
            case KeyConstant.REPO_TYPE_HTML:
                return exportHtml(jasperPrint);
            case KeyConstant.REPO_TYPE_TXT:
                return exportTxt(jasperPrint);
            default:
                throw new IllegalArgumentException("فرمت نامعتبر: " + format);
        }
    }

    private static byte[] exportPdf(JasperPrint jasperPrint) throws JRException {
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private static byte[] exportXls(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream xlsStream = new ByteArrayOutputStream();
        JRXlsExporter xlsExporter = new JRXlsExporter();
        xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsStream));
        xlsExporter.exportReport();
        return xlsStream.toByteArray();
    }

    private static byte[] exportCsv(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream csvStream = new ByteArrayOutputStream();
        JRCsvExporter csvExporter = new JRCsvExporter();
        csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvStream));
        csvExporter.exportReport();
        return csvStream.toByteArray();
    }

    private static byte[] exportHtml(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(htmlStream));
        htmlExporter.exportReport();
        return htmlStream.toByteArray();
    }


    private static byte[] exportTxt(JasperPrint jasperPrint) throws JRException, IOException {
        ByteArrayOutputStream txtStream = new ByteArrayOutputStream();
        txtStream.write("Generated Report:\n".getBytes(StandardCharsets.UTF_8));
        for (JRPrintPage page : jasperPrint.getPages()) {
            txtStream.write(("Page: " + jasperPrint.getPages().indexOf(page) + "\n").getBytes(StandardCharsets.UTF_8));
        }
        return txtStream.toByteArray();
    }
}
