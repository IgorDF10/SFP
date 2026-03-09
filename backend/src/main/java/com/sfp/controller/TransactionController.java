package com.sfp.controller;

import com.sfp.dto.request.TransactionRequestDTO;
import com.sfp.dto.response.TransactionResponseDTO;
import com.sfp.model.enums.TransactionType;
import com.sfp.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestParam Long userId,
            @Valid @RequestBody TransactionRequestDTO dto) {
        TransactionResponseDTO transaction = transactionService.createTransaction(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @RequestParam Long userId,
            @PathVariable Long id) {
        TransactionResponseDTO transaction = transactionService.getTransactionById(userId, id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@RequestParam Long userId) {
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactionsByUser(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByType(
            @RequestParam Long userId,
            @PathVariable TransactionType type) {
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByType(userId, type);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByCategory(
            @RequestParam Long userId,
            @PathVariable Long categoryId) {
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByCategory(userId, categoryId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/period")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByPeriod(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByPeriod(userId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/recurring")
    public ResponseEntity<List<TransactionResponseDTO>> getRecurringTransactions(@RequestParam Long userId) {
        List<TransactionResponseDTO> transactions = transactionService.getRecurringTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<TransactionResponseDTO>> getUnpaidTransactions(@RequestParam Long userId) {
        List<TransactionResponseDTO> transactions = transactionService.getUnpaidTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @RequestParam Long userId,
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequestDTO dto) {
        TransactionResponseDTO transaction = transactionService.updateTransaction(userId, id, dto);
        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @RequestParam Long userId,
            @PathVariable Long id) {
        transactionService.deleteTransaction(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/totals/income")
    public ResponseEntity<BigDecimal> getTotalIncome(@RequestParam Long userId) {
        BigDecimal total = transactionService.getTotalIncome(userId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/totals/expense")
    public ResponseEntity<BigDecimal> getTotalExpense(@RequestParam Long userId) {
        BigDecimal total = transactionService.getTotalExpense(userId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/totals/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam Long userId) {
        BigDecimal balance = transactionService.getBalance(userId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/totals/income/period")
    public ResponseEntity<BigDecimal> getIncomeByPeriod(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal total = transactionService.getIncomeByPeriod(userId, startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/totals/expense/period")
    public ResponseEntity<BigDecimal> getExpenseByPeriod(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal total = transactionService.getExpenseByPeriod(userId, startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countTransactions(@RequestParam Long userId) {
        long count = transactionService.countTransactionsByUser(userId);
        return ResponseEntity.ok(count);
    }
}