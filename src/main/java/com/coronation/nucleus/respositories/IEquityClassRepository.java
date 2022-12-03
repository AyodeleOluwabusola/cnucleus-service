package com.coronation.nucleus.respositories;


import com.coronation.nucleus.entities.EquityClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author AuodeleOluwabusola
 */
public interface IEquityClassRepository extends JpaRepository<EquityClass, Long> {

    @Override
    Optional<EquityClass> findById(Long id);

    Optional<EquityClass> findByName(String name);
}
