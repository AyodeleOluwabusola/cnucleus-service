package com.coronation.nucleus.controller;

import com.coronation.nucleus.interfaces.IResponse;
import com.coronation.nucleus.request.EquityClassRequest;
import com.coronation.nucleus.request.IssueGrantRequest;
import com.coronation.nucleus.service.EquityClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("equity-class")
public class EquityClassController {

    @Autowired
    EquityClassService equityClassService;

    @PostMapping
    @RequestMapping("save")
    public IResponse createEquityClass(@RequestBody @Valid EquityClassRequest request) {
        return equityClassService.createEquityClass(request);
    }

    @PutMapping
    public IResponse editEquityClass(@RequestBody @Valid EquityClassRequest request) {
        return equityClassService.createEquityClass(request);
    }

    @PostMapping("issue-grant")
    public IResponse issueGrant(@RequestBody @Valid IssueGrantRequest request) {
        return equityClassService.issueGrant(request);
    }

    @DeleteMapping
    @RequestMapping("delete")
    public IResponse deleteEquityClass(List<Long> equityIds) {
        return equityClassService.deleteEquityClass(equityIds);
    }
}
