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
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import org.springframework.context.annotation.FilterType;

@WebMvcTest(value = UserController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class UserControllerTests {
    
    @Autowired
    private UserController userController;
    @MockBean
    private MedicoService medicoService;
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(value = "spring")
        @Test
    void testInitCreationForm() throws Exception{
        mockMvc.perform(get("/users/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("users/createMedicoForm"))
        .andExpect(model().attributeExists("medico"));
    }
    
    @WithMockUser(value = "spring")
        @Test
    void testProcessCreationFormSuccess() throws Exception{
        mockMvc.perform(post("/users/new")
        .with(csrf())
        .param("nombre", "Medico")
        .param("apellidos", "DePruebas")
        .param("domicilio", "Domicilio, 1")
        .param("n_telefono", "645568848")
        .param("DNI", "22645219V")
        .param("user.Username", "medicoPruebas")
        .param("user.Password", "contrasenaPruebas")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/"));
    }

    @WithMockUser(value = "spring")
        @Test
    void testProcessCreationFormHasErrors() throws Exception{
        mockMvc.perform(post("/users/new")
        .with(csrf())
        .param("nombre", "Medico")
        .param("apellidos", "DePruebas")
        .param("domicilio", "Domicilio, 1")
        .param("n_telefono", "645568848")
        .param("DNI", "")
        .param("user.Username", "medicoPruebas")
        .param("user.Password", "")
        )
        .andExpect(status().isOk())
        .andExpect(view().name("users/createMedicoForm"))
        .andExpect(model().attributeExists("medico"));
    }

    
}