package com.salon.controller;

import com.salon.BeautySalonApplication;
import com.salon.entity.Master;
import com.salon.entity.Service;
import com.salon.service.AppointmentService;
import com.salon.service.ClientService;
import com.salon.service.MasterService;
import com.salon.service.ServiceService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
        locations = "classpath:application.properties")
public class AppointmentControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AppointmentService service;

    @Autowired
    private MasterService masterService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ServiceService serviceService;

    @Before
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void contextLoads() throws Exception {
        assertNotNull(mvc);
        assertNotNull(service);
        assertNotNull(masterService);
        assertNotNull(clientService);

        //Adding some correct data before testing

        mvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postService()))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("HAIRCUT")));

        Service service = serviceService.findAll().get(0);

        mvc.perform(post("/api/masters")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postMaster(service.getId())))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("master")));


        mvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postClient()))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("client")));

        mvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postAdmin()))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("admin")));

        Master master = masterService.getAll().get(0);

        mvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postAppointment_valid_default(master.getId(), service.getName())))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @After
    public void clearUp(){
        masterService.deleteAll();
        service.deleteAll();
        clientService.deleteAll();
        serviceService.deleteAll();
    }

    @Test
    @WithMockUser(username = "client",
            roles = {"CLIENT"})
    public void createAnAppointment_success()
            throws Exception {

        Service service = serviceService.findAll().get(0);
        Master master = masterService.getAll().get(0);

        mvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postAppointment_valid(master.getId(), service.getName())))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "client",
            roles = {"CLIENT"})
    public void createAnAppointment_invalidDataFailure()
            throws Exception{
        mvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postAppointment_invalid()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin",
            roles = {"CLIENT"})
    public void getAllUsersAppointments_success() throws Exception {
        mvc.perform(get("/api/me/appointments"))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)));
    }



    private String postMaster(Long serviceId){
        return "{\"username\": \"master\"," +
                "\"firstName\": \"Aliona\"," +
                "\"lastName\": \"Last\"," +
                "\"email\": \"master123@gmail.com\"," +
                "\"instagram\": \"blablablab\"," +
                "\"password\": \"1Aaaa\"," +
                "\"position\": \"ART_STYLIST\"," +
                "\"services\": [" + serviceId + "]}";
    }

    private String postClient(){
        return "{\"username\": \"client\"," +
                "\"firstName\": \"name\"," +
                "\"lastName\": \"Last\"," +
                "\"email\": \"client@gmail.com\"," +
                "\"password\": \"1Aaaa\"}";
    }

    private String postAdmin(){
        return "{\"username\": \"admin\"," +
                "\"firstName\": \"name\"," +
                "\"lastName\": \"Last\"," +
                "\"email\": \"admin@gmail.com\"," +
                "\"password\": \"1Aaaa\"}";
    }

    private String postService(){
        return "{\"name\": \"HAIRCUT\"," +
                "\"price\": 2100}";
    }


    private String postAppointment_valid(Long masterId, String serviceName){
        return "{\"appDate\": \"2019-08-13T12:00:00Z\"," +
                "\"master\": " + masterId + "," +
                "\"serviceName\": \"" + serviceName + "\"}";
    }

    private String postAppointment_valid_default(Long masterId, String serviceName){
        return "{\"appDate\": \"2019-09-13T12:00:00Z\"," +
                "\"master\": " + masterId + "," +
                "\"serviceName\": \"" + serviceName + "\"}";
    }

    private String postAppointment_invalid(){
        return "{\"appDate\": \"2019-08-13T12:00:00Z\"," +
                "\"master\": 2," +
                "\"serviceName\": \"\"}";
    }

}
