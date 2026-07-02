package com.example.demo;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class Q2 {

    public static void main(String[] args) {
        SpringApplication.run(Q2.class, args);
    }
}

// ================= CUSTOM ANNOTATION =================

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SupportedCurrencyValidator.class)
@interface SupportedCurrency {

    String message() default "Unsupported currency";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

// ================= VALIDATOR =================

class SupportedCurrencyValidator implements ConstraintValidator<SupportedCurrency, String> {

    private static final Set<String> VALID =
            Set.of("USD", "EUR", "GBP");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && VALID.contains(value);
    }
}

// ================= TRANSACTION DTO =================

class TransactionDTO {

    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    @SupportedCurrency
    private String currency;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

// ================= BATCH DTO =================

class BatchRequestDTO {

    @NotBlank(message = "Batch ID cannot be blank")
    private String batchId;

    @Valid               // Important: validates every TransactionDTO
    private List<TransactionDTO> transactions;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}

// ================= CONTROLLER =================

@RestController
@RequestMapping("/api/v1/batches")
class BatchController {

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String processBatch(@Valid @RequestBody BatchRequestDTO batch) {

        // Simulate processing

        return "Batch accepted successfully";
    }
}

// ================= GLOBAL EXCEPTION HANDLER =================

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // Example:
            // transactions[0].amount
            // transactions[1].currency
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }
}