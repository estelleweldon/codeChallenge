package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationReport;
import com.mindex.challenge.exceptions.CompensationException;
import com.mindex.challenge.exceptions.EmployeeException;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeService employeeService;

    /**
     *
     * @param compensation
     * @return
     */
    @Override
    public Compensation create(Compensation compensation) throws EmployeeException, CompensationException {
        LOG.debug("Creating compensation [{}]", compensation);

        validateEmployeeId(compensation.getEmployee().getEmployeeId());
        compensation.setCompensationId(UUID.randomUUID().toString());
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public CompensationReport read(String employeeId) {
        LOG.debug("Reading compensation with employee id [{}]", employeeId);

        List<Compensation> compensations = compensationRepository.findByEmployee_EmployeeId(employeeId);
        return new CompensationReport(compensations);
    }

    private void validateEmployeeId(String employeeId) throws EmployeeException {
        employeeService.read(employeeId);
    }
}
