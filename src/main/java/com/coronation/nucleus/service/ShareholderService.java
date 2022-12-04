package com.coronation.nucleus.service;

import com.coronation.nucleus.entities.EquityClass;
import com.coronation.nucleus.entities.Share;
import com.coronation.nucleus.entities.Shareholder;
import com.coronation.nucleus.enums.IResponseEnum;
import com.coronation.nucleus.pojo.ResponseData;
import com.coronation.nucleus.request.ShareholderRequest;
import com.coronation.nucleus.respositories.ICompanyProfileRepository;
import com.coronation.nucleus.respositories.IEquityClassRepository;
import com.coronation.nucleus.respositories.IShareRepository;
import com.coronation.nucleus.respositories.IShareholderRepository;
import com.coronation.nucleus.respositories.filter.ShareholderQueryFilter;
import com.coronation.nucleus.util.ProxyTransformer;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * @author toyewole
 */
@Slf4j
@Service
public class ShareholderService {

    @Autowired
    ICompanyProfileRepository companyProfileRepository;

    @Autowired
    IShareholderRepository shareholderRepository;

    @Autowired
    IEquityClassRepository iEquityClassRepository;

    @Autowired
    IShareRepository iShareRepository;

    public ResponseData<?> handleShareholderCreation(Long companyId, ShareholderRequest shareholderRequest) {

        return companyProfileRepository.findById(companyId)
                .map(companyProfile -> {
                    Shareholder shareholder = Optional.ofNullable(shareholderRequest.getShareholderId())
                            .flatMap(id -> shareholderRepository.findById(id))
                            .orElse(ProxyTransformer.transformToShareholder(shareholderRequest));

                    if (shareholder == null) {
                        return ResponseData.getResponseData(IResponseEnum.NO_SHAREHOLDER_FOUND, null, null);
                    }
                    shareholder.setCompanyProfile(companyProfile);

                    Optional<EquityClass> optionalEquityClass = Optional
                            .ofNullable(shareholderRequest.getEquityClass().getId())
                            .flatMap(id -> iEquityClassRepository.findById(id));

                    if (optionalEquityClass.isEmpty()) {
                        return ResponseData.getResponseData(IResponseEnum.NO_EQUITY_CLASS_FOUND, null, null);
                    }
                    if (!isEquityUnderCompany(companyProfile.getEquityClasses(), optionalEquityClass.get())) {
                        ResponseData.getResponseData(IResponseEnum.EQUITY_NOT_UNDER_COMPANY_PROFILE, null, null);
                    }

                    Share share = new Share();
                    share.setShareholder(shareholder);
                    share.setTotalShares(shareholderRequest.getTotalShares());
                    share.setDateIssued(shareholderRequest.getDateIssued());
                    share.setEquityClass(optionalEquityClass.get());

                    shareholder.addShare(share);

                    companyProfile.getShareholders().add(shareholder);

                    companyProfileRepository.save(companyProfile);
                    shareholderRepository.save(shareholder);

                    return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, null);
                }).orElse(ResponseData.getResponseData(IResponseEnum.NO_COMPANY_PROFILE_FOUND, null, null));

    }

    private boolean isEquityUnderCompany(Set<EquityClass> equityClasses, EquityClass equityClass) {

        return equityClasses.stream().anyMatch(equity -> equityClass.getId().equals(equity.getId()));

    }

    public ResponseData<?> editShareholder(ShareholderRequest shareholderRequest) {

        if (shareholderRequest.getShareholderId() == null || shareholderRequest.getShareId() == null) {
            return ResponseData.getResponseData(IResponseEnum.INVALID_REQUEST, "ShareholderId or shareId cannot be null Kindly contact support", null);
        }


        return shareholderRepository.findById(shareholderRequest.getShareholderId())
                .map(shareholder -> {
                    shareholder.setFirstName(Optional.ofNullable(shareholderRequest.getFirstName())
                            .orElse(shareholderRequest.getCompanyName()));
                    shareholder.setLastName(shareholderRequest.getLastName());

                    shareholder.setShareholderTypeEnum(shareholderRequest.getShareholderType());
                    shareholder.setCategory(shareholderRequest.getCategory());


                    Optional<Share> optionalShare = iShareRepository.findById(shareholderRequest.getShareId());
                    if (optionalShare.isEmpty()) {
                        return ResponseData.getResponseData(IResponseEnum.INVALID_REQUEST, null, null);
                    }

                    Share share = optionalShare.get();
                    if (!Objects.equals(shareholderRequest.getEquityClass().getId(), share.getEquityClass().getId())) {
                        Optional<EquityClass> optionalEquityClass = iEquityClassRepository.findById(shareholderRequest.getEquityClass().getId());

                        if (optionalEquityClass.isEmpty()) {
                            return ResponseData.getResponseData(IResponseEnum.NO_EQUITY_CLASS_FOUND, null, null);
                        }

                        share.setEquityClass(optionalEquityClass.get());

                    }

                    shareholderRepository.save(shareholder);

                    return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, null);
                })
                .orElse(ResponseData.getResponseData(IResponseEnum.NO_SHAREHOLDER_FOUND, null, null));


    }

    public ResponseData<Page<Shareholder>> handleShareholderDataReq(Long companyId, Optional<String> optionalName, int size, int index) {

        Specification<Shareholder> specification = Specification
                .where(ShareholderQueryFilter.equalCompanyId(companyId))
                .and(ShareholderQueryFilter.equalActive(true))
                .and(ShareholderQueryFilter.likeFirstName(optionalName.orElse(null)));

        Page<Shareholder> page = shareholderRepository.findAll(specification, Pageable.ofSize(size).withPage(index));
        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, page);

    }

    @Transactional
    public ResponseData<Boolean> handleShareHolderDelete(Long shareholderId) {
        shareholderRepository.softDeleteShareholder(shareholderId);
        log.debug("Soft delete shareholder id : {}", shareholderId);
        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, null);
    }
}
