package com.coronation.nucleus.service;

import com.coronation.nucleus.entities.Share;
import com.coronation.nucleus.enums.EquityTypeEnum;
import com.coronation.nucleus.enums.IResponseEnum;
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

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    //TODO perform a check for a complete company profle so has to not cause trouble
    public IResponse createCompanyProfile(CompanyProfileRequest request) {

        CompanyProfile companyProfile = new CompanyProfile();
        if (request.getCompanyProfileId() != null) {
            Optional<CompanyProfile> existingCompanyProfile = iCompanyProfileRepository.findByIdAndStageIsNotContaining(request.getCompanyProfileId(), "FINAL");
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
                EquityClass equityClass = null;

                if (companyProfile.getEquityClasses() == null) {
                    equityClass = new EquityClass();
                    companyProfile.addEquityClass(equityClass);

                } else {
                    equityClass = companyProfile.getEquityClasses().iterator().next();
                }
                equityClass.setName("FOUNDER");
                equityClass.setCode(Optional.ofNullable(equityClass.getCode()).
                        orElse(generateEquityCode(equityClass.getName())));
                equityClass.setPricePerShare(request.getParValue());

                equityClass.setType(EquityTypeEnum.COMMON.name());


                iEquityClassRepository.save(equityClass);

                shareholder.setFirstName(shareholderRequest.getFirstName());
                shareholder.setLastName(shareholderRequest.getLastName());
                shareholder.setEmailAddress(shareholderRequest.getEmailAddress());
                shareholder.setShareholderTypeEnum(shareholderRequest.getShareholderType());

                Share share = new Share();
                share.setTotalShares(shareholderRequest.getTotalShares());
                share.setDateIssued(shareholderRequest.getDateIssued());
                share.setEquityClass(equityClass);

                shareholder.addShare(share);

                shareholder.setCompanyProfile(companyProfile);

                companyProfile.addShareholder(shareholder);
            }
        }

        iCompanyProfileRepository.save(companyProfile);
        if (!StringUtils.equalsIgnoreCase(request.getStage(), Constants.FINAL)) {
            iUserRepository.setPendingRequestPk(companyProfile.getId(), request.getRequestingUser());
        }


        return ResponseData.getResponseData(IResponseEnum.SUCCESS, "", companyProfile);
    }

    private String generateEquityCode(@NotNull String name) {
        return name.substring(0, 2) + Constants.DATE_TIME_FORMATTER.format(LocalDateTime.now());
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
        response.setStage(profile.getStage());
        response.setRequestingUser(profile.getUser().getId());

        List<ShareholderRequest> shareholders = new ArrayList<>();
        for (Shareholder shareholder : profile.getShareholders()) {
            ShareholderRequest shareholderRequest = new ShareholderRequest();
            shareholderRequest.setShareholderId(shareholder.getId());
            shareholderRequest.setFirstName(shareholder.getFirstName());
            shareholderRequest.setLastName(shareholder.getLastName());
            shareholderRequest.setEmailAddress(shareholder.getEmailAddress());

            Share share = shareholder.getShares().iterator().next();

            shareholderRequest.setTotalShares(share.getTotalShares());
            shareholderRequest.setDateIssued(share.getDateIssued());
            Optional<EquityClass> equityClass = Optional.ofNullable(share.getEquityClass());
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
        double totalIssuedShares;
        if (equityClassId != null) {
            numberOfShareholders = iShareholderRepository.getCountByCompanyProfileIdAndEquityClassId(companyId, equityClassId);
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
