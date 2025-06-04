package com.husha.jasperreports.repositories;

import com.husha.jasperreports.entities.JasperReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JasperReportRepository extends JpaRepository<JasperReportEntity, Long> {

    //  جستجوی گزارش براساس کد گزارش
    Optional<JasperReportEntity> findByReportCode(Integer reportCode);

    // بررسی اینکه آیا گزارش ثابت (Fixed) است
    boolean existsByFixedTrue();
}
