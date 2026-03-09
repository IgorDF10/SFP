package com.sfp.service;

import com.sfp.dto.request.TransactionRequestDTO;
import com.sfp.dto.response.TransactionResponseDTO;
import com.sfp.exception.ResourceNotFoundException;
import com.sfp.exception.BusinessException;
import com.sfp.model.Category;
import com.sfp.model.Transaction;
import com.sfp.model.User;
import com.sfp.model.enums.TransactionType;
import com.sfp.repository.CategoryRepository;
import com.sfp.repository.TransactionRepository;
import com.sfp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public TransactionResponseDTO createTransaction(Long userId, TransactionRequestDTO dto) {
        // Busca o usuário
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Busca a categoria
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        // Verifica se a categoria pertence ao usuário
        if (!category.getUser().getId().equals(userId)) {
            throw new BusinessException("Categoria não pertence ao usuário");
        }

        // Verifica se o tipo da transação é compatível com o tipo da categoria
        if ((dto.getType() == TransactionType.INCOME && category.getType() != com.sfp.model.enums.CategoryType.INCOME) ||
            (dto.getType() == TransactionType.EXPENSE && category.getType() != com.sfp.model.enums.CategoryType.EXPENSE)) {
            throw new BusinessException("Tipo da transação incompatível com tipo da categoria");
        }

        // Cria a transação
        Transaction transaction = Transaction.builder()
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .type(dto.getType())
                .date(dto.getDate())
                .notes(dto.getNotes())
                .isRecurring(dto.getIsRecurring())
                .isPaid(dto.getIsPaid())
                .user(user)
                .category(category)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponseDTO(savedTransaction);
    }

    @Transactional(readOnly = true)
    public TransactionResponseDTO getTransactionById(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        // Verifica se a transação pertence ao usuário
        if (!transaction.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Transação não encontrada");
        }

        return mapToResponseDTO(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getAllTransactionsByUser(Long userId) {
        return transactionRepository.findByUserIdOrderByDateDesc(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByType(Long userId, TransactionType type) {
        return transactionRepository.findByUserIdAndType(userId, type).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByCategory(Long userId, Long categoryId) {
        return transactionRepository.findByUserIdAndCategoryId(userId, categoryId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getRecurringTransactions(Long userId) {
        return transactionRepository.findByUserIdAndIsRecurringTrue(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getUnpaidTransactions(Long userId) {
        return transactionRepository.findByUserIdAndIsPaidFalse(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponseDTO updateTransaction(Long userId, Long transactionId, TransactionRequestDTO dto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        // Verifica se a transação pertence ao usuário
        if (!transaction.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Transação não encontrada");
        }

        // Busca a nova categoria se foi alterada
        if (!transaction.getCategory().getId().equals(dto.getCategoryId())) {
            Category newCategory = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

            // Verifica se a categoria pertence ao usuário
            if (!newCategory.getUser().getId().equals(userId)) {
                throw new BusinessException("Categoria não pertence ao usuário");
            }

            // Verifica compatibilidade de tipos
            if ((dto.getType() == TransactionType.INCOME && newCategory.getType() != com.sfp.model.enums.CategoryType.INCOME) ||
                (dto.getType() == TransactionType.EXPENSE && newCategory.getType() != com.sfp.model.enums.CategoryType.EXPENSE)) {
                throw new BusinessException("Tipo da transação incompatível com tipo da categoria");
            }

            transaction.setCategory(newCategory);
        }

        // Atualiza os campos
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setDate(dto.getDate());
        transaction.setNotes(dto.getNotes());
        transaction.setIsRecurring(dto.getIsRecurring());
        transaction.setIsPaid(dto.getIsPaid());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToResponseDTO(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Transação não encontrada");
        }

        transactionRepository.delete(transaction);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalIncome(Long userId) {
        return transactionRepository.sumIncomeByUserId(userId);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalExpense(Long userId) {
        return transactionRepository.sumExpenseByUserId(userId);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long userId) {
        return transactionRepository.calculateBalance(userId);
    }

    @Transactional(readOnly = true)
    public BigDecimal getIncomeByPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.sumIncomeByUserIdAndPeriod(userId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public BigDecimal getExpenseByPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.sumExpenseByUserIdAndPeriod(userId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public long countTransactionsByUser(Long userId) {
        return transactionRepository.countByUserId(userId);
    }

    // Método auxiliar para converter Entity -> DTO
    private TransactionResponseDTO mapToResponseDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .date(transaction.getDate())
                .notes(transaction.getNotes())
                .isRecurring(transaction.getIsRecurring())
                .isPaid(transaction.getIsPaid())
                .userId(transaction.getUser().getId())
                .categoryId(transaction.getCategory().getId())
                .categoryName(transaction.getCategory().getName())
                .categoryColor(transaction.getCategory().getColor())
                .categoryIcon(transaction.getCategory().getIcon())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}