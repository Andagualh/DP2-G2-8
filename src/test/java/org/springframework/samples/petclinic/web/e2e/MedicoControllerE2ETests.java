package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.User;
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

    @WithMockUser(value = "alvaroMedico", authorities = {"medico"})
        @Test
    void testInitFindForm() throws Exception{
        mockMvc.perform(get("/medicos/find"))
        .andExpect(status().isOk())
        .andExpect(view().name("medicos/findMedicos"))
        .andExpect(model().attributeExists("medico"));
    }

    @Test
    void testInitFindFormNotLogged() throws Exception{
        mockMvc.perform(get("/medicos/find"))
        .andExpect(status().is3xxRedirection());
    } 

    @WithMockUser(username = "alvaroMedico", authorities = {"medico"})
    @Test
    void testProcessFindFormSuccessMoreThanOneMedico() throws Exception{
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

    @WithMockUser(username = "alvaroMedico", authorities = {"medico"})
        @Test
    void testProcessFindFormPreciseApellido() throws Exception{
   //Surname Search
    mockMvc.perform(get("/medicos?apellidos={apellidos}", "Alferez"))
    .andExpect(status().is3xxRedirection())
    .andExpect(view().name("redirect:/medicos/" + 1));
    }

    @WithMockUser(value = "spring")
        @Test
    void testProcessFindFormPreciseApellidoManyResults() throws Exception{
   //Surname Search
    
    Medico medic = new Medico();
    medic.setNombre("Medico");
    medic.setApellidos("De Prueba");
    medic.setDomicilio("Domicilio");
    medic.setDNI("22645219V");
    medic.setN_telefono("645568848");
    User user = new User();
    user.setUsername("medicPru");
    user.setPassword("pass");
    user.setEnabled(true);
    medic.setUser(user);
   
    medicoService.saveMedico(medic);

    Medico medic2 = new Medico();
    medic2.setNombre("Nombre");
    medic2.setApellidos("De Prueba");
    medic2.setDomicilio("Domicilio");
    medic2.setDNI("98279809G");
    medic2.setN_telefono("645568848");
    User user2 = new User();
    user2.setUsername("medicPru2");
    user2.setPassword("pass");
    user2.setEnabled(false);
    medic2.setUser(user2);
    medicoService.saveMedico(medic2);
    

    mockMvc.perform(get("/medicos?apellidos={apellidos}", medic.getApellidos()))
        .andExpect(status().isOk())
        .andExpect(view().name("medicos/medicosList"))
        .andExpect(model().attributeExists("selections"));
    
    
    }

    @WithMockUser(username = "alvaroMedico", authorities = {"medico"})
        @Test
    void testProcessFindFormPreciseApellidoEmptyResults() throws Exception{
   
    mockMvc.perform(get("/medicos?apellidos={apellidos}", "apellidosProbando"))
        .andExpect(status().isOk())
        .andExpect(view().name("medicos/findMedicos"));
    }

    @Test
    void testProcessFindFormPreciseApellidoNotLogged() throws Exception{
   
    mockMvc.perform(get("/medicos?apellidos={apellidos}", "apellidosProbando"))
        .andExpect(status().is3xxRedirection());
    }




}