package com.coronation.nucleus.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CompanyProfile.class)
public abstract class CompanyProfile_ extends BaseEntity_ {

	public static volatile SingularAttribute<CompanyProfile, String> stage;
	public static volatile SetAttribute<CompanyProfile, Shareholder> shareholders;
	public static volatile SingularAttribute<CompanyProfile, String> companyType;
	public static volatile SingularAttribute<CompanyProfile, String> companyName;
	public static volatile SingularAttribute<CompanyProfile, Double> totalAuthorisedShares;
	public static volatile SingularAttribute<CompanyProfile, String> countryIncorporated;
	public static volatile SingularAttribute<CompanyProfile, String> currency;
	public static volatile SingularAttribute<CompanyProfile, LocalDate> incorporationDate;
	public static volatile SetAttribute<CompanyProfile, EquityClass> equityClasses;
	public static volatile SingularAttribute<CompanyProfile, CTUser> user;
	public static volatile SingularAttribute<CompanyProfile, Double> totalAllocated;

	public static final String STAGE = "stage";
	public static final String SHAREHOLDERS = "shareholders";
	public static final String COMPANY_TYPE = "companyType";
	public static final String COMPANY_NAME = "companyName";
	public static final String TOTAL_AUTHORISED_SHARES = "totalAuthorisedShares";
	public static final String COUNTRY_INCORPORATED = "countryIncorporated";
	public static final String CURRENCY = "currency";
	public static final String INCORPORATION_DATE = "incorporationDate";
	public static final String EQUITY_CLASSES = "equityClasses";
	public static final String USER = "user";
	public static final String TOTAL_ALLOCATED = "totalAllocated";

}

