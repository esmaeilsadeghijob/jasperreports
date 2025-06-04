package com.husha.jasperreports.repositories;

import com.husha.jasperreports.entities.JasperReportParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JasperReportParamRepository extends JpaRepository<JasperReportParam, Long> {

    //  دریافت لیست پارامترهای مربوط به گزارش خاص
    List<JasperReportParam> findByParamName(String paramName);

    //  جستجوی پارامترهای مرتبط با یک نوع مشخص
    List<JasperReportParam> findByParamType(Integer paramType);
}
