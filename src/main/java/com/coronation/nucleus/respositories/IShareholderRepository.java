package com.coronation.nucleus.respositories;


import com.coronation.nucleus.entities.Shareholder;
import com.coronation.nucleus.interfaces.IEquityDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author AuodeleOluwabusola
 */
public interface IShareholderRepository extends JpaRepository<Shareholder, Long>, JpaSpecificationExecutor<Shareholder> {

    @Override
    Optional<Shareholder> findById(Long id);

    long countByCompanyProfileId(Long companyId);

    @Query("select  count (s) from Share s  where s.shareholder.companyProfile.id =:companyId and s.equityClass.id =:equityClassId")
    long getCountByCompanyProfileIdAndEquityClassId(Long companyId, Long equityClassId);


    @Query(value = " select  sum ( s.totalShares) from Share s where s.shareholder.companyProfile.id = :companyId ")
    long allIssuedSharesByCompanyId(Long companyId);

    @Query(value = "SELECT SUM(s.totalShares) from Share  s WHERE s.shareholder.companyProfile.id = :companyId and s.equityClass.id = :equityClass ")
    double allIssuedSharesByCompanyIdAndEquityClass(Long companyId, Long equityClass);


    @Query(value = "SELECT s.equityClass.id as equityClass, COUNT(s) from Share s WHERE s.shareholder.companyProfile.id = :companyId GROUP BY  s.equityClass.id ")
    List<IEquityDistribution> getEquityClassDistribution(Long companyId);


    @Modifying
    @Query(value = "update Shareholder set deleted = true, active=false where id = :shareholderId")
    void softDeleteShareholder(Long shareholderId);


}
