package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationReport;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exceptions.CompensationException;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    @Spy
    private CompensationRepository compensationRepository;

    @Spy
    private EmployeeService employeeService;

    @InjectMocks
    private CompensationServiceImpl compensationService;

    private final Employee e1 = new Employee();
    private final Employee e2 = new Employee();
    private Compensation e2Compensation;

    @Before
    public void setup() throws EmployeeException {
        e1.setEmployeeId("1");
        e2.setEmployeeId("2");
        e2Compensation = new Compensation(e2, 1234.56f, "EUR", new Date(1641013200000000L));
        e2Compensation.setCompensationId("abc");
        when(compensationRepository.findByEmployee_EmployeeId(e2.getEmployeeId())).
                thenReturn(Collections.singletonList(e2Compensation));
        when(employeeService.read("1")).thenReturn(e1);
        when(employeeService.read("2")).thenReturn(e2);
        when(employeeService.read("3")).thenThrow(new EmployeeException(EmployeeException.INVALID_EMPLOYEE));
    }

    @Test
    public void testCreateCompensation() throws ParseException, EmployeeException, CompensationException {
        Float salary = 123456.78f;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Compensation compensation = new Compensation(e1, salary, "USD", formatter.parse("2022-05-27"));
        Compensation returnVal = compensationService.create(compensation);
        assertNotNull(returnVal.getCompensationId());
        assertEquals(e1.getEmployeeId(), returnVal.getEmployee().getEmployeeId());
        assertEquals(salary, returnVal.getSalary());
        assertEquals("USD", returnVal.getCurrencyUnit());
        assertEquals(1653624000000L, returnVal.getEffectiveDate().getTime());
        verify(compensationRepository, times(1)).insert(Mockito.any(Compensation.class));
    }

    @Test
    public void testReadCompensation() {
        CompensationReport returnReport = compensationService.read(e2.getEmployeeId());
        assertEquals(1, returnReport.getCompensationList().size());
        Compensation returnVal = returnReport.getCompensationList().get(0);
        assertNotNull(returnVal.getCompensationId());
        assertEquals(e2.getEmployeeId(), returnVal.getEmployee().getEmployeeId());
        assertEquals(e2Compensation.getSalary(), returnVal.getSalary());
        assertEquals("EUR", returnVal.getCurrencyUnit());
        assertEquals(1641013200000000L, returnVal.getEffectiveDate().getTime());
        verify(compensationRepository, times(1)).findByEmployee_EmployeeId(Mockito.any(String.class));
    }

    @Test
    public void testReadCompensation_emptyReturn() {
        CompensationReport returnReport = compensationService.read(e1.getEmployeeId());
        assertEquals(0, returnReport.getCompensationList().size());
        verify(compensationRepository, times(1)).findByEmployee_EmployeeId(Mockito.any(String.class));
    }

    @Test
    public void testReadCompensation_null() {
        when(compensationRepository.findByEmployee_EmployeeId(null)).thenReturn(null);
        CompensationReport returnReport = compensationService.read(null);
        assertNull(returnReport.getCompensationList());
    }

    @Test
    public void testReadCompensation_emptyString() {
        when(compensationRepository.findByEmployee_EmployeeId("")).thenReturn(null);
        CompensationReport returnReport = compensationService.read("");
        assertNull(returnReport.getCompensationList());
    }

    @Test
    public void testReadCompensation_badString() {
        when(compensationRepository.findByEmployee_EmployeeId("123")).thenReturn(null);
        CompensationReport returnReport = compensationService.read("123");
        assertNull(returnReport.getCompensationList());
    }
}
