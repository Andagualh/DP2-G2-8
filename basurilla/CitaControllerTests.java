package org.springframework.samples.petclinic.web;

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
import org.junit.jupiter.api.BeforeEach;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.context.annotation.FilterType;


@WebMvcTest(value = CitaController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class CitaControllerTests{

    @Autowired
    private CitaController citaController;
    @MockBean
    private CitaService citaService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthoritiesService authoritiesService;
    @MockBean
    private MedicoService medicoService;
    @MockBean
    private PacienteService pacienteService;

    @Autowired
    private MockMvc mockMvc;
    
    private static final int TEST_MEDICO_ID = 1; 
    private static final int TEST_PACIENTE_ID = 1;
    private static final String TEST_USER_ID = "1";
    private static final int TEST_CITA_ID = 1;

    @BeforeEach
    void setup(){
        
        given(this.medicoService.getMedicoById(TEST_MEDICO_ID)).willReturn(new Medico());
        given(this.userService.findUserByUsername(TEST_USER_ID)).willReturn(Optional.of(new User()));
        given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(new Paciente()));
        given(this.citaService.findCitaById(TEST_CITA_ID)).willReturn(Optional.of(new Cita()));
    }

    @WithMockUser(value = "spring")
        @Test
    void testCrearCita() throws Exception{
        mockMvc.perform(get("/citas/new/{pacienteId}", TEST_PACIENTE_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("citas/createOrUpdateCitaForm"))
        .andExpect(model().attributeExists("cita"));
    }

    @WithMockUser(value = "spring")
        @Test
    void testSalvarCitaSuccess() throws Exception{
        mockMvc.perform(post("/citas/save")
            .with(csrf())
            .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
            .param("fecha","2020-08-08")
            .param("lugar","Seville"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/citas")
        );
    }
    
    @WithMockUser(value = "spring")
        @Test
    void testSalvarCitaHasErrors() throws Exception{
        mockMvc.perform(post("/citas/save")
        .with(csrf())
        .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
        .param("fecha", "2020/09/09")
        .param("lugar","Seville"))
        .andExpect(model().attributeHasErrors("cita"))
        .andExpect(status().isOk())
        .andExpect(view().name("citas/createOrUpdateCitaForm")
        );
    }
    
    @WithMockUser(value = "spring")
        @Test
    void testBorrarCitas() throws Exception{
        //El objeto sobre el que se realiza un Test si existe en este caso.
        mockMvc.perform(get("/citas/delete/{citaId}", TEST_CITA_ID))
        //.andExpect(model().attributeExists("message"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas")
        );
    }
    @WithMockUser(value = "spring")
        @Test
    void testBorrarCitaNoPresente() throws Exception{
        //No existe ningún objeto CITA con ID 2 cuando se ejecuta este Test.
        mockMvc.perform(get("/citas/delete/{citaId}", 2))
      //  .andExpect(model().attributeExists("message"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas")
        );
    }
    
    @WithMockUser(value = "spring")
        @Test
    void testInitFindForm() throws Exception{
        mockMvc.perform(
        get("/citas/find"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("cita"))
        .andExpect(view().name("citas/findCitas")
        );
    }
    
    @WithMockUser(value = "spring")
        @Test
    void testProcessFindFormSuccess() throws Exception{
        Cita dum1 = new Cita();
        Cita dum2 = new Cita();
        dum1.setFecha(LocalDate.of(2020, 8, 8));
        dum2.setFecha(LocalDate.of(2020, 8, 8));
       
        given(this.citaService.findCitasByFecha(LocalDate.of(2020,8,8))).willReturn(Lists.newArrayList(dum1, dum2));
        
        mockMvc.perform(
        get("/citas/porfecha")
        .param("fecha", "2020-08-08"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("selections"))
        .andExpect(view().name("citas/listCitas")
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testProcessFindFormNoCitaFound() throws Exception{
        mockMvc.perform(
            get("/citas/porfecha")
            .param("fecha", "2020-08-07"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("cita", "fecha"))
			.andExpect(model().attributeHasFieldErrorCode("cita", "fecha", "notFound"))
            .andExpect(view().name("citas/findCitas")
            );
    }    

    @WithMockUser(value = "spring")
        @Test
    void testInitList() throws Exception{
       given(this.userService.getCurrentMedico().getId())
       .willReturn(Integer.parseInt(TEST_USER_ID));
       
        mockMvc.perform(
            get("/citas"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/citas/" + TEST_USER_ID)
        );
    }    
}