package com.husha.jasperreports.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports/jrxml/variable-types")
public class VariableTypeController {

    private static final List<String> VARIABLE_TYPES = List.of(
            "String", "Integer", "Boolean", "Date", "Double", "Long"
    );

    @GetMapping
    public ResponseEntity<List<String>> getVariableTypes() {
        return ResponseEntity.ok(VARIABLE_TYPES);
    }
}

