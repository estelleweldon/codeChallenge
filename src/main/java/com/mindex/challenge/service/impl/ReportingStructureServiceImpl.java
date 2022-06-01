package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.EmployeeException;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure get(String employeeId) throws EmployeeException {
        LOG.debug("Getting reporting structure for employee [{}]", employeeId);
        Employee manager = employeeService.read(employeeId);
        List<String> reports = new ArrayList<>();
        reports.add(manager.getEmployeeId());
        visitEmployeesInHierarchy(manager, reports);
        return new ReportingStructure(manager, reports.size() - 1);
    }

    private void visitEmployeesInHierarchy(Employee employee, List<String> visitedEmployeeIds) throws EmployeeException {
        if(employee.getDirectReports() != null) {
//            for (Employee report : employee.getDirectReports()) {
            for(int i = 0; i < employee.getDirectReports().size(); i++) {
                Employee report = employee.getDirectReports().get(i);
                String reportId = report.getEmployeeId();
                if(!visitedEmployeeIds.contains(reportId)) {
                    visitedEmployeeIds.add(reportId);
                    Employee fullReport = employeeService.read(report.getEmployeeId());
                    employee.getDirectReports().set(i, fullReport);
                    if (fullReport.getDirectReports() != null && fullReport.getDirectReports().size() > 0) {
                        visitEmployeesInHierarchy(fullReport, visitedEmployeeIds);
                    }
                }
            }
        }
    }
}
