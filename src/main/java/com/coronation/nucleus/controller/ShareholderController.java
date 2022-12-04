package com.coronation.nucleus.controller;

import com.coronation.nucleus.pojo.ResponseData;
import com.coronation.nucleus.pojo.ShareDataResp;
import com.coronation.nucleus.request.ShareholderRequest;
import com.coronation.nucleus.service.ShareholderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author toyewole
 */
@RestController
@RequestMapping("/shareholder")
public class ShareholderController {

    @Autowired
    private ShareholderService shareholderService;


    @PostMapping()
    @RequestMapping("/{companyId}/create")
    public ResponseData<?> createShareholder(@PathVariable("companyId") Long companyId, @Valid @RequestBody ShareholderRequest shareholderRequest) {
        return shareholderService.handleShareholderCreation(companyId, shareholderRequest);
    }

    @PutMapping()
    @RequestMapping("/{companyId}/edit")
    public ResponseData<?> editShareholder(@Valid @RequestBody ShareholderRequest shareholderRequest) {
        return shareholderService.editShareholder(shareholderRequest);
    }

    @GetMapping("{companyId}")
    public ResponseData<ShareDataResp> getShareholders(@RequestParam("name") Optional<String> optionalName, @RequestParam int size,
                                                       @RequestParam int index, @PathVariable("companyId") Long companyId) {
        return shareholderService.handleShareholderDataReq(companyId, optionalName, size, index);
    }

    @DeleteMapping
    @RequestMapping("/share/{shareId}")
    public ResponseData<Boolean> softDeleteShareholders(@PathVariable(name = "shareId") Long shareId) {
        return shareholderService.handleShareHolderDelete(shareId);
    }


}
