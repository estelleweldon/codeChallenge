package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationReport;
import com.mindex.challenge.exceptions.CompensationException;
import com.mindex.challenge.exceptions.EmployeeException;

public interface CompensationService {
    /**
     * Saves a new Compenstation object to the db
     * @param compensation
     * @return
     */
    Compensation create(Compensation compensation) throws EmployeeException, CompensationException;

    /**
     * Returns all the Compensation objects that belong to employee
     * @param employeeId
     * @return
     */
    CompensationReport read(String employeeId) throws EmployeeException;
}
