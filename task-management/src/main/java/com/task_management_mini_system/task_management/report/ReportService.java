package com.task_management_mini_system.task_management.report;

import com.task_management_mini_system.task_management.report.dto.TaskReportRow;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    public byte[] generateTasksPdf(List<TaskReportRow> rows, String reportTitle, String generatedBy) {
        try {
            ClassPathResource jrxml = new ClassPathResource("reports/tasks_report.jrxml");
            InputStream jrxmlInputStream = jrxml.getInputStream();

            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);

            Map<String, Object> params = new HashMap<>();
            params.put("REPORT_TITLE", reportTitle);
            params.put("GENERATED_BY", generatedBy);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate tasks PDF report", e);
        }
    }
}
