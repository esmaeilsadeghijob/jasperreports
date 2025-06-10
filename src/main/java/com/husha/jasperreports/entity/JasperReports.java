package com.husha.jasperreports.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class JasperReports {
    @Id
    @GeneratedValue
    private UUID id;

    private int cid;
    private int sid;
    private int reportCode;
    @Lob
    private byte[] reportContent;
    private boolean fixed;
}
