package com.coronation.nucleus.entities;

import com.coronation.nucleus.enums.ShareholderCategoryEnum;
import com.coronation.nucleus.enums.ShareholderTypeEnum;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Shareholder.class)
public abstract class Shareholder_ extends BaseEntity_ {

	public static volatile SetAttribute<Shareholder, Share> shares;
	public static volatile SingularAttribute<Shareholder, String> firstName;
	public static volatile SingularAttribute<Shareholder, String> lastName;
	public static volatile SingularAttribute<Shareholder, String> emailAddress;
	public static volatile SingularAttribute<Shareholder, CompanyProfile> companyProfile;
	public static volatile SingularAttribute<Shareholder, ShareholderCategoryEnum> category;
	public static volatile SingularAttribute<Shareholder, ShareholderTypeEnum> shareholderTypeEnum;

	public static final String SHARES = "shares";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String EMAIL_ADDRESS = "emailAddress";
	public static final String COMPANY_PROFILE = "companyProfile";
	public static final String CATEGORY = "category";
	public static final String SHAREHOLDER_TYPE_ENUM = "shareholderTypeEnum";

}

