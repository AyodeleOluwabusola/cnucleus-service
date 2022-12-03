package com.coronation.nucleus.service;

import com.coronation.nucleus.enums.IResponseEnum;
import com.coronation.nucleus.entities.CTUser;
import com.coronation.nucleus.entities.CompanyProfile;
import com.coronation.nucleus.entities.EquityClass;
import com.coronation.nucleus.entities.Shareholder;
import com.coronation.nucleus.enums.ShareholderCategoryEnum;
import com.coronation.nucleus.interfaces.IResponse;
import com.coronation.nucleus.pojo.ResponseData;
import com.coronation.nucleus.pojo.response.CompanyDashboardResponse;
import com.coronation.nucleus.pojo.response.CompanyProfileResponse;
import com.coronation.nucleus.request.CompanyProfileRequest;
import com.coronation.nucleus.request.EquityClassRequest;
import com.coronation.nucleus.request.ShareholderRequest;
import com.coronation.nucleus.respositories.ICompanyProfileRepository;
import com.coronation.nucleus.respositories.IEquityClassRepository;
import com.coronation.nucleus.respositories.IShareholderRepository;
import com.coronation.nucleus.respositories.IUserRepository;
import com.coronation.nucleus.util.AppProperties;
import com.coronation.nucleus.util.Constants;
import com.coronation.nucleus.util.ProxyTransformer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toyewole
 */

@Transactional
@Service
@Slf4j
public class CompanyProfileService {

    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    IShareholderRepository iShareholderRepository;

    @Autowired
    ICompanyProfileRepository iCompanyProfileRepository;

    @Autowired
    IEquityClassRepository iEquityClassRepository;

    @Autowired
    AppProperties appProperties;

    public IResponse createCompanyProfile(CompanyProfileRequest request) {

        CompanyProfile companyProfile = new CompanyProfile();
        if (request.getCompanyProfileId() != null) {
            Optional<CompanyProfile> existingCompanyProfile = iCompanyProfileRepository.findById(request.getCompanyProfileId());
            if (existingCompanyProfile.isEmpty()) {
                return ResponseData.getResponseData(IResponseEnum.NO_COMPANY_PROFILE_FOUND, String.valueOf(request.getCompanyProfileId()), null);
            }
            companyProfile = existingCompanyProfile.get();
        }

        companyProfile.setCompanyName(request.getCompanyName());
        companyProfile.setCompanyType(request.getCompanyType());
        companyProfile.setIncorporationDate(request.getIncorporationDate());
        companyProfile.setCountryIncorporated(request.getCountryIncorporated());
        companyProfile.setCurrency(request.getCurrency());
        companyProfile.setTotalAuthorisedShares(request.getTotalAuthorisedShares());
        companyProfile.setParValue(request.getParValue());
        companyProfile.setUser(iUserRepository.getReferenceById(request.getRequestingUser()));
        companyProfile.setStage(request.getStage());

        if (request.getShareholders() != null && !request.getShareholders().isEmpty()) {
            for (ShareholderRequest shareholderRequest : request.getShareholders()) {
                Shareholder shareholder = new Shareholder();
                if (shareholderRequest.getShareholderId() != null) {
                    Optional<Shareholder> existingShareholder = iShareholderRepository.findById(shareholderRequest.getShareholderId());
                    if (existingShareholder.isEmpty()) {
                        return ResponseData.getResponseData(IResponseEnum.NO_SHAREHOLDER_FOUND, String.valueOf(shareholderRequest.getShareholderId()), null);
                    }
                    shareholder = existingShareholder.get();
                }

                shareholder.setFirstName(shareholderRequest.getFirstName());
                shareholder.setLastName(shareholderRequest.getLastName());
                shareholder.setEmailAddress(shareholderRequest.getEmailAddress());
                shareholder.setTotalShares(shareholderRequest.getTotalShares());
                shareholder.setDateIssued(shareholderRequest.getDateIssued());
                shareholder.setShareholderTypeEnum(shareholderRequest.getShareholderType());
                EquityClass equityClass;
                if (shareholderRequest.getEquityClass().getId() != null) {
                    Optional<EquityClass> optionalEquityClass = iEquityClassRepository.findById(shareholderRequest.getEquityClass().getId());
                    if (optionalEquityClass.isEmpty()) {
                        return ResponseData.getResponseData(IResponseEnum.NO_EQUITY_CLASS_FOUND, String.valueOf(shareholderRequest.getEquityClass()), null);
                    }
                    equityClass = optionalEquityClass.get();
                } else {

                    Optional<EquityClass> classOptional = Optional.ofNullable(companyProfile.getEquityClasses()).orElse(Collections.emptySet()).stream().
                            filter(item -> StringUtils.equalsIgnoreCase(item.getName(), shareholderRequest.getEquityClass().getName()))
                            .findFirst();


                    equityClass = classOptional.orElseGet(() -> {
                        var equityClass1 = new EquityClass();
                        equityClass1.setName(StringUtils.upperCase(shareholderRequest.getEquityClass().getName()));
                        equityClass1.setType(StringUtils.upperCase(shareholderRequest.getEquityClass().getType()));
                        return equityClass1;
                    });
                    companyProfile.addEquityClass(equityClass);

                    iEquityClassRepository.save(equityClass);

                }
                shareholder.setCompanyProfile(companyProfile);
                shareholder.setEquityClass(equityClass);

                companyProfile.addShareholder(shareholder);
            }
        }

        iCompanyProfileRepository.save(companyProfile);
        if (!StringUtils.equalsIgnoreCase(request.getStage(), Constants.FINAL)) {
            iUserRepository.setPendingRequestPk(companyProfile.getId(), request.getRequestingUser());
        }


        return ResponseData.getResponseData(IResponseEnum.SUCCESS, "", companyProfile);
    }

    public IResponse retrievePendingCompanyProfileCreation(Long userId) {

        Optional<CTUser> ctUser = iUserRepository.findById(userId);
        if (ctUser.isEmpty()) {
            return ResponseData.getResponseData(IResponseEnum.NO_USER_FOUND, null, null);
        }

        Long pendingRequestPk = ctUser.get().getPendingRequestPk();
        if (pendingRequestPk == null) {
            return ResponseData.getResponseData(IResponseEnum.NO_PENDING_REQUEST, null, null);
        }

        Optional<CompanyProfile> companyProfile = iCompanyProfileRepository.findById(pendingRequestPk);
        if (companyProfile.isEmpty()) {
            return ResponseData.getResponseData(IResponseEnum.NO_PENDING_REQUEST, null, null);
        }

        CompanyProfile profile = companyProfile.get();

        CompanyProfileResponse response = new CompanyProfileResponse();
        response.setCompanyProfileId(profile.getId());
        response.setCompanyName(profile.getCompanyName());
        response.setCompanyType(profile.getCompanyType());
        response.setIncorporationDate(profile.getIncorporationDate().format(Constants.DATE_FORMATTER));
        response.setCountryIncorporated(profile.getCountryIncorporated());
        response.setCurrency(profile.getCurrency());
        response.setTotalAuthorisedShares(String.valueOf(profile.getTotalAuthorisedShares()));
        response.setParValue(profile.getParValue());
        response.setStage(profile.getStage());
        response.setRequestingUser(profile.getUser().getId());

        List<ShareholderRequest> shareholders = new ArrayList<>();
        for (Shareholder shareholder : profile.getShareholders()) {
            ShareholderRequest shareholderRequest = new ShareholderRequest();
            shareholderRequest.setShareholderId(shareholder.getId());
            shareholderRequest.setFirstName(shareholder.getFirstName());
            shareholderRequest.setLastName(shareholder.getLastName());
            shareholderRequest.setEmailAddress(shareholder.getEmailAddress());
            shareholderRequest.setTotalShares(shareholder.getTotalShares());
            shareholderRequest.setPricePerShare(shareholder.getPricePerShare());
            shareholderRequest.setDateIssued(shareholder.getDateIssued());
            Optional<EquityClass> equityClass = iEquityClassRepository.findById(shareholderRequest.getEquityClass().getId());
            if (equityClass.isEmpty()) {
                return ResponseData.getResponseData(IResponseEnum.SUCCESS, String.valueOf(shareholderRequest.getEquityClass()), null);
            }
            shareholderRequest.setEquityClass(new EquityClassRequest(equityClass.get()));

            shareholders.add(shareholderRequest);
        }

        response.setShareholders(shareholders);

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, response);

    }

    public IResponse getDashboardData(long companyId, Long equityClassId) {

        ResponseData<CompanyDashboardResponse> responseData = new ResponseData<>();

        Optional<CompanyProfile> companyProfile = iCompanyProfileRepository.findById(companyId);
        if (companyProfile.isEmpty()) {
            responseData.setResponse(IResponseEnum.NO_COMPANY_PROFILE_FOUND);
            return responseData;
        }

        CompanyDashboardResponse response = new CompanyDashboardResponse();
        long numberOfShareholders;
        long totalIssuedShares;
        if (equityClassId != null) {
            numberOfShareholders = iShareholderRepository.countByCompanyProfileIdAndEquityClassId(companyId, equityClassId);
            totalIssuedShares = iShareholderRepository.allIssuedSharesByCompanyIdAndEquityClass(companyId, equityClassId);
        } else {
            numberOfShareholders = iShareholderRepository.countByCompanyProfileId(companyId);
            totalIssuedShares = iShareholderRepository.allIssuedSharesByCompanyId(companyId);
        }

        response.setNumberOfShareholders(numberOfShareholders);
        response.setTotalAuthorizedShares(companyProfile.get().getTotalAuthorisedShares());
        response.setTotalIssuedShares(totalIssuedShares);
        response.setEquityClassDistribution(iShareholderRepository.getEquityClassDistribution(companyId));

        responseData.setData(response);
        responseData.setResponse(IResponseEnum.SUCCESS);
        return responseData;
    }
}
