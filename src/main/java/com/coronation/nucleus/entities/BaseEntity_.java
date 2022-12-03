package com.coronation.nucleus.entities;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BaseEntity.class)
public abstract class BaseEntity_ {

	public static volatile SingularAttribute<BaseEntity, Boolean> deleted;
	public static volatile SingularAttribute<BaseEntity, Boolean> active;
	public static volatile SingularAttribute<BaseEntity, Long> id;
	public static volatile SingularAttribute<BaseEntity, LocalDateTime> lastModified;
	public static volatile SingularAttribute<BaseEntity, LocalDateTime> createDate;

	public static final String DELETED = "deleted";
	public static final String ACTIVE = "active";
	public static final String ID = "id";
	public static final String LAST_MODIFIED = "lastModified";
	public static final String CREATE_DATE = "createDate";

}

