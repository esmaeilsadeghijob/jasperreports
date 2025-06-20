package com.husha.jasperreports.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "report_templates")
public class ReportTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "jrxml_content", columnDefinition = "TEXT", nullable = false)
    private String jrxmlContent;

}
