package com.coronation.nucleus.respositories;


import com.coronation.nucleus.entities.Shareholder;
import com.coronation.nucleus.interfaces.IEquityDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author AuodeleOluwabusola
 */
public interface IShareholderRepository extends JpaRepository<Shareholder, Long> {

    @Override
    Optional<Shareholder> findById(Long id);

    long countByCompanyProfileId(Long companyId);

    @Query(value = "SELECT SUM(TOTAL_SHARES) from SHARE_HOLDER WHERE COMPANY_PROFILE = :companyId", nativeQuery = true)
    long allIssuesSharesByCompanyId(Long companyId);

    @Query(value = "SELECT SUM(CASE WHEN equity_class IN ('FOUNDER') THEN 1 ELSE 0 END) as founders, SUM(CASE WHEN equity_class IN ('SERIES_A') THEN 1 ELSE 0 END) as seriesA, " +
            "SUM(CASE WHEN equity_class IN ('SERIES_B') THEN 1 ELSE 0 END) as seriesB, SUM(CASE WHEN equity_class IN ('SERIES_C') THEN 1 ELSE 0 END) as seriesC " +
            "from SHARE_HOLDER", nativeQuery = true)
    IEquityDistribution getEquityClassDistribution(Long companyId);
}
