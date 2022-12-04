package com.coronation.nucleus.respositories;


import com.coronation.nucleus.entities.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author AuodeleOluwabusola
 */
public interface ICompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

    @Query("select  c from CompanyProfile  c where c.id =:companyId and c.stage <> 'FINAL'and c.user.id = :userId ")
    Optional<CompanyProfile> getPendingCompanyProfileForUser(Long companyId, Long userId);

}
