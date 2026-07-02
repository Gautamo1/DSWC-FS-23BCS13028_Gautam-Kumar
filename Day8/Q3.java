package com.example.demo;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Q3 {

    public static void main(String[] args) {
        SpringApplication.run(Q3.class, args);
    }
}

// ====================== CUSTOM ANNOTATION ======================

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordsMatchValidator.class)
@interface PasswordsMatch {

    String message() default "Passwords do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

// ====================== VALIDATOR ======================

class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, RegistrationRequestDTO> {

    @Override
    public boolean isValid(RegistrationRequestDTO dto, ConstraintValidatorContext context) {

        if (dto == null) {
            return true;
        }

        return dto.getPassword() != null &&
               dto.getPassword().equals(dto.getConfirmPassword());
    }
}

// ====================== REQUEST DTO ======================

@PasswordsMatch
class RegistrationRequestDTO {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Email(message = "Invalid email")
    private String email;

    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

// ====================== RESPONSE DTO ======================

class UserResponseDTO {

    private String username;
    private String email;

    public UserResponseDTO() {
    }

    public UserResponseDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

// ====================== CONTROLLER ======================

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody RegistrationRequestDTO request) {

        // Simulate saving the user

        UserResponseDTO response =
                new UserResponseDTO(request.getUsername(), request.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

// ====================== GLOBAL EXCEPTION HANDLER ======================

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // Field errors
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Global (class-level) errors
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put("passwords", error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }
}