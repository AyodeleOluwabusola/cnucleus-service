package com.coronation.nucleus.service;

import com.coronation.nucleus.entities.CompanyProfile;
import com.coronation.nucleus.entities.EquityClass;
import com.coronation.nucleus.entities.Share;
import com.coronation.nucleus.entities.Shareholder;
import com.coronation.nucleus.enums.IResponseEnum;
import com.coronation.nucleus.pojo.ResponseData;
import com.coronation.nucleus.pojo.ShareDTO;
import com.coronation.nucleus.pojo.ShareDataResp;
import com.coronation.nucleus.pojo.ShareRequest;
import com.coronation.nucleus.request.ShareholderRequest;
import com.coronation.nucleus.respositories.ICompanyProfileRepository;
import com.coronation.nucleus.respositories.IEquityClassRepository;
import com.coronation.nucleus.respositories.IShareRepository;
import com.coronation.nucleus.respositories.IShareholderRepository;
import com.coronation.nucleus.respositories.ShareJdbcTemplate;
import com.coronation.nucleus.util.ProxyTransformer;

import java.util.ArrayList;
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

    public ResponseData<List<ResponseData<?>>> handleShareholderCreation(List<ShareholderRequest> shareholderRequestList) {

        List<ResponseData<?>>  responseData =    shareholderRequestList.stream()
                .map(shareholderRequest -> Optional.ofNullable(shareholderRequest.getCompanyId())
                        .flatMap(companyId -> companyProfileRepository.findById(companyId))
                        .map(companyProfile -> handleShareholderCreation(shareholderRequest, companyProfile))
                        .orElse(ResponseData.getResponseData(IResponseEnum.NO_COMPANY_PROFILE_FOUND, String.valueOf(shareholderRequest.getCompanyId()), null))).collect(Collectors.toList());

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null , responseData);

    }

    private ResponseData<Shareholder> handleShareholderCreation(ShareholderRequest shareholderRequest, com.coronation.nucleus.entities.CompanyProfile companyProfile) {

        Shareholder shareholder = ProxyTransformer.transformToShareholder(shareholderRequest);
        if (shareholder == null) {
            return ResponseData.getResponseData(IResponseEnum.NO_SHAREHOLDER_FOUND, null, null);
        }
        shareholder.setCompanyProfile(companyProfile);

        ResponseData<List<Share>> sharesResponse = getSharesFromReq(shareholderRequest, companyProfile, shareholder);
        if (IResponseEnum.SUCCESS.getCode() != sharesResponse.getCode()) {
            return ResponseData.getResponseData(IResponseEnum.ERROR, sharesResponse.getDescription(), null);
        }
        shareholder.getShares().addAll(sharesResponse.getData());
        companyProfile.getShareholders().add(shareholder);

        companyProfileRepository.save(companyProfile);
        shareholderRepository.save(shareholder);

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, shareholder);
    }

    private ResponseData<List<Share>> getSharesFromReq(ShareholderRequest shareholderRequest, CompanyProfile companyProfile, Shareholder shareholder) {

        List<Share> shares = new ArrayList<>();
        //TODO retrieve equity from comapany
        for (ShareRequest request : shareholderRequest.getShares()) {
            Optional<EquityClass> optionalEquityClass = Optional.ofNullable(request.getEquityId())
                    .flatMap(id -> iEquityClassRepository.findById(id));

            if (optionalEquityClass.isEmpty()) {
                return ResponseData.getResponseData(IResponseEnum.NO_EQUITY_CLASS_FOUND, String.valueOf(request.getEquityId()), null);
            }
            if (!isEquityUnderCompany(companyProfile.getEquityClasses(), optionalEquityClass.get())) {
                return ResponseData.getResponseData(IResponseEnum.EQUITY_NOT_UNDER_COMPANY_PROFILE, null, null);
            }

            EquityClass equityClass = optionalEquityClass.get();

            var total = equityClass.getTotalShares() - (equityClass.getTotalAllocated() + request.getTotalShares());
            if (total < 0) {
                return ResponseData.getResponseData(IResponseEnum.ALLOCATION_SIZE_GREATER_THAN_TOTAL_SHARES, null, null);
            }

            Share share1 = new Share();
            share1.setShareholder(shareholder);
            share1.setTotalShares(request.getTotalShares());
            share1.setDateIssued(request.getDateIssued());

            share1.setEquityClass(optionalEquityClass.get());
            shares.add(share1);
        }

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, shares);

    }

    private boolean isEquityUnderCompany(Set<EquityClass> equityClasses, EquityClass equityClass) {

        return equityClasses.stream().anyMatch(equity -> equity.getId().equals(equityClass.getId()));

    }

    public ResponseData<?> editShareholder(ShareholderRequest shareholderRequest) {

        if (shareholderRequest.getShareholderId() == null) {
            return ResponseData.getResponseData(IResponseEnum.INVALID_REQUEST, "ShareholderId cannot be null Kindly contact support", null);
        }

        return shareholderRepository.findById(shareholderRequest.getShareholderId())
                .map(shareholder -> editShareholder(shareholderRequest, shareholder))
                .orElse(ResponseData.getResponseData(IResponseEnum.NO_SHAREHOLDER_FOUND, String.valueOf(shareholderRequest.getShareholderId()), null));

    }

    private ResponseData<Object> editShareholder(ShareholderRequest shareholderRequest, Shareholder shareholder) {
        shareholder.setFirstName(Optional.ofNullable(shareholderRequest.getFirstName())
                .orElse(shareholderRequest.getCompanyName()));
        shareholder.setLastName(shareholderRequest.getLastName());

        shareholder.setShareholderTypeEnum(shareholderRequest.getShareholderType());
        shareholder.setCategory(shareholderRequest.getCategory());

        ResponseData<String> saveResponse = updateSharesFromRequest(shareholder, shareholderRequest.getShares());
        if (IResponseEnum.SUCCESS.getCode() != saveResponse.getCode()) {
            return ResponseData.getResponseData(IResponseEnum.ERROR, saveResponse.getDescription(), null);
        }

        shareholderRepository.save(shareholder);

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, shareholder.getId());
    }

    private ResponseData<String> updateSharesFromRequest(Shareholder shareholder, List<ShareRequest> shareRequestList) {

        for (ShareRequest shareRequest : shareRequestList) {
            Optional<Share> optionalShare = getShareFromShareholder(shareholder, shareRequest.getShareId());

            if (optionalShare.isEmpty()) {
                return ResponseData.getResponseData(IResponseEnum.NO_SHARE_FOUND, String.valueOf(shareRequest.getShareId()), null);
            }

            if (shareRequest.getEquityId() != null && !Objects.equals(optionalShare.get().getEquityClass().getId(), shareRequest.getEquityId())) {
                Optional<EquityClass> optionalEquityClass = iEquityClassRepository.findById(shareRequest.getEquityId());

                if (optionalEquityClass.isEmpty()) {
                    return ResponseData.getResponseData(IResponseEnum.NO_EQUITY_CLASS_FOUND, null, null);
                }
                if (!isEquityUnderCompany(shareholder.getCompanyProfile().getEquityClasses(), optionalEquityClass.get())) {
                    return ResponseData.getResponseData(IResponseEnum.NO_EQUITY_CLASS_FOUND, null, null);
                }
                //revert the previous equity back
                EquityClass prev = optionalShare.get().getEquityClass();
                prev.setTotalAllocated(prev.getTotalAllocated() - optionalShare.get().getTotalShares());

                EquityClass newEquity = optionalEquityClass.get();
                double total = newEquity.getTotalShares() - (newEquity.getTotalAllocated() + shareRequest.getTotalShares());

                if (total < 0) {
                    return ResponseData.getResponseData(IResponseEnum.ALLOCATION_SIZE_GREATER_THAN_TOTAL_SHARES, null, null);
                }

                iEquityClassRepository.save(prev);

                newEquity.setTotalAllocated(newEquity.getTotalAllocated() + shareRequest.getTotalShares());
                // saving would be cascaded.
                log.debug("updating the new equity class {} ", newEquity.getId());
                optionalShare.get().setEquityClass(newEquity);

            } else if (!optionalShare.get().getTotalShares().equals(shareRequest.getTotalShares())) {
                //to avoid scenario where the total share is updated
                EquityClass equityClass = optionalShare.get().getEquityClass();

                var allocated = equityClass.getTotalAllocated();
                allocated = allocated - optionalShare.get().getTotalShares();
                var total = equityClass.getTotalShares() - (allocated + shareRequest.getTotalShares());

                if (total < 0) {
                    return ResponseData.getResponseData(IResponseEnum.ALLOCATION_SIZE_GREATER_THAN_TOTAL_SHARES, null, null);
                }
                equityClass.setTotalAllocated(equityClass.getTotalAllocated() + shareRequest.getTotalShares());
            }
        }

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, null);
    }

    private Optional<Share> getShareFromShareholder(Shareholder shareholder, Long shareId) {
        if (shareId == null) {
            return Optional.empty();
        }
        return shareholder
                .getShares().stream().filter(share -> share.getId().equals(shareId)).findFirst();

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
                        .orElse(ResponseData.getResponseData(IResponseEnum.NO_SHAREHOLDER_FOUND, String.valueOf(shareId), null)))
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
