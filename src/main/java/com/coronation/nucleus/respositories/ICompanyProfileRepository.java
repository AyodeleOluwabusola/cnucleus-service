package com.coronation.nucleus.respositories;


import com.coronation.nucleus.entities.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author AuodeleOluwabusola
 */
public interface ICompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

    @Override
    Optional<CompanyProfile> findById(Long aLong);

}
