package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.EmployeeException;

public interface ReportingStructureService {
    /**
     * Returns ReportingStructure of the employee whose id is passed in
     * @param employeeId
     * @return
     */
    ReportingStructure get(String employeeId) throws EmployeeException;
}
