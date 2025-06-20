package com.husha.jasperreports.repository;

import com.husha.jasperreports.entity.ReportField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportFieldRepository extends JpaRepository<ReportField, Long> {

    // پیدا کردن متغیرها بر اساس نام
    Optional<ReportField> findByName(String name);

    // پیدا کردن تمامی متغیرها مربوط به یک تمپلیت خاص
    List<ReportField> findByTemplateId(Long templateId);
}

