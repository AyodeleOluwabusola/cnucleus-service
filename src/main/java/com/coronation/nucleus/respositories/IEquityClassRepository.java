package com.coronation.nucleus.respositories;


import com.coronation.nucleus.entities.EquityClass;
import com.coronation.nucleus.interfaces.IEquityClassDataTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author AuodeleOluwabusola
 */
public interface IEquityClassRepository extends JpaRepository<EquityClass, Long> {

    @Override
    Optional<EquityClass> findById(Long id);

    @Query(value = "select shh.first_name as shareholderFirstName, shh.last_name as shareholderLastName, ec.code, sh.total_shares as shareholderTotalShare, ec.total_shares as equityClassTotalShare, " +
            "ec.price_per_share as equityClassPricePerShare " +
            "from share_holder shh " +
            "join share sh on sh.SHARE_HOLDER_FK = shh.id " +
            "join equity_class ec on ec.id = :equityClassId", nativeQuery = true)
    List<IEquityClassDataTable> getEquityClassDataTable(Long equityClassId);

}
