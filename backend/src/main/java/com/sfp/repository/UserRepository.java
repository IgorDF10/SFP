package com.sfp.repository;

import com.sfp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca usuário por email
     * @param email Email do usuário
     * @return Optional com o usuário se encontrado
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica se existe usuário com o email informado
     * @param email Email a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuário por email e que esteja ativo
     * @param email Email do usuário
     * @return Optional com o usuário se encontrado e ativo
     */
    Optional<User> findByEmailAndIsActiveTrue(String email);

    /**
     * Busca usuários ativos
     * @return Lista de usuários ativos
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    java.util.List<User> findAllActiveUsers();

    /**
     * Conta quantos usuários estão ativos
     * @return Quantidade de usuários ativos
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();
}