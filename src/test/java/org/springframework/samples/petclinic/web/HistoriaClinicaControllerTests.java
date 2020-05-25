
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = HistoriaClinicaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

class HistoriaClinicaControllerTests {

	private static final String			VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM	= "pacientes/createOrUpdateHistoriaClinicaForm";

	@Autowired
	private HistoriaClinicaController	historiaController;
	@MockBean
	private HistoriaClinicaService		historiaService;
	@MockBean
	private PacienteService				pacienteService;
	@MockBean
	private MedicoService				medicoService;
	@MockBean
	private UserService					userService;
	@MockBean
	private AuthoritiesService			authoritiesService;

	@Autowired
	private MockMvc						mockMvc;

	private static final int			TEST_HC_ID									= 1;
	private static final int			TEST_PACIENTE_ID							= 1;
	private static final int			TEST_PACIENTE2_ID							= 2;
	private static final int			TEST_MEDICO_ID								= 1;
	private static final String 		TEST_MEDICOUSER_ID = "medico1";
	private static final int			TEST_MEDICO2_ID								= 2;
	private static final String 		TEST_MEDICOUSER2_ID = "medico2";
	
	private Medico 						medico;
	private User 						medicoUser;
	private Medico 						medico2;
	private User 						medico2User;
	private Authorities					authorities;
	private Paciente					javier;
	private Paciente					adolfo;


	@BeforeEach
	void setup() {
		
		this.medico = new Medico();
		this.medico.setId(TEST_MEDICO_ID);
		this.medico.setNombre("Medico");
		this.medico.setApellidos("Apellidos");
		this.medico.setDNI("12345678Z");
		this.medico.setN_telefono("123456789");
		this.medico.setDomicilio("Domicilio");
		
		this.medicoUser = new User();
		this.medicoUser.setUsername(TEST_MEDICOUSER2_ID);
		this.medicoUser.setPassword("medico1");
		this.medicoUser.setEnabled(true);
		
		this.medico.setUser(this.medicoUser);
		this.medico.getUser().setEnabled(true);
		
		this.authorities = new Authorities();
		this.authorities.setUsername(TEST_MEDICOUSER2_ID);
		this.authorities.setAuthority("medico");
		
		
		this.medico2 = new Medico();
		this.medico2.setId(TEST_MEDICO2_ID);
		this.medico2.setNombre("Medico2");
		this.medico2.setApellidos("Apellidos2");
		this.medico2.setDNI("53279183M");
		this.medico2.setN_telefono("123456789");
		this.medico2.setDomicilio("Domicilio");
		
		this.medico2User = new User();
		this.medico2User.setUsername(TEST_MEDICOUSER2_ID);
		this.medico2User.setPassword("medico2");
		this.medico2User.setEnabled(true);
		
		this.medico2.setUser(this.medicoUser);
		this.medico2.getUser().setEnabled(true);
		
		this.authorities = new Authorities();
		this.authorities.setUsername(TEST_MEDICOUSER2_ID);
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
		this.javier.setMedico(this.medico);
		
		this.adolfo = new Paciente();
		this.adolfo.setId(TEST_PACIENTE2_ID);
		this.adolfo.setNombre("Adolfo");
		this.adolfo.setApellidos("Perez");
		this.adolfo.setF_nacimiento(LocalDate.of(1992, 6, 24));
		this.adolfo.setDNI("12345678Z");
		this.adolfo.setDomicilio("El cuervo de Sevilla");
		this.adolfo.setN_telefono(644789302);
		this.adolfo.setEmail("adolfo_perez@gmail.com");
		this.adolfo.setF_alta(LocalDate.now());
		this.adolfo.setMedico(this.medico2);
		
		BDDMockito.given(this.pacienteService.findPacienteById(HistoriaClinicaControllerTests.TEST_PACIENTE_ID)).willReturn(Optional.of(new Paciente()));
		BDDMockito.given(this.historiaService.findHistoriaClinicaByPacienteId(HistoriaClinicaControllerTests.TEST_PACIENTE_ID)).willReturn(new HistoriaClinica());
		BDDMockito.given(this.userService.getCurrentMedico()).willReturn(this.medico);
		
		
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowHistoriaClinica() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica", HistoriaClinicaControllerTests.TEST_PACIENTE_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica")).andExpect(MockMvcResultMatchers.view().name("pacientes/historiaClinicaDetails"));
	}
	

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationFormAuthorized() throws Exception{
	    
		
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(this.javier));
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/new", TEST_PACIENTE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("paciente"))
		.andExpect(MockMvcResultMatchers.view().name(VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationFormNotAuthorized() throws Exception{
		
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(this.adolfo));
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/new", TEST_PACIENTE_ID))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes"));
	}
	

	@WithMockUser(value = "spring")
	@Test
	void testCrearHistoriaClinica() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerTests.TEST_PACIENTE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", "Historia clinica paciente X"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/{pacienteId}"));
	}

	//Caso en el que el paciente tiene una historia clinica ya creada
	@WithMockUser(value = "spring")
	@Test
	void testCrearHistoriaClinicaFailure() throws Exception {

		Paciente paciente = new Paciente();
		paciente.setId(HistoriaClinicaControllerTests.TEST_PACIENTE_ID);
		BDDMockito.given(this.pacienteService.findPacienteById(HistoriaClinicaControllerTests.TEST_PACIENTE_ID)).willReturn(Optional.of(paciente));
		HistoriaClinica hc = new HistoriaClinica();
		hc.setPaciente(paciente);
		BDDMockito.given(this.pacienteService.findHistoriaClinicaByPaciente(paciente)).willReturn(hc);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerTests.TEST_PACIENTE_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("descripcion", "historia clinica oups")
			.param("paciente.id", Integer.toString(HistoriaClinicaControllerTests.TEST_PACIENTE_ID)))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testCrearHistoriaClinicaHasErrors() throws Exception {
		Paciente paciente = new Paciente();
	    paciente.setId(HistoriaClinicaControllerTests.TEST_PACIENTE_ID);
	    BDDMockito.given(this.pacienteService.findHistoriaClinicaByPaciente(paciente)).willReturn(new HistoriaClinica());
	    
	    this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/new", TEST_PACIENTE_ID)
	    		.with(SecurityMockMvcRequestPostProcessors.csrf())
	    		.param("descripcion", "Nueva descripción para la historia clinica")
	    		.param("paciente.id", "?"))
	    		.andExpect(MockMvcResultMatchers.model().hasErrors())
	    		.andExpect(MockMvcResultMatchers.status().isOk())
	    		.andExpect(MockMvcResultMatchers.view().name(VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormAuthorized() throws Exception{
	    
		HistoriaClinica hc = new HistoriaClinica();
		hc.setDescripcion("Descripcion generica");
		hc.setId(TEST_HC_ID);
		hc.setPaciente(this.javier);
		
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(this.javier));
		BDDMockito.given(this.pacienteService.findHistoriaClinicaByPaciente(this.javier)).willReturn(hc);
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/edit", TEST_PACIENTE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("paciente"))
		.andExpect(MockMvcResultMatchers.view().name(VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormNotAuthorized() throws Exception{
	    
		HistoriaClinica hc = new HistoriaClinica();
		hc.setDescripcion("Descripcion generica");
		hc.setId(TEST_HC_ID);
		hc.setPaciente(this.adolfo);
		
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(this.adolfo));
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/edit", TEST_PACIENTE_ID))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception{
		Paciente paciente = new Paciente();
	    paciente.setId(HistoriaClinicaControllerTests.TEST_PACIENTE_ID);
	    BDDMockito.given(this.pacienteService.findHistoriaClinicaByPaciente(paciente)).willReturn(new HistoriaClinica());
	    
	    this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/edit", TEST_PACIENTE_ID)
	    		.with(SecurityMockMvcRequestPostProcessors.csrf())
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
				.param("descripcion", "Nueva descripción para la historia clinica"))
	    		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	    		.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/{pacienteId}"));
	}
	
	//Intento de actualizar historia clinica con errores en el result
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception{
		Paciente paciente = new Paciente();
	    paciente.setId(HistoriaClinicaControllerTests.TEST_PACIENTE_ID);
	    BDDMockito.given(this.pacienteService.findHistoriaClinicaByPaciente(paciente)).willReturn(new HistoriaClinica());
	    
	    this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/edit", TEST_PACIENTE_ID)
	    		.with(SecurityMockMvcRequestPostProcessors.csrf())
	    		.param("descripcion", "Nueva descripción para la historia clinica"))
	    		.andExpect(MockMvcResultMatchers.model().hasErrors())
	    		.andExpect(MockMvcResultMatchers.status().isOk())
	    		.andExpect(MockMvcResultMatchers.view().name(VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM));
	}
	

}
