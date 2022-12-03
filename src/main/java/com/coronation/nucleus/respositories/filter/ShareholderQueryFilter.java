package com.coronation.nucleus.respositories.filter;

import com.coronation.nucleus.entities.CompanyProfile_;
import com.coronation.nucleus.entities.Shareholder;
import com.coronation.nucleus.entities.Shareholder_;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;


/**
 * @author toyewole
 */

public class ShareholderQueryFilter {

    public static Specification<Shareholder> likeCompanyName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return (root, query, cb) -> cb.like(root.get(Shareholder_.FIRST_NAME), "%" + name + "%");
    }

    public static Specification<Shareholder> likeFirstName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return (root, query, cb) -> cb.like(root.get(Shareholder_.FIRST_NAME), "%" + name + "%");
    }

    public static Specification<Shareholder> equalCompanyId(Long companyId) {
        if (companyId == null ) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(Shareholder_.COMPANY_PROFILE), companyId);
    }

    public static Specification<Shareholder> equalActive(boolean active) {
        return (root, query, cb) -> cb.equal(root.get(Shareholder_.ACTIVE), active);
    }
}
