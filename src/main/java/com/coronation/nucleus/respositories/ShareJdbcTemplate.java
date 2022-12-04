package com.coronation.nucleus.respositories;

import com.coronation.nucleus.pojo.ShareDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author toyewole
 */

@Service
public class ShareJdbcTemplate {


    @PersistenceContext
    EntityManager entityManager;

    public List<ShareDTO> getShareDtos(Long companyId, String name, int index, int pageSize) {

        String whereClause = getWhereClause(companyId, name);
        TypedQuery<ShareDTO> type = entityManager.createQuery(
                " SELECT new com.coronation.nucleus.pojo.ShareDTO ( s.id , s.shareholder.id , s.shareholder.firstName, s.shareholder.lastName, " +
                        " s.shareholder.emailAddress , s.shareholder.shareholderTypeEnum, s.equityClass.name  ) " +
                        " FROM Share s  where s.active = true " + whereClause,
                ShareDTO.class);

        setParameters(type, name, companyId);
        type.setFirstResult(index);
        type.setMaxResults(pageSize < 0 ? 10 : pageSize);

        return type.getResultList();

    }


    public Long getCount(Long companyId, String name) {

        String whereClause = getWhereClause(companyId, name);
        TypedQuery<Long> type = entityManager.createQuery(
                "SELECT count (distinct  s.id) FROM Share s where s.active = true " + whereClause,
                Long.class);

        setParameters(type, name, companyId);

        return type.getSingleResult();
    }



    private  <T> void setParameters(TypedQuery<T> type, String name, Long companyId) {
        if (StringUtils.isNotBlank(name)) {
            type.setParameter("name", "%" + name.toUpperCase() + "%");

        }
        if (companyId != null) {
            type.setParameter("companyId", companyId);
        }

    }

    private String getWhereClause(Long companyId, String name) {

        StringBuilder builder = new StringBuilder();

        if (companyId != null) {
            builder.append(" and  s.shareholder.companyProfile.id  =:companyId ");
        }
        if (StringUtils.isNotBlank(name)) {
            builder.append(" and  upper(s.shareholder.firstName)  like :name ");
        }

        return builder.toString();
    }
}
