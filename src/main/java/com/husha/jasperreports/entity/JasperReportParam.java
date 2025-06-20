package com.husha.jasperreports.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name = "jasper_reports_params")
public class JasperReportParam {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private JasperReports report; // اتصال به گزارش مربوطه

    @Column(name = "paramname", length = 200, nullable = false)
    private String paramName; // نام پارامتر

    @Column(name = "paramtype", nullable = false)
    private int paramType; // نوع پارامتر (1=String, 2=Integer, 3=Date)
}
