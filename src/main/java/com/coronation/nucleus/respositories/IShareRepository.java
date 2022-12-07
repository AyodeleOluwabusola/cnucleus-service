package com.coronation.nucleus.respositories;

import com.coronation.nucleus.entities.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * @author toyewole
 */
public interface IShareRepository extends JpaRepository<Share, Long> {

    List<Share> findAllByIdIn(List<Long> ids);
}
