package com.sfp.repository;

import com.sfp.model.Transaction;
import com.sfp.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Busca todas as transações de um usuário
     * @param userId ID do usuário
     * @return Lista de transações
     */
    List<Transaction> findByUserId(Long userId);

    /**
     * Busca transações de um usuário ordenadas por data (mais recente primeiro)
     * @param userId ID do usuário
     * @return Lista de transações ordenadas
     */
    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    /**
     * Busca transações de um usuário por tipo
     * @param userId ID do usuário
     * @param type Tipo da transação (INCOME ou EXPENSE)
     * @return Lista de transações do tipo especificado
     */
    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

    /**
     * Busca transações de um usuário por categoria
     * @param userId ID do usuário
     * @param categoryId ID da categoria
     * @return Lista de transações da categoria
     */
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);

    /**
     * Busca transações entre duas datas
     * @param userId ID do usuário
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de transações no período
     */
    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Busca transações entre datas ordenadas por data
     * @param userId ID do usuário
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de transações ordenadas
     */
    List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Busca transações por tipo e período
     * @param userId ID do usuário
     * @param type Tipo da transação
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de transações
     */
    List<Transaction> findByUserIdAndTypeAndDateBetween(Long userId, TransactionType type, LocalDate startDate, LocalDate endDate);

    /**
     * Calcula o total de receitas de um usuário
     * @param userId ID do usuário
     * @return Soma das receitas
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'INCOME' AND t.isPaid = true")
    BigDecimal sumIncomeByUserId(@Param("userId") Long userId);

    /**
     * Calcula o total de despesas de um usuário
     * @param userId ID do usuário
     * @return Soma das despesas
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'EXPENSE' AND t.isPaid = true")
    BigDecimal sumExpenseByUserId(@Param("userId") Long userId);

    /**
     * Calcula receitas em um período
     * @param userId ID do usuário
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Soma das receitas no período
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'INCOME' AND t.isPaid = true AND t.date BETWEEN :startDate AND :endDate")
    BigDecimal sumIncomeByUserIdAndPeriod(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Calcula despesas em um período
     * @param userId ID do usuário
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Soma das despesas no período
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'EXPENSE' AND t.isPaid = true AND t.date BETWEEN :startDate AND :endDate")
    BigDecimal sumExpenseByUserIdAndPeriod(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Calcula saldo (receitas - despesas) de um usuário
     * @param userId ID do usuário
     * @return Saldo atual
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END), 0) FROM Transaction t WHERE t.user.id = :userId AND t.isPaid = true")
    BigDecimal calculateBalance(@Param("userId") Long userId);

    /**
     * Busca transações recorrentes de um usuário
     * @param userId ID do usuário
     * @return Lista de transações recorrentes
     */
    List<Transaction> findByUserIdAndIsRecurringTrue(Long userId);

    /**
     * Busca transações não pagas de um usuário
     * @param userId ID do usuário
     * @return Lista de transações pendentes
     */
    List<Transaction> findByUserIdAndIsPaidFalse(Long userId);

    /**
     * Conta quantas transações um usuário tem
     * @param userId ID do usuário
     * @return Quantidade de transações
     */
    long countByUserId(Long userId);

    /**
     * Busca últimas N transações de um usuário
     * @param userId ID do usuário
     * @return Lista das últimas transações
     */
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.date DESC, t.createdAt DESC")
    List<Transaction> findRecentTransactionsByUserId(@Param("userId") Long userId);
}