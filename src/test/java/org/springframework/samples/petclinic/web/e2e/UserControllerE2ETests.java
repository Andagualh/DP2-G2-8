package org.springframework.samples.petclinic.web.e2e;

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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class UserControllerE2ETests {
    
    @Autowired
    private UserService userService;
    @Autowired
    private MedicoService medicoService;

    @Autowired
    private MockMvc mockMvc;

    
    @Test
    void testInitCreationForm() throws Exception{
        mockMvc.perform(get("/users/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("users/createMedicoForm"))
        .andExpect(model().attributeExists("medico"));
    }

    //TODO: Falla, averiguar porqué ¿Servicio medico quizá?
    
    @Test
    void testProcessCreationFormSuccess() throws Exception{
        mockMvc.perform(post("/users/new")
        .with(csrf())
        .param("nombre", "Medico")
        .param("apellidos", "DePruebas")
        .param("domicilio", "Domicilio, 1")
        .param("n_telefono", "645568848")
        .param("DNI", "44518952Z")
        .param("user.Username", "medicoPruebas")
        .param("user.Password", "entrar")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/"));
    }


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