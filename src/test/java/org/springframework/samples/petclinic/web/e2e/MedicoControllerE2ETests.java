package org.springframework.samples.petclinic.web.e2e;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.MedicoController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class MedicoControllerE2ETests {
    
    @Autowired
    private MedicoController medicoController;
    @Autowired
    private UserService userService;
    @Autowired
    private MedicoService medicoService;
    @Autowired
    private AuthoritiesService authoService;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "alvaroMedico", authorities = {"medico"})
    @Test
    void testShowMedico() throws Exception{
    
    mockMvc.perform(get("/medicos/{medicoId}", 1))
        .andExpect(status().isOk())
        .andExpect(view().name("medicos/medicoDetails"))
        .andExpect(model().attributeExists("medico"));
    }

    @Test
    void testShowMedicoNotLogged() throws Exception{
    
    mockMvc.perform(get("/medicos/{medicoId}", 1))
        .andExpect(status().is3xxRedirection());
    }

    //Illegal Action
    @WithMockUser(username = "alvaroMedico", authorities = {"medico"})
    @Test
    void testShowNonExistingMedico() throws Exception{

    mockMvc.perform(get("/medicos/{medicoId}", 585))
        .andExpect(status().isOk())
        .andExpect(view().name("exception"));
    }

    @WithMockUser(username = "alvaroMedico", authorities = {"medico"})
    @Test
    void testProcessFindFormSuccess() throws Exception{
        mockMvc.perform(get("/medicos"))
        .andExpect(status().isOk())
        .andExpect(view().name("medicos/medicosList"))
        .andExpect(model().attributeExists("selections"));
    }

    @Test
    void testProcessFindFormNotLogged() throws Exception{
        mockMvc.perform(get("/medicos"))
        .andExpect(status().is3xxRedirection());
    }

}