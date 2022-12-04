package com.coronation.nucleus.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EquityClass.class)
public abstract class EquityClass_ extends BaseEntity_ {

	public static volatile SingularAttribute<EquityClass, String> code;
	public static volatile SingularAttribute<EquityClass, String> name;
	public static volatile SingularAttribute<EquityClass, Double> pricePerShare;
	public static volatile SingularAttribute<EquityClass, String> type;
	public static volatile SingularAttribute<EquityClass, Double> totalShares;
	public static volatile SingularAttribute<EquityClass, Double> totalAllocated;

	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String PRICE_PER_SHARE = "pricePerShare";
	public static final String TYPE = "type";
	public static final String TOTAL_SHARES = "totalShares";
	public static final String TOTAL_ALLOCATED = "totalAllocated";

}

