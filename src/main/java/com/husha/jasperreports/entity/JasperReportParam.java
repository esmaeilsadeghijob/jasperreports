package com.husha.jasperreports.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class JasperReportParam {
    @Id
    @GeneratedValue
    private UUID id;
    private String paramName;
    private int paramType;
}
