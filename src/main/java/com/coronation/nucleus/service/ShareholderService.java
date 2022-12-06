package com.coronation.nucleus.service;

import com.coronation.nucleus.entities.EquityClass;
import com.coronation.nucleus.entities.Share;
import com.coronation.nucleus.entities.Shareholder;
import com.coronation.nucleus.enums.IResponseEnum;
import com.coronation.nucleus.pojo.ResponseData;
import com.coronation.nucleus.pojo.ShareDTO;
import com.coronation.nucleus.pojo.ShareDataResp;
import com.coronation.nucleus.request.ShareholderRequest;
import com.coronation.nucleus.respositories.ICompanyProfileRepository;
import com.coronation.nucleus.respositories.IEquityClassRepository;
import com.coronation.nucleus.respositories.IShareRepository;
import com.coronation.nucleus.respositories.IShareholderRepository;
import com.coronation.nucleus.respositories.ShareJdbcTemplate;
import com.coronation.nucleus.util.ProxyTransformer;

import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    ShareJdbcTemplate shareJdbcTemplate;

    public List<ResponseData<?>> handleShareholderCreation(List<ShareholderRequest> shareholderRequestList) {

        return shareholderRequestList.stream()
                .map(shareholderRequest -> companyProfileRepository.findById(shareholderRequest.getCompanyId())
                        .map(companyProfile -> handleShareholderCreation(shareholderRequest, companyProfile))
                        .orElse(ResponseData.getResponseData(IResponseEnum.NO_COMPANY_PROFILE_FOUND, null, null)))
                .collect(Collectors.toList());
    }

    private ResponseData<Object> handleShareholderCreation(ShareholderRequest shareholderRequest, com.coronation.nucleus.entities.CompanyProfile companyProfile) {
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

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, shareholder.getId());
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

    public ResponseData<ShareDataResp> handleShareholderDataReq(Long companyId, Optional<String> optionalName, int size, int index) {

        List<ShareDTO> shareDTOS = shareJdbcTemplate.getShareDtos(companyId, optionalName.orElse(null), index, size);
        long count = shareJdbcTemplate.getCount(companyId, optionalName.orElse(null));

        var shareDataResp = new ShareDataResp();
        shareDataResp.setShareDTOList(shareDTOS);
        shareDataResp.setCount(count);

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, shareDataResp);

    }

    @Transactional
    public List<ResponseData<Long>> handleShareHolderDelete(List<Long> shareIds) {

       return shareIds.stream()
                .map(shareId -> iShareRepository.findById(shareId)
                .map(share -> deleteShares(shareId, share))
                        .orElse(ResponseData.getResponseData(IResponseEnum.NO_SHAREHOLDER_FOUND, null, shareId)))
               .collect(Collectors.toList());

    }

    private ResponseData<Long> deleteShares(Long shareId, Share share) {
        EquityClass equityClass = share.getEquityClass();

        equityClass.setTotalAllocated(equityClass.getTotalAllocated() - share.getTotalShares());
        log.debug("Soft delete shareholder id : {}", share.getId());

        share.setActive(Boolean.FALSE);
        share.setDeleted(Boolean.TRUE);

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, shareId);
    }
}
