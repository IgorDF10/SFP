package com.sfp.service;

import com.sfp.dto.response.CategorySummaryDTO;
import com.sfp.dto.response.DashboardSummaryDTO;
import com.sfp.dto.response.TransactionResponseDTO;
import com.sfp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryDTO getDashboardSummary(Long userId) {
        BigDecimal totalIncome = transactionService.getTotalIncome(userId);
        BigDecimal totalExpense = transactionService.getTotalExpense(userId);
        BigDecimal balance = transactionService.getBalance(userId);
        long totalTransactions = transactionService.countTransactionsByUser(userId);

        // Busca as últimas 10 transações
        List<TransactionResponseDTO> recentTransactions = transactionRepository
                .findRecentTransactionsByUserId(userId)
                .stream()
                .limit(10)
                .map(t -> transactionService.getTransactionById(userId, t.getId()))
                .collect(Collectors.toList());

        return DashboardSummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .totalTransactions(totalTransactions)
                .recentTransactions(recentTransactions)
                .expensesByCategory(new ArrayList<>()) // Implementar depois
                .build();
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDTO getDashboardByPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalIncome = transactionService.getIncomeByPeriod(userId, startDate, endDate);
        BigDecimal totalExpense = transactionService.getExpenseByPeriod(userId, startDate, endDate);
        BigDecimal balance = totalIncome.subtract(totalExpense);

        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByPeriod(userId, startDate, endDate);
        long totalTransactions = transactions.size();

        return DashboardSummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .totalTransactions(totalTransactions)
                .recentTransactions(transactions.stream().limit(10).collect(Collectors.toList()))
                .expensesByCategory(new ArrayList<>())
                .build();
    }
}