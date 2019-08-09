package com.salon.controller;

import com.salon.BeautySalonApplication;
import com.salon.service.AppointmentService;
import com.salon.service.MasterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeautySalonApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-integrationtest.yml")
public class AppointmentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppointmentService service;

    @MockBean
    private MasterService masterService;

    @Test
    public void contextLoads() {
        assertNotNull(mvc);
        assertNotNull(service);
        assertNotNull(masterService);
    }




    @Test
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void givenEmployees_whenGetEmployees_thenStatus200()
            throws Exception {

        assertThat(service.findAll().size(), is(0));

        mvc.perform(post("api/appointments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postAppointment()))
                .andExpect(status().isOk());

//        mvc.perform(get("/api/employees")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].name", is("bob")));
    }


    private String postAppointment(){
        return "{\"appDate\": \"2019-08-13T12:00:00Z\"," +
                "\"master\": 21," +
                "\"serviceName\": \"HAIRCUT\"}";
    }

}
