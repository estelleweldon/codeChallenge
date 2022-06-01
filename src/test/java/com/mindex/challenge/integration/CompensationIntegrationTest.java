package com.mindex.challenge.integration;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationReport;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.impl.EmployeeServiceImplTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CompensationIntegrationTest extends EmployeeServiceImplTest {

    private String getCompensationUrl;
    private String createCompensationUrl;

    @Before
    public void setup() {
        super.setup();
        getCompensationUrl = "http://localhost:" + port + "/compensation/{employeeId}";
        createCompensationUrl = "http://localhost:" + port + "/compensation";
    }

    @Test
    public void testCreateReadCreateRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        {
            Compensation compensation = new Compensation();
            compensation.setEmployee(createdEmployee);
            compensation.setSalary(1234.56f);
            compensation.setCurrencyUnit("USD");
            compensation.setEffectiveDate(new Date());

            Compensation createdCompensation = restTemplate.postForEntity(createCompensationUrl, compensation, Compensation.class).getBody();

            assertNotNull(createdCompensation.getCompensationId());

            CompensationReport compensationReport = restTemplate.getForEntity(getCompensationUrl, CompensationReport.class, createdEmployee.getEmployeeId()).getBody();

            assertNotNull(compensationReport);
            assertNotNull(compensationReport.getCompensationList());
            assertEquals(1, compensationReport.getCompensationList().size());
            assertEquals(createdCompensation.getCompensationId(), compensationReport.getCompensationList().get(0).getCompensationId());
        }
        {
            Compensation compensation = new Compensation();
            compensation.setEmployee(createdEmployee);
            compensation.setSalary(789.10f);
            compensation.setCurrencyUnit("USD");
            compensation.setEffectiveDate(new Date());

            Compensation createdCompensation = restTemplate.postForEntity(createCompensationUrl, compensation, Compensation.class).getBody();

            assertNotNull(createdCompensation.getCompensationId());

            CompensationReport compensationReport = restTemplate.getForEntity(getCompensationUrl, CompensationReport.class, createdEmployee.getEmployeeId()).getBody();

            assertNotNull(compensationReport);
            assertNotNull(compensationReport.getCompensationList());
            assertEquals(2, compensationReport.getCompensationList().size());
        }
    }

    @Test
    public void testBadCreateRequest_nullEmployee() {
        Compensation compensation = new Compensation();
        compensation.setSalary(1234.56f);
        compensation.setCurrencyUnit("USD");
        compensation.setEffectiveDate(new Date());

        ResponseEntity<Compensation> compensationResponseEntity = restTemplate.postForEntity(createCompensationUrl, compensation, Compensation.class);
        assertEquals(HttpStatus.BAD_REQUEST, compensationResponseEntity.getStatusCode());
    }

    @Test
    public void testBadCreateRequest_missingFieldSalary() {
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId("123");
        Compensation compensation = new Compensation();
        compensation.setEmployee(testEmployee);
        compensation.setCurrencyUnit("USD");
        compensation.setEffectiveDate(new Date());

        ResponseEntity<Compensation> compensationResponseEntity = restTemplate.postForEntity(createCompensationUrl, compensation, Compensation.class);
        assertEquals(HttpStatus.BAD_REQUEST, compensationResponseEntity.getStatusCode());
    }

    @Test
    public void testBadCreateRequest_missingFieldCurrencyUnit() {
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId("123");
        Compensation compensation = new Compensation();
        compensation.setEmployee(testEmployee);
        compensation.setSalary(1234.56f);
        compensation.setEffectiveDate(new Date());

        ResponseEntity<Compensation> compensationResponseEntity = restTemplate.postForEntity(createCompensationUrl, compensation, Compensation.class);
        assertEquals(HttpStatus.BAD_REQUEST, compensationResponseEntity.getStatusCode());
    }

    @Test
    public void testBadCreateRequest_emptyFieldCurrencyUnit() {
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId("123");
        Compensation compensation = new Compensation();
        compensation.setEmployee(testEmployee);
        compensation.setSalary(1234.56f);
        compensation.setCurrencyUnit("");
        compensation.setEffectiveDate(new Date());

        ResponseEntity<Compensation> compensationResponseEntity = restTemplate.postForEntity(createCompensationUrl, compensation, Compensation.class);
        assertEquals(HttpStatus.BAD_REQUEST, compensationResponseEntity.getStatusCode());
    }

    @Test
    public void testBadCreateRequest_missingFieldEffectiveDate() {
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId("123");
        Compensation compensation = new Compensation();
        compensation.setEmployee(testEmployee);
        compensation.setSalary(1234.56f);
        compensation.setCurrencyUnit("USD");

        ResponseEntity<Compensation> compensationResponseEntity = restTemplate.postForEntity(createCompensationUrl, compensation, Compensation.class);
        assertEquals(HttpStatus.BAD_REQUEST, compensationResponseEntity.getStatusCode());
    }

    @Test
    public void testBadReadRequest_badEmployeeId() {
        CompensationReport compensationReport = restTemplate.getForEntity(getCompensationUrl, CompensationReport.class, "123").getBody();
        assertNotNull(compensationReport);
        assertEquals(0, compensationReport.getCompensationList().size());
    }
}
