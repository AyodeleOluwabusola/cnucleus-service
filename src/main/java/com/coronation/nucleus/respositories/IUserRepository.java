package com.coronation.nucleus.respositories;

import com.coronation.nucleus.entities.CTUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author toyewole
 */
public interface IUserRepository extends JpaRepository<CTUser, Long> {

    boolean existsUserByEmail(String email);

    boolean existsUserById(Long id);

    @Override
    Optional<CTUser> findById(Long userId);

    @Query("select ct.pendingRequestPk from CTUser ct where ct.id = :id")
    Optional<Long> getPendingRequestPk(Long id);

    @Modifying
    @Query("update CTUser u set u.pendingRequestPk = ?1 where u.id = ?2")
    int setPendingRequestPk(Long pendingRequestPk, Long id);

}
