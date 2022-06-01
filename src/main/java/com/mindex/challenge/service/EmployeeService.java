package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exceptions.EmployeeException;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id) throws EmployeeException;
    Employee update(Employee employee);
}
