package com.coronation.nucleus.service;

import com.coronation.nucleus.entities.EquityClass;
import com.coronation.nucleus.enums.IResponseEnum;
import com.coronation.nucleus.pojo.ResponseData;
import com.coronation.nucleus.request.EquityClassRequest;
import com.coronation.nucleus.request.IssueGrantRequest;
import com.coronation.nucleus.request.ShareholderRequest;
import com.coronation.nucleus.respositories.IEquityClassRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EquityClassService {

    @Autowired
    IEquityClassRepository iEquityClassRepository;

    @Autowired
    ShareholderService shareholderService;

    public ResponseData<?> createEquityClass(EquityClassRequest request) {

        EquityClass equityClass = new EquityClass();
        equityClass.setType(request.getType());
        equityClass.setName(request.getName());
        equityClass.setCode(getEquityCode(request.getName()));
        equityClass.setPricePerShare(request.getPricePerShare());
        equityClass.setTotalShares(request.getTotalShares());

        iEquityClassRepository.save(equityClass);

        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, equityClass);
    }

    public ResponseData<?> issueGrant(IssueGrantRequest request) {

        Optional<EquityClass> optionalEquityClass = iEquityClassRepository.findById(request.getEquityClassId());
        if (optionalEquityClass.isEmpty()) {
            return ResponseData.getResponseData(IResponseEnum.NO_EQUITY_CLASS_FOUND, null, null);
        }
        EquityClass equityClass = optionalEquityClass.get();
        if ((equityClass.getTotalAllocated() + request.getNumberOfShares()) > equityClass.getTotalShares()) {
            return ResponseData.getResponseData(IResponseEnum.ALLOCATION_SIZE_GREATER_THAN_TOTAL_SHARES, null, null);
        }

        equityClass.setTotalAllocated(equityClass.getTotalAllocated() + request.getNumberOfShares());
        iEquityClassRepository.save(equityClass);

        List<ShareholderRequest> shareholders = request.getShareholders();
        return shareholderService.handleShareholderCreation(request.getCompanyId(), shareholders.get(0));
    }

    public ResponseData<?> deleteEquityClass(List<Long> equityIds) {

        List<ResponseData<Long>> result = equityIds.stream().map(equityId ->
                iEquityClassRepository.findById(equityId).map(equityClass -> {
                    log.debug("Soft delete equity class id : {}", equityId);
                    equityClass.setTotalAllocated(equityClass.getTotalAllocated() - equityClass.getTotalShares());
                    equityClass.setActive(Boolean.FALSE);
                    equityClass.setDeleted(Boolean.TRUE);

                    iEquityClassRepository.save(equityClass);
                    return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, equityId);
                }).orElse(ResponseData.getResponseData(IResponseEnum.NO_SHAREHOLDER_FOUND, String.valueOf(equityId), equityId))).collect(Collectors.toList());
        return ResponseData.getResponseData(IResponseEnum.SUCCESS, null, result);
    }


    private String getEquityCode(String equityName) {
        return String.valueOf(equityName.charAt(0)) + equityName.charAt(equityName.length());
    }
}
