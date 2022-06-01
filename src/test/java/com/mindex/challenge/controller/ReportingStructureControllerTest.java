package com.mindex.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureControllerTest {

    @Spy
    private ReportingStructureService reportingStructureService;

    @InjectMocks
    private ReportingStructureController reportingStructureController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(reportingStructureController).build();
    }

    @Test
    public void testRead() throws Exception {
        String employeeId = "1";
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        ReportingStructure reportingStructure = new ReportingStructure(employee, 3);
        Mockito.when(reportingStructureService.get(Mockito.anyString())).thenReturn(reportingStructure);
        MvcResult mvcResult = this.mockMvc.perform(get("/reportingStructure/" + employeeId)).andExpect(status().isOk()).andReturn();
        String resultJson = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ReportingStructure result = mapper.readValue(resultJson, ReportingStructure.class);

        assertNotNull(result);
        assertEquals("1", result.getEmployee().getEmployeeId());
        assertEquals(3, result.getNumberOfReports().intValue());
        verify(reportingStructureService, times(1)).get("1");
    }

    @Test
    public void testRead_noRequestParam() throws Exception {
        this.mockMvc.perform(get("/reportingStructure")).andExpect(status().isNotFound());
    }

}
