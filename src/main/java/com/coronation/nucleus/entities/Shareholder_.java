package com.coronation.nucleus.entities;

import com.coronation.nucleus.enums.ShareholderCategoryEnum;
import com.coronation.nucleus.enums.ShareholderTypeEnum;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Shareholder.class)
public abstract class Shareholder_ extends com.coronation.nucleus.entities.BaseEntity_ {

	public static volatile SingularAttribute<Shareholder, String> firstName;
	public static volatile SingularAttribute<Shareholder, String> lastName;
	public static volatile SingularAttribute<Shareholder, LocalDate> dateIssued;
	public static volatile SingularAttribute<Shareholder, String> emailAddress;
	public static volatile SingularAttribute<Shareholder, EquityClass> equityClass;
	public static volatile SingularAttribute<Shareholder, Long> pricePerShare;
	public static volatile SingularAttribute<Shareholder, CompanyProfile> companyProfile;
	public static volatile SingularAttribute<Shareholder, ShareholderCategoryEnum> category;
	public static volatile SingularAttribute<Shareholder, ShareholderTypeEnum> shareholderTypeEnum;
	public static volatile SingularAttribute<Shareholder, Long> totalShares;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String DATE_ISSUED = "dateIssued";
	public static final String EMAIL_ADDRESS = "emailAddress";
	public static final String EQUITY_CLASS = "equityClass";
	public static final String PRICE_PER_SHARE = "pricePerShare";
	public static final String COMPANY_PROFILE = "companyProfile";
	public static final String CATEGORY = "category";
	public static final String SHAREHOLDER_TYPE_ENUM = "shareholderTypeEnum";
	public static final String TOTAL_SHARES = "totalShares";

}

