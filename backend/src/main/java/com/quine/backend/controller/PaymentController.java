//**TESTING PAYMENT TESTING PAYMENT TESTING
package com.quine.backend.controller;

import com.quine.backend.model.Student;
import com.quine.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private StudentService studentService;

    // 1. Fee Status dikhane ke liye
    @GetMapping("/status/{studentId}")
    public ResponseEntity<?> getFeeStatus(@PathVariable String studentId) {
        return studentService.getStudentByRollNo(studentId)
                .map(s -> ResponseEntity.ok(Map.of(
                        "total", s.getTotalFees(),
                        "paid", s.getFeesPaid(),
                        "remaining", s.getTotalFees() - s.getFeesPaid()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    // 2. Code Verify karne ke liye (Your 8248 Logic)
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> payload) {
        String studentId = (String) payload.get("studentId");
        double code = Double.parseDouble(payload.get("code").toString());

        return studentService.getStudentByRollNo(studentId).map(student -> {
            // Reverse calculation: Amount = Code - 8248
            double amountPaid = code - 8248;

            if (amountPaid > 0) {
                student.setFeesPaid(student.getFeesPaid() + amountPaid);
                studentService.saveStudent(student);
                return ResponseEntity.ok(Map.of("message", "Payment Successful!", "amount", amountPaid));
            }
            return ResponseEntity.badRequest().body("Invalid Payment Code!");
        }).orElse(ResponseEntity.notFound().build());
    }
}