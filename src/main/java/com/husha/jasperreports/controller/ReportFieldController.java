package com.husha.jasperreports.controller;

import com.husha.jasperreports.dto.ReportFieldDTO;
import com.husha.jasperreports.entity.ReportField;
import com.husha.jasperreports.repository.ReportFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fields")
public class ReportFieldController {

    @Autowired
    private ReportFieldRepository fieldRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addField(@RequestBody ReportFieldDTO fieldDTO) {
        try {
            ReportField field = new ReportField(fieldDTO.getName(), fieldDTO.getType());
            fieldRepository.save(field);
            return ResponseEntity.ok("متغیر " + fieldDTO.getName() + " با نوع " + fieldDTO.getType() + " ذخیره شد.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("خطا در ذخیره‌سازی متغیر: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<ReportField>> getFields() {
        return ResponseEntity.ok(fieldRepository.findAll());
    }
}
