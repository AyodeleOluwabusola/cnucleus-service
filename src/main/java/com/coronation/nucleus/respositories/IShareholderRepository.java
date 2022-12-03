package com.coronation.nucleus.respositories;


import com.coronation.nucleus.entities.Shareholder;
import com.coronation.nucleus.interfaces.IEquityDistribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author AuodeleOluwabusola
 */
public interface IShareholderRepository extends JpaRepository<Shareholder, Long> , JpaSpecificationExecutor<Shareholder> {

    @Override
    Optional<Shareholder> findById(Long id);

    long countByCompanyProfileId(Long companyId);

    long countByCompanyProfileIdAndEquityClassId(Long companyId, Long equityClassId);

    @Query(value = "SELECT SUM(TOTAL_SHARES) from SHARE_HOLDER WHERE COMPANY_PROFILE_FK = :companyId", nativeQuery = true)
    long allIssuedSharesByCompanyId(Long companyId);

    @Query(value = "SELECT SUM(TOTAL_SHARES) from SHARE_HOLDER WHERE COMPANY_PROFILE_FK = :companyId AND EQUITY_CLASS_FK = :equityClass", nativeQuery = true)
    long allIssuedSharesByCompanyIdAndEquityClass(Long companyId, Long equityClass);

    @Query(value = "SELECT EQUITY_CLASS_FK as equityClass, COUNT(*) from SHARE_HOLDER sh WHERE COMPANY_PROFILE_FK = :companyId GROUP BY EQUITY_CLASS_FK", nativeQuery = true)
    List<IEquityDistribution> getEquityClassDistribution(Long companyId);


    @Modifying
    @Query(value = "update Shareholder set deleted = true, active=false where id = :shareholderId" )
    void softDeleteShareholder(Long shareholderId);


}
