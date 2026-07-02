package com.example.demo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Q4 {

    public static void main(String[] args) {
        SpringApplication.run(Q4.class, args);
    }
}

// ===================== VALIDATION GROUPS =====================

interface SmsGroup {
}

interface EmailGroup {
}

// ===================== DTO =====================

class AlertConfigDTO {

    @NotBlank(message = "Phone number is required", groups = SmsGroup.class)
    private String phoneNumber;

    @NotBlank(message = "Email address is required", groups = EmailGroup.class)
    @Email(message = "Invalid email format", groups = EmailGroup.class)
    private String emailAddress;

    private String message;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

// ===================== CONTROLLER =====================

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController {

    // SMS Endpoint
    @PostMapping("/sms")
    public ResponseEntity<String> sendSMS(
            @Validated(SmsGroup.class) @RequestBody AlertConfigDTO request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("SMS notification configured successfully");
    }

    // EMAIL Endpoint
    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(
            @Validated(EmailGroup.class) @RequestBody AlertConfigDTO request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Email notification configured successfully");
    }
}

// ===================== GLOBAL EXCEPTION HANDLER =====================

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }
}