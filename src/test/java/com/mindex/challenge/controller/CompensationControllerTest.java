package com.mindex.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationReport;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationControllerTest {

    @Spy
    private CompensationService compensationService;

    @InjectMocks
    private CompensationController compensationController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(compensationController).build();
    }

    @Test
    public void testRead() throws Exception {
        String employeeId = "1";
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Compensation compensation = new Compensation(employee, 1234.45f,"USD", new Date());
        CompensationReport compensationReport = new CompensationReport(Collections.singletonList(compensation));
        Mockito.when(compensationService.read(employeeId)).thenReturn(compensationReport);
        MvcResult mvcResult = this.mockMvc.perform(get("/compensation/1")).andExpect(status().isOk()).andReturn();
        String resultJson = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        CompensationReport resultReport = mapper.readValue(resultJson, CompensationReport.class);

        assertNotNull(resultReport);
        assertEquals(1, resultReport.getCompensationList().size());
        assertEquals("1", resultReport.getCompensationList().get(0).getEmployee().getEmployeeId());
        verify(compensationService, times(1)).read("1");
    }

    @Test
    public void testRead_noRequestParam() throws Exception {
        this.mockMvc.perform(get("/compensation")).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testCreate() throws Exception {
        String employeeId = "1";
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Compensation compensation = new Compensation(employee, 1234.45f,"USD", new Date());
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(compensation);
        Mockito.when(compensationService.create(Mockito.any(Compensation.class))).thenReturn(compensation);
        MvcResult mvcResult = this.mockMvc.perform(post("/compensation").
                contentType(MediaType.APPLICATION_JSON).content(requestJson)).
                andExpect(status().isOk()).andReturn();
        String resultJson = mvcResult.getResponse().getContentAsString();

        Compensation result = mapper.readValue(resultJson, Compensation.class);

        assertNotNull(result);
        assertEquals("1", result.getEmployee().getEmployeeId());
        verify(compensationService, times(1)).create(Mockito.any(Compensation.class));
    }

    @Test
    public void testCreate_noRequestBody() throws Exception {
        this.mockMvc.perform(post("/compensation").
                contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}
