package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Q5 {

    public static void main(String[] args) {
        SpringApplication.run(Q5.class, args);
    }
}

// ======================= DTO =======================

class TelemetryDTO {

    private String sensorId;
    private Double temperature;

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}

// ======================= CONTROLLER =======================

@RestController
@RequestMapping("/api/v1")
class TelemetryController {

    @PostMapping("/telemetry")
    public ResponseEntity<String> receiveTelemetry(
            @RequestBody TelemetryDTO telemetry) {

        // Simulate processing

        return ResponseEntity.ok("Telemetry received successfully");
    }
}

// ======================= GLOBAL EXCEPTION HANDLER =======================

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleParsingError(
            HttpMessageNotReadableException ex) {

        Map<String, String> error = new HashMap<>();

        error.put("error", "Malformed JSON payload");

        // Extract a readable part of the exception message
        String message = ex.getMostSpecificCause().getMessage();

        if (message != null && message.length() > 150) {
            message = message.substring(0, 150) + "...";
        }

        error.put("details", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}