package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.EmployeeException;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    @Spy
    private EmployeeService employeeService;

    @InjectMocks
    private ReportingStructureServiceImpl reportingStructureService;

    private final Employee e1 = new Employee();
    private final Employee e2 = new Employee();
    private final Employee e3 = new Employee();
    private final Employee e4 = new Employee();

    @Before
    public void setup() {
    }

    private void createHierarchySimple() throws EmployeeException {
        e1.setEmployeeId("1");
        e1.setDirectReports(Arrays.asList(e2, e3));
        e2.setEmployeeId("2");
        e3.setEmployeeId("3");
        e3.setDirectReports(Collections.singletonList(e4));
        e4.setEmployeeId("4");
        when(employeeService.read(e1.getEmployeeId())).thenReturn(e1);
        when(employeeService.read(e2.getEmployeeId())).thenReturn(e2);
        when(employeeService.read(e3.getEmployeeId())).thenReturn(e3);
        when(employeeService.read(e4.getEmployeeId())).thenReturn(e4);
        when(employeeService.read("abcd")).thenThrow(new EmployeeException(EmployeeException.INVALID_EMPLOYEE));
    }

    private void createHierarchyWithCycle() throws EmployeeException {
        e1.setEmployeeId("1");
        e1.setDirectReports(Arrays.asList(e2, e3));
        e2.setEmployeeId("2");
        e3.setEmployeeId("3");
        e3.setDirectReports(Arrays.asList(e4, e1));
        e4.setEmployeeId("4");
        when(employeeService.read(e1.getEmployeeId())).thenReturn(e1);
        when(employeeService.read(e2.getEmployeeId())).thenReturn(e2);
        when(employeeService.read(e3.getEmployeeId())).thenReturn(e3);
        when(employeeService.read(e4.getEmployeeId())).thenReturn(e4);
    }

    private void createIncompleteHierarchy() throws EmployeeException {
        e1.setEmployeeId("1");
        e1.setDirectReports(Arrays.asList(e2, e3));
        e2.setEmployeeId("2");
        e3.setEmployeeId("3");
        e3.setDirectReports(Arrays.asList(e4, e1));
        e4.setEmployeeId("4");
        when(employeeService.read(e1.getEmployeeId())).thenReturn(e1);
        when(employeeService.read(e2.getEmployeeId())).thenReturn(e2);
        when(employeeService.read(e3.getEmployeeId())).thenReturn(e3);
        when(employeeService.read(e4.getEmployeeId())).thenThrow(new EmployeeException(EmployeeException.INVALID_EMPLOYEE));
    }

    @Test
    public void testGetReportingStructure_noReports() throws EmployeeException {
        createHierarchySimple();
        ReportingStructure reportingStructure = reportingStructureService.get(e4.getEmployeeId());
        assertEquals(Integer.valueOf(0), reportingStructure.getNumberOfReports());
        assertEquals(e4, reportingStructure.getEmployee());
        verify(employeeService, times(1)).read(Mockito.any(String.class));
    }

    @Test
    public void testGetReportingStructure_oneLevelOfReports() throws EmployeeException {
        createHierarchySimple();
        ReportingStructure reportingStructure = reportingStructureService.get(e3.getEmployeeId());
        assertEquals(Integer.valueOf(1), reportingStructure.getNumberOfReports());
        assertEquals(e3, reportingStructure.getEmployee());
        verify(employeeService, times(2)).read(Mockito.any(String.class));
    }

    @Test
    public void testGetReportingStructure_twoLevelsOfReports() throws EmployeeException {
        createHierarchySimple();
        ReportingStructure reportingStructure = reportingStructureService.get(e1.getEmployeeId());
        assertEquals(Integer.valueOf(3), reportingStructure.getNumberOfReports());
        assertEquals(e1, reportingStructure.getEmployee());
        verify(employeeService, times(4)).read(Mockito.any(String.class));
    }

    @Test
    public void testGetReportingStructure_withCycle() throws EmployeeException {
        createHierarchyWithCycle();
        ReportingStructure reportingStructure = reportingStructureService.get(e1.getEmployeeId());
        assertEquals(Integer.valueOf(3), reportingStructure.getNumberOfReports());
        assertEquals(e1, reportingStructure.getEmployee());
        verify(employeeService, times(4)).read(Mockito.any(String.class));
    }

    @Test(expected = EmployeeException.class)
    public void testGetReportingStructure_null() throws EmployeeException {
        when(employeeService.read(Mockito.nullable(String.class))).thenThrow(new EmployeeException(EmployeeException.INVALID_EMPLOYEE));
        ReportingStructure reportingStructure = reportingStructureService.get(null);
    }

    @Test(expected = EmployeeException.class)
    public void testGetReportingStructure_emptyString() throws EmployeeException {
        when(employeeService.read("")).thenThrow(new EmployeeException(EmployeeException.INVALID_EMPLOYEE));
        ReportingStructure reportingStructure = reportingStructureService.get("");
    }

    @Test(expected = EmployeeException.class)
    public void testGetReportingStructure_badString() throws EmployeeException {
        createHierarchySimple();
        ReportingStructure reportingStructure = reportingStructureService.get("abcd");
    }

    @Test(expected = EmployeeException.class)
    public void testGetReportingStructure_incompleteHierarchy() throws EmployeeException {
        createIncompleteHierarchy();
        ReportingStructure reportingStructure = reportingStructureService.get(e1.getEmployeeId());
    }
}
