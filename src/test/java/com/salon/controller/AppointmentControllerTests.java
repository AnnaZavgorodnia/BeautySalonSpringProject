package com.salon.controller;

import com.salon.BeautySalonApplication;
import com.salon.entity.Appointment;
import com.salon.entity.Master;
import com.salon.entity.Service;
import com.salon.service.AppointmentService;
import com.salon.service.ClientService;
import com.salon.service.MasterService;
import com.salon.service.ServiceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private AppointmentService appointmentService;

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
        assertNotNull(appointmentService);
        assertNotNull(masterService);
        assertNotNull(clientService);
        assertNotNull(serviceService);

        assertThat(appointmentService.findAll().size(), is(0));
        assertThat(serviceService.findAll().size(), is(0));
        assertThat(masterService.getAll().size(), is(0));
        assertThat(clientService.getAll().size(), is(0));

        //Adding some correct data before testing

        mvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postService()))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("HAIRCUT")));

        assertThat(serviceService.findAll().size(), is(1));

        Service service = serviceService.findAll().get(0);

        mvc.perform(post("/api/masters")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postMaster(service.getId())))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("master")));

        assertThat(masterService.getAll().size(), is(1));

        mvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postClient()))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("client")));

        assertThat(clientService.getAll().size(), is(1));

        mvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postAdmin()))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("admin")));

        assertThat(clientService.getAll().size(), is(2));

        Master master = masterService.getAll().get(0);

        mvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postAppointment_valid_default(master.getId(), service.getName())))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assertThat(appointmentService.findAll().size(), is(1));
    }

    @After
    public void clearUp(){
        masterService.deleteAll();
        clientService.deleteAll();
        serviceService.deleteAll();
    }

    //POST api/appointments

    @Test
    @WithMockUser(username = "client",
            roles = {"CLIENT"})
    public void createAnAppointment_success()
            throws Exception {

        assertThat(serviceService.findAll().size(), is(1));
        assertThat(masterService.getAll().size(), is(1));

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
    @WithMockUser(username = "client",
            roles = {"CLIENT"})
    public void createAnAppointment_duplicateDataFailure()
            throws Exception{

        assertThat(serviceService.findAll().size(), is(1));
        assertThat(masterService.getAll().size(), is(1));

        Master master = masterService.getAll().get(0);
        Service service = serviceService.findAll().get(0);

        mvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(postAppointment_valid_default(master.getId(), service.getName())))
                .andExpect(status().isConflict());
    }

    // GET api/me/appointments

    @Test
    @WithMockUser(username = "admin",
            roles = {"CLIENT"})
    public void getAllUsersAppointments_success() throws Exception {

        assertThat(appointmentService.findAll().size(), is(1));

        mvc.perform(get("/api/me/appointments"))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    // GET api/appointments

    @Test
    @WithMockUser(username = "client",
            roles = {"CLIENT"})
    public void getAllAppointments_forbidden() throws Exception {
        mvc.perform(get("/api/appointments"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin",
            authorities = {"ADMIN"})
    public void getAllAppointments_success() throws Exception {
        mvc.perform(get("/api/appointments"))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    // GET api/appointments/{id}

    @Test
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void getAppointmentById_success() throws Exception {

        assertThat(appointmentService.findAll().size(), is(1));

        Appointment appointment = appointmentService.findAll().get(0);

        mvc.perform(get("/api/appointments/"+appointment.getId()))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appDate", is("13-09-2019")));
    }

    @Test
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void getAppointmentById_idDoesNotExistFailure() throws Exception {

        mvc.perform(get("/api/appointments/1234"))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // DELETE api/appointments/{id}

    @Test
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void deleteAppointmentById_success() throws Exception {

        assertThat(appointmentService.findAll().size(), is(1));

        Appointment appointment = appointmentService.findAll().get(0);

        mvc.perform(delete("/api/appointments/" + appointment.getId()))
                .andExpect(status().isOk());

        assertThat(appointmentService.findAll().size(), is(0));
    }

    @Test
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void deleteAppointmentById_idDoesNotExistFailure() throws Exception {

        assertThat(appointmentService.findAll().size(), is(1));

        mvc.perform(delete("/api/appointments/1234"))
                .andExpect(status().isInternalServerError());

        assertThat(appointmentService.findAll().size(), is(1));
    }

    // GET api/masters/{id}/appointments

    @Test
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void getMastersAppointments_success() throws Exception {

        assertThat(appointmentService.findAll().size(), is(1));
        assertThat(masterService.getAll().size(), is(1));

        Master master = masterService.getAll().get(0);

        mvc.perform(get("/api/masters/" + master.getId() + "/appointments"))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void getMastersAppointments_noAppointmentsWithSuchMaster() throws Exception {

        mvc.perform(get("/api/masters/" + 1111 + "/appointments"))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    // GET api/masters/{id}/appointments/{date}

    @Test
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void getMastersAppointmentsByDate_success() throws Exception {

        assertThat(appointmentService.findAll().size(), is(1));
        assertThat(masterService.getAll().size(), is(1));

        Master master = masterService.getAll().get(0);

        mvc.perform(get("/api/masters/" + master.getId() + "/appointments/2019-09-13T12:00:00Z"))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @WithMockUser(username = "admin",
            roles = {"ADMIN"})
    public void getMastersAppointmentsByDate_noAppointmentsWithSuchMaster() throws Exception {

        mvc.perform(get("/api/masters/" + 1111 + "/appointments/2019-09-13T12:00:00Z"))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
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
                "\"master\": 123," +
                "\"serviceName\": \"\"}";
    }

}
