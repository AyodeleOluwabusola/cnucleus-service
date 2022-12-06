package com.coronation.nucleus.controller;

import com.coronation.nucleus.interfaces.IResponse;
import com.coronation.nucleus.request.CompanyProfileRequest;
import com.coronation.nucleus.service.CompanyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("company-profile")
public class CompanyProfileController {

    @Autowired
    CompanyProfileService companyProfileService;

    @PostMapping
    @RequestMapping("create")
    public IResponse createCompanyProfile(@RequestBody @Valid CompanyProfileRequest request) {
        return companyProfileService.createCompanyProfile(request);
    }

    @GetMapping("pending/{loggedInUser}")
    public IResponse createCompanyProfile(@PathVariable("loggedInUser") long id) {
        return companyProfileService.retrievePendingCompanyProfileCreation(id);
    }

    @GetMapping("dashboard/{companyId}")
    public IResponse getCompanyData(@PathVariable("companyId") long id, @RequestParam("equityClassId")Optional<Long> equityClassId) {
        return companyProfileService.getDashboardData(id, equityClassId);
    }
}
