package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.junit.jupiter.api.BeforeEach;
import org.apache.tomcat.jni.Local;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import org.springframework.context.annotation.FilterType;

@WebMvcTest(value = MedicoController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class MedicoControllerTests {
    
    @Autowired
    private MedicoController medicoController;
    @MockBean
    private MedicoService medicoService;
    
    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(value = "spring")
        @Test
    void testShowMedico() throws Exception{
    
    given(this.medicoService.getMedicoById(BDDMockito.anyInt())).willReturn(new Medico());
    
    mockMvc.perform(get("/medicos/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("medicos/medicoDetails"))
        .andExpect(model().attributeExists("medico"));
    }
    
    /*@WithMockUser(value = "spring")
        @Test
    void testInitFindForm() throws Exception{
        mockMvc.perform(get("/medicos/find"))
        .andExpect(status().isOk())
        .andExpect(view().name("medicos/findMedicos"))
        .andExpect(model().attributeExists("medico"));
    }*/

    @WithMockUser(value = "spring")
        @Test
    void testProcessFindFormSuccess() throws Exception{
       
        Collection<Medico> res = new ArrayList<>();
        Medico medic = new Medico();
        medic.setNombre("Medico");
        medic.setApellidos("De Prueba");
        medic.setDomicilio("Domicilio");
        medic.setDNI("22645219V");
        medic.setN_telefono("645568848");
        medic.setUser(new User());

        Medico medic2 = new Medico();
        medic2.setNombre("Medico");
        medic2.setApellidos("De Prueba");
        medic2.setDomicilio("Domicilio");
        medic2.setDNI("22645219V");
        medic2.setN_telefono("645568848");
        medic2.setUser(new User());

        res.add(medic);
        res.add(medic2);


       given(this.medicoService.getMedicos()).willReturn(res);

        mockMvc.perform(get("/medicos"))
        .andExpect(status().isOk())
        .andExpect(view().name("medicos/medicosList"))
        .andExpect(model().attributeExists("selections"));
    }

}