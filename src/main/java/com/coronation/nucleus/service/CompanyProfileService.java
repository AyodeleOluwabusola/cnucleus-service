package com.coronation.nucleus.service;

import com.coronation.nucleus.IResponseEnum;
import com.coronation.nucleus.entities.CTUser;
import com.coronation.nucleus.entities.CompanyProfile;
import com.coronation.nucleus.entities.EquityClass;
import com.coronation.nucleus.entities.Shareholder;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        ResponseData responseData = new ResponseData();
        if (request.getCompanyProfileId() != null) {
            Optional<CompanyProfile> existingCompanyProfile = iCompanyProfileRepository.findById(request.getCompanyProfileId());
            if (existingCompanyProfile.isEmpty()) {
                responseData.setFormattedResponse(IResponseEnum.NO_COMPANY_PROFILE_FOUND,
                        String.valueOf(request.getCompanyProfileId()));
                return responseData;
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
                        responseData.setFormattedResponse(IResponseEnum.NO_SHAREHOLDER_FOUND,
                                String.valueOf(shareholderRequest.getShareholderId()));
                        return responseData;
                    }
                    shareholder = existingShareholder.get();
                }

                shareholder.setFirstName(shareholderRequest.getFirstName());
                shareholder.setLastName(shareholderRequest.getLastName());
                shareholder.setEmailAddress(shareholderRequest.getEmailAddress());
                shareholder.setTotalShares(shareholderRequest.getTotalShares());
                shareholder.setDateIssued(shareholderRequest.getDateIssued());
                EquityClass equityClass;
                if(shareholderRequest.getEquityClass().getId() != null) {
                    Optional<EquityClass> optionalEquityClass = iEquityClassRepository.findById(shareholderRequest.getEquityClass().getId());
                    if (optionalEquityClass.isEmpty()) {
                        responseData.setFormattedResponse(IResponseEnum.NO_EQUITY_CLASS_FOUND,
                                String.valueOf(shareholderRequest.getEquityClass()));
                        return responseData;
                    }
                    equityClass = optionalEquityClass.get();
                }else{
                    Set<EquityClass> prevEquityClass = companyProfile.getEquityClasses();
                    List<String> prevEquityClassName = Collections.emptyList();
                    if(prevEquityClass != null && !prevEquityClass.isEmpty()) {
                        prevEquityClassName = prevEquityClass.stream().map(EquityClass::getName).collect(Collectors.toList());
                    }
                    if(!prevEquityClassName.contains(shareholderRequest.getEquityClass().getName())) {
                        equityClass = new EquityClass();
                        equityClass.setName(StringUtils.upperCase(shareholderRequest.getEquityClass().getName()));
                        equityClass.setType(StringUtils.upperCase(shareholderRequest.getEquityClass().getType()));
                        iEquityClassRepository.save(equityClass);
                        companyProfile.addEquityClass(equityClass);
                    }else {
                        Optional<EquityClass> retrievedEquityClass = iEquityClassRepository.findByName(shareholderRequest.getEquityClass().getName());
                        equityClass = retrievedEquityClass.orElse(null);
                    }
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

        responseData.setResponse(IResponseEnum.SUCCESS);
        return responseData;
    }

    public IResponse retrievePendingCompanyProfileCreation(Long userId) {

        ResponseData<CompanyProfileResponse> responseData = new ResponseData<>();
        Optional<CTUser> ctUser = iUserRepository.findById(userId);
        if (ctUser.isEmpty()) {
            responseData.setResponse(IResponseEnum.NO_USER_FOUND);
            return responseData;
        }

        Long pendingRequestPk = ctUser.get().getPendingRequestPk();
        if (pendingRequestPk == null) {
            responseData.setResponse(IResponseEnum.NO_PENDING_REQUEST);
            return responseData;
        }

        Optional<CompanyProfile> companyProfile = iCompanyProfileRepository.findById(pendingRequestPk);
        if (companyProfile.isEmpty()) {
            responseData.setResponse(IResponseEnum.NO_PENDING_REQUEST);
            return responseData;
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
                responseData.setFormattedResponse(IResponseEnum.NO_EQUITY_CLASS_FOUND,
                        String.valueOf(shareholderRequest.getEquityClass()));
                return responseData;
            }
            shareholderRequest.setEquityClass(new EquityClassRequest(equityClass.get()));

            shareholders.add(shareholderRequest);
        }

        response.setShareholders(shareholders);

        responseData.setResponse(IResponseEnum.SUCCESS);
        responseData.setData(response);
        return responseData;
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
