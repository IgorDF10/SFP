package com.sfp.repository;

import com.sfp.model.Category;
import com.sfp.model.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Busca todas as categorias de um usuário
     * @param userId ID do usuário
     * @return Lista de categorias do usuário
     */
    List<Category> findByUserId(Long userId);

    /**
     * Busca categorias ativas de um usuário
     * @param userId ID do usuário
     * @return Lista de categorias ativas
     */
    List<Category> findByUserIdAndIsActiveTrue(Long userId);

    /**
     * Busca categorias de um usuário por tipo
     * @param userId ID do usuário
     * @param type Tipo da categoria (INCOME ou EXPENSE)
     * @return Lista de categorias do tipo especificado
     */
    List<Category> findByUserIdAndType(Long userId, CategoryType type);

    /**
     * Busca categorias ativas de um usuário por tipo
     * @param userId ID do usuário
     * @param type Tipo da categoria
     * @return Lista de categorias ativas do tipo especificado
     */
    List<Category> findByUserIdAndTypeAndIsActiveTrue(Long userId, CategoryType type);

    /**
     * Busca categoria por nome, tipo e usuário
     * @param userId ID do usuário
     * @param name Nome da categoria
     * @param type Tipo da categoria
     * @return Optional com a categoria se encontrada
     */
    Optional<Category> findByUserIdAndNameAndType(Long userId, String name, CategoryType type);

    /**
     * Verifica se existe categoria com o nome e tipo para o usuário
     * @param userId ID do usuário
     * @param name Nome da categoria
     * @param type Tipo da categoria
     * @return true se existe, false caso contrário
     */
    boolean existsByUserIdAndNameAndType(Long userId, String name, CategoryType type);

    /**
     * Conta quantas categorias um usuário tem
     * @param userId ID do usuário
     * @return Quantidade de categorias
     */
    long countByUserId(Long userId);

    /**
     * Busca categorias ordenadas por nome
     * @param userId ID do usuário
     * @return Lista de categorias ordenadas
     */
    @Query("SELECT c FROM Category c WHERE c.user.id = :userId ORDER BY c.name ASC")
    List<Category> findByUserIdOrderByName(@Param("userId") Long userId);
}