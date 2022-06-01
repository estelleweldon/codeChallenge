package com.mindex.challenge.integration;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.impl.EmployeeServiceImplTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReportingStructureIntegrationTest extends EmployeeServiceImplTest {

    private String getReportingStructureUrl;

    @Before
    public void setup() {
        super.setup();
        getReportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{employeeId}";
    }

    @Test
    public void testRead_noReports() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        ReportingStructure reportingStructure = restTemplate.getForEntity(getReportingStructureUrl, ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();
        assertNotNull(reportingStructure);
        assertEquals(0, reportingStructure.getNumberOfReports().intValue());
        assertEquals(createdEmployee.getEmployeeId(), reportingStructure.getEmployee().getEmployeeId());
    }

    @Test
    public void testRead_withReports() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        Employee testManager = new Employee();
        testManager.setFirstName("John");
        testManager.setLastName("Doe");
        testManager.setDepartment("Engineering");
        testManager.setPosition("Developer");
        testManager.setDirectReports(Arrays.asList(createdEmployee1, createdEmployee2));
        Employee createdManager = restTemplate.postForEntity(employeeUrl, testManager, Employee.class).getBody();

        ReportingStructure reportingStructure = restTemplate.getForEntity(getReportingStructureUrl, ReportingStructure.class, createdManager.getEmployeeId()).getBody();
        assertNotNull(reportingStructure);
        assertEquals(2, reportingStructure.getNumberOfReports().intValue());
        assertEquals(createdManager.getEmployeeId(), reportingStructure.getEmployee().getEmployeeId());
    }

    @Test
    public void testBadCreateRequest_nullEmployee() {
        ReportingStructure reportingStructure = restTemplate.getForEntity(getReportingStructureUrl, ReportingStructure.class, "123").getBody();
        assertNotNull(reportingStructure);
        assertNull(reportingStructure.getEmployee());
        assertNull(reportingStructure.getNumberOfReports());
    }
}
