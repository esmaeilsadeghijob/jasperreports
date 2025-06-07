package com.husha.jasperreports.repository;

import com.husha.jasperreports.entity.JasperReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JasperReportRepository extends JpaRepository<JasperReport, UUID> {
}
