package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationReport;
import com.mindex.challenge.exceptions.CompensationException;
import com.mindex.challenge.exceptions.EmployeeException;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    /**
     * Reads compensation records from db
     * @param employeeId
     * @return List of compensation objects that belong to employeeId
     */
    @GetMapping("/compensation/{employeeId}")
    public CompensationReport read(@PathVariable String employeeId) throws EmployeeException {
        LOG.debug("Received compensation read request for [{}]", employeeId);

        return compensationService.read(employeeId);
    }

    /**
     * Creates new compensation record in db
     * @param compensation
     * @return Compensation object that was just saved
     */
    @PostMapping("/compensation")
    public Compensation create(@Valid @RequestBody Compensation compensation) throws EmployeeException, CompensationException {
        LOG.debug("Received compensation create request for [{}]", compensation);

        return compensationService.create(compensation);
    }
}
