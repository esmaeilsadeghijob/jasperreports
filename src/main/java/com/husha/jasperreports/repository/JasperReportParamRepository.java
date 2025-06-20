package com.husha.jasperreports.repository;

import com.husha.jasperreports.entity.JasperReportParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface JasperReportParamRepository extends JpaRepository<JasperReportParam, UUID> {
    // دریافت لیست پارامترها بر اساس ID گزارش
    List<JasperReportParam> findByReportId(UUID reportId);
}
