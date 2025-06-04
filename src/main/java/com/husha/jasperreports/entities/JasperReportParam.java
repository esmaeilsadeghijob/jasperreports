package com.husha.jasperreports.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "jasper_reports_params")
public class JasperReportParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paramName;
    private int paramType;
}
