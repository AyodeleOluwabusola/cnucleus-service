package com.coronation.nucleus.respositories;

import com.coronation.nucleus.entities.Share;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author toyewole
 */
public interface IShareRepository extends JpaRepository<Share, Long> {
}
