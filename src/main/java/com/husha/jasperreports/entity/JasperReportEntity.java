//package com.husha.jasperreports.entity;
//
//import java.util.UUID;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import org.hibernate.annotations.GenericGenerator;
//
//@Data
//@Entity
//@Table(name = "jasper_reports")
//public class JasperReportEntity {
//    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    private UUID id; // مقداردهی خودکار UUID بدون Identity
//    @Version
//    private Integer version;
//    private int cid;
//    private int sid;
//    private int reportCode;
//    @Lob
//    private byte[] reportContent;
//    private boolean fixed;
//}
//
