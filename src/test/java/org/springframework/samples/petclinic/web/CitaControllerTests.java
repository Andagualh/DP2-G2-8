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

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

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
    private static final String TEST_MEDICOUSER_ID = "medico1";
    
    private Paciente            javier;

    private Medico                medico1;

    private User                medico1User;

    private Authorities            authorities;

    @BeforeEach
    void setup(){
        this.medico1 = new Medico();
        this.medico1.setId(TEST_MEDICO_ID);
        this.medico1.setNombre("Medico 2");
        this.medico1.setApellidos("Apellidos");
        this.medico1.setDNI("12345678Z");
        this.medico1.setN_telefono("123456789");
        this.medico1.setDomicilio("Domicilio");
        
        this.medico1User = new User();
        this.medico1User.setUsername(TEST_MEDICOUSER_ID);
        this.medico1User.setPassword("medico1");
        this.medico1User.setEnabled(true);
        
        this.medico1.setUser(this.medico1User);
        this.medico1.getUser().setEnabled(true);
        
        this.authorities = new Authorities();
        this.authorities.setUsername(TEST_MEDICOUSER_ID);
        this.authorities.setAuthority("medico");

        this.javier = new Paciente();
        this.javier.setId(TEST_PACIENTE_ID);
        this.javier.setNombre("Javier");
        this.javier.setApellidos("Silva");
        this.javier.setF_nacimiento(LocalDate.of(1997, 6, 8));
        this.javier.setDNI("12345678Z");
        this.javier.setDomicilio("Ecija");
        this.javier.setN_telefono(612345987);
        this.javier.setEmail("javier_silva@gmail.com");
        this.javier.setF_alta(LocalDate.now());
        this.javier.setMedico(this.medico1);
        
        given(this.medicoService.getMedicoById(TEST_MEDICO_ID)).willReturn(this.medico1);
        given(this.userService.findUserByUsername(TEST_USER_ID)).willReturn(Optional.of(this.medico1User));
        given(this.pacienteService.findPacienteById(BDDMockito.anyInt())).willReturn(Optional.of(this.javier));
        given(this.citaService.findCitaById(TEST_CITA_ID)).willReturn(Optional.of(new Cita()));
    }

    @WithMockUser(value = "spring")
        @Test
    void testCrearCita() throws Exception{
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        mockMvc.perform(get("/citas/new/{pacienteId}", TEST_PACIENTE_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("citas/createOrUpdateCitaForm"))
        .andExpect(model().attributeExists("cita"));
    }

    @WithMockUser(value = "spring")
        @Test
    void testCrearCitaForPacienteNotBelongMedico() throws Exception{
        Medico medico2 = new Medico();
        medico2.setId(2);
        medico2.setNombre("Medico 3");
        medico2.setApellidos("Apellidos");
        medico2.setDNI("12345678Z");
        medico2.setN_telefono("123456789");
        medico2.setDomicilio("Domicilio");
        
        User medico2User = new User();
        medico2User.setUsername("medico2");
        medico2User.setPassword("medico2");
        medico2User.setEnabled(true);

        given(this.userService.getCurrentMedico()).willReturn(medico2);


        mockMvc.perform(get("/citas/new/{pacienteId}", TEST_PACIENTE_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("accessNotAuthorized"));
    }

    @WithMockUser(value = "spring")
        @Test
    void testSalvarCitaSuccess() throws Exception{
        mockMvc.perform(post("/citas/save")
            .with(csrf())
            .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
            .param("paciente.nombre", "test")
            .param("paciente.apellidos", "test")
            .param("paciente.f_nacimiento", "1997/09/09")
            .param("paciente.f_alta", "2020/08/08")
            .param("paciente.DNI", "12345689Q")
            .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
            .param("paciente.medico.nombre", "test")
            .param("paciente.medico.apellidos", "test")
            .param("paciente.medico.domicilio", "test")
            .param("paciente.medico.user.username", "test")
            .param("paciente.medico.user.password", "test")
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
        .param("paciente.nombre", "test")
        .param("paciente.apellidos", "test")
        .param("paciente.f_nacimiento", "1997/09/09")
        .param("paciente.f_alta", "2020/08/08")
        .param("paciente.DNI", "12345689Q")
        .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
        .param("paciente.medico.nombre", "test")
        .param("paciente.medico.apellidos", "test")
        .param("paciente.medico.domicilio", "test")
        .param("paciente.medico.user.username", "test")
        .param("paciente.medico.user.password", "test")
        .param("fecha", "2020-09-09")
        .param("lugar",""))
        .andExpect(model().attributeHasErrors("cita"))
        .andExpect(status().isOk())
        .andExpect(view().name("citas/createOrUpdateCitaForm")
        );
    }

    //Este caso ocurre cuando un Paciente ya tiene una cita el mismo día

    @WithMockUser(value = "spring")
        @Test
    void testSalvarCitaOnAlreadyExistingDate() throws Exception{
        given(this.citaService.existsCitaPacienteDate(BDDMockito.any(LocalDate.class), BDDMockito.any(Paciente.class)))
        .willReturn(true);

        mockMvc.perform(post("/citas/save")
        .with(csrf())
        .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
        .param("paciente.nombre", "test")
        .param("paciente.apellidos", "test")
        .param("paciente.f_nacimiento", "1997/09/09")
        .param("paciente.f_alta", "2020/08/08")
        .param("paciente.DNI", "12345689Q")
        .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
        .param("paciente.medico.nombre", "test")
        .param("paciente.medico.apellidos", "test")
        .param("paciente.medico.domicilio", "test")
        .param("paciente.medico.user.username", "test")
        .param("paciente.medico.user.password", "test")
        .param("fecha", "2020-09-09")
        .param("lugar","Sevilla"))
        .andExpect(status().isOk())
        .andExpect(view().name("citas/createOrUpdateCitaForm")
        );
    }
    
    @WithMockUser(value = "spring")
	    @Test
	void testSalvarCitaPastDate() throws Exception{
	    mockMvc.perform(post("/citas/save")
	    .with(csrf())
        .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
        .param("paciente.nombre", "test")
        .param("paciente.apellidos", "test")
        .param("paciente.f_nacimiento", "1997/09/09")
        .param("paciente.f_alta", "2020/08/08")
        .param("paciente.DNI", "12345689Q")
        .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
        .param("paciente.medico.nombre", "test")
        .param("paciente.medico.apellidos", "test")
        .param("paciente.medico.domicilio", "test")
        .param("paciente.medico.user.username", "test")
        .param("paciente.medico.user.password", "test")
	    .param("fecha", "2019-01-01")
	    .param("lugar","Seville"))
	    .andExpect(model().attribute("message", "La fecha debe estar en presente o futuro"))
	    .andExpect(status().isOk())
	    .andExpect(view().name("citas/createOrUpdateCitaForm")
	    );
	}
    	    
    @WithMockUser(value = "spring")
	    @Test
	void testSalvarCitaHasErrorsCoverage() throws Exception{
	    mockMvc.perform(post("/citas/save")
	    .with(csrf())
        .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
        .param("paciente.nombre", "test")
        .param("paciente.apellidos", "test")
        .param("paciente.f_nacimiento", "1997/09/09")
        .param("paciente.f_alta", "2020/08/08")
        .param("paciente.DNI", "12345689Q")
        .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
        .param("paciente.medico.nombre", "test")
        .param("paciente.medico.apellidos", "test")
        .param("paciente.medico.domicilio", "test")
        .param("paciente.medico.user.username", "test")
        .param("paciente.medico.user.password", "test")
	    .param("fecha", "2019-01-01")
	    .param("lugar",""))
	    .andExpect(model().attributeHasErrors("cita"))
	    .andExpect(model().attributeHasFieldErrors("cita", "lugar"))
	    .andExpect(status().isOk())
	    .andExpect(view().name("citas/createOrUpdateCitaForm")
	    );
	}
    
    @WithMockUser(value = "spring")
        @Test
    void testBorrarCitas() throws Exception{
        Cita citaTest = new Cita();
        citaTest.setFecha(LocalDate.now().plusDays(1));
        citaTest.setLugar("LUGAR");
        citaTest.setId(90);
        citaTest.setPaciente(this.javier);
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        given(this.citaService.findCitaById(BDDMockito.anyInt())).willReturn(Optional.of(citaTest));

        mockMvc.perform(get("/citas/delete/{citaId}", TEST_CITA_ID))
        //.andExpect(model().attributeExists("message"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas")
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testBorrarCitasPacienteDifferentMedico() throws Exception{
        Medico medic = new Medico();
        medic.setId(2);
        given(this.userService.getCurrentMedico()).willReturn(medic);
        Cita citaTest = new Cita();
        citaTest.setFecha(LocalDate.now().plusDays(1));
        citaTest.setLugar("LUGAR");
        citaTest.setId(90);
        citaTest.setPaciente(this.javier);
        given(this.citaService.findCitaById(BDDMockito.anyInt())).willReturn(Optional.of(citaTest));

        
        mockMvc.perform(get("/citas/delete/{citaId}", 90))
        //.andExpect(model().attributeExists("message"))
        .andExpect(status().isOk())
        .andExpect(view().name("accessNotAuthorized")
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testBorrarCitaNoPresente() throws Exception{
        
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        given(this.citaService.findCitaById(BDDMockito.anyInt())).willReturn(Optional.empty());

        mockMvc.perform(get("/citas/delete/{citaId}", 9999))
      //  .andExpect(model().attributeExists("message"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas")
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testBorrarCitaWithFechaNull() throws Exception{

        Cita cita = new Cita();
        cita.setLugar("Lugar");
        cita.setPaciente(this.javier);
        cita.setFecha(null);
        cita.setId(9090);
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        given(this.citaService.findCitaById(BDDMockito.anyInt())).willReturn(Optional.of(cita));

        mockMvc.perform(get("/citas/delete/{citaId}", 9090))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas")
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testBorrarCitaFechaPresente() throws Exception{
        Cita cita = new Cita();
        cita.setLugar("Lugar");
        cita.setPaciente(this.javier);
        cita.setFecha(LocalDate.now());
        cita.setId(9090);
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        given(this.citaService.findCitaById(BDDMockito.anyInt())).willReturn(Optional.of(cita));

        mockMvc.perform(get("/citas/delete/{citaId}", 9090))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas"));
    }

    @WithMockUser(value = "spring")
        @Test
    void testBorrarCitaFechaPasado() throws Exception{
        Cita cita = new Cita();
        cita.setLugar("Lugar");
        cita.setPaciente(this.javier);
        cita.setFecha(LocalDate.now().minusDays(1));
        cita.setId(9090);
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        given(this.citaService.findCitaById(BDDMockito.anyInt())).willReturn(Optional.of(cita));

        mockMvc.perform(get("/citas/delete/{citaId}", 9090))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas"));
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
        dum1.setFecha(LocalDate.of(2020, 8, 8));
        dum1.setPaciente(this.javier);
        
        given(this.citaService.findCitasByFecha(LocalDate.of(2020,8,8), this.medico1)).willReturn(Lists.newArrayList(dum1));
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        
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
	void testProcessFindFormNoDate() throws Exception{
	    Cita dum1 = new Cita();
	    
	    dum1.setFecha(LocalDate.now());
	    
	   
	    given(this.citaService.findCitasByFecha(LocalDate.now(), this.medico1)).willReturn(Lists.newArrayList(dum1));
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        
	    mockMvc.perform(
	    get("/citas/porfecha")
	    .param("fecha", "null"))
	    .andExpect(status().isOk())
	    .andExpect(model().attributeExists("selections"))
	    .andExpect(view().name("citas/listCitas")
	    );
	}
    
    @WithMockUser(value = "spring")
        @Test
    void testProcessFindFormNoCitaFound() throws Exception{
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);

        mockMvc.perform(
            get("/citas/porfecha")
            .param("fecha", "2020-08-07"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("cita", "fecha"))
			.andExpect(model().attributeHasFieldErrorCode("cita", "fecha", "error.citaNotFound"))
            .andExpect(view().name("citas/findCitas")
            );
    }    

    @WithMockUser(value = "spring")
        @Test
    void testInitList() throws Exception{
        Medico medic = new Medico();
        medic.setId(TEST_MEDICO_ID);
        given(this.userService.getCurrentMedico()).willReturn(medic);
        
       
        mockMvc.perform(
            get("/citas"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/citas/" + TEST_USER_ID)
        );
    }
    
    @WithMockUser(value = "spring")
        @Test
    void listadoCitasSuccess() throws Exception{
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        given(citaService.findCitasByMedicoId(TEST_MEDICO_ID)).willReturn(Lists.newArrayList(new Cita(), new Cita()));
        
        mockMvc.perform(
            get("/citas/{medicoId}", TEST_MEDICO_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("selections"))
            .andExpect(view().name("citas/listCitas")
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void listadoCitasOfOtherMedico() throws Exception{
        Medico medic = new Medico();
        medic.setId(2);
        given(this.userService.getCurrentMedico()).willReturn(medic);
        
        mockMvc.perform(
            get("/citas/{medicoId}", TEST_MEDICO_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("accessNotAuthorized")
        );
    }
    
    @WithMockUser(value = "spring")
        @Test
    void listadoCitasIsEmpty() throws Exception{
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        given(citaService.findCitasByMedicoId(TEST_MEDICO_ID)).willReturn(Lists.newArrayList());

        mockMvc.perform(
            get("/citas/{medicoId}", TEST_MEDICO_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/")
        );
    }
    
}