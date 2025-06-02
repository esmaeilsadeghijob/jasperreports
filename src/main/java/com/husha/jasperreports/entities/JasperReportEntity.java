package com.husha.jasperreports.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "jasper_reports")
public class JasperReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cid;
    private int sid;
    private int reportCode;
    @Lob
    private byte[] reportContent;
    private boolean fixed;
}
