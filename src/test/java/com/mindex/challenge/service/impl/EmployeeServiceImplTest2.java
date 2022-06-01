package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exceptions.EmployeeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest2 {

    @Spy
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Before
    public void setup() {
        when(employeeRepository.findByEmployeeId(null)).thenReturn(null);
        when(employeeRepository.findByEmployeeId("")).thenReturn(null);
        when(employeeRepository.findByEmployeeId("1234")).thenReturn(null);
    }

    @Test(expected = EmployeeException.class)
    public void testRead_nullId() throws EmployeeException {
        Employee nobody = employeeService.read(null);
    }

    @Test(expected = EmployeeException.class)
    public void testRead_emptyStringId() throws EmployeeException {
        Employee nobody = employeeService.read("");
    }

    @Test(expected = EmployeeException.class)
    public void testRead_badId() throws EmployeeException {
        Employee nobody = employeeService.read("1234");
    }
}
