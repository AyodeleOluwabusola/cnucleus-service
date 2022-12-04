package com.coronation.nucleus.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Share.class)
public abstract class Share_ extends BaseEntity_ {

	public static volatile SingularAttribute<Share, LocalDate> dateIssued;
	public static volatile SingularAttribute<Share, EquityClass> equityClass;
	public static volatile SingularAttribute<Share, Shareholder> shareholder;
	public static volatile SingularAttribute<Share, Double> totalShares;

	public static final String DATE_ISSUED = "dateIssued";
	public static final String EQUITY_CLASS = "equityClass";
	public static final String SHAREHOLDER = "shareholder";
	public static final String TOTAL_SHARES = "totalShares";

}

