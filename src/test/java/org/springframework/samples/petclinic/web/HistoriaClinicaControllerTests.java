
package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Paciente;
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
	private static final int			TEST_MEDICO_ID								= 1;
	private static final String			TEST_USER_ID								= "1";


	@BeforeEach
	void setup() {

		BDDMockito.given(this.pacienteService.findPacienteById(HistoriaClinicaControllerTests.TEST_PACIENTE_ID)).willReturn(Optional.of(new Paciente()));
		BDDMockito.given(this.historiaService.findHistoriaClinicaByPacienteId(HistoriaClinicaControllerTests.TEST_PACIENTE_ID)).willReturn(new HistoriaClinica());

	}

	@WithMockUser(value = "spring")
	@Test
	void testShowHistoriaClinica() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica", HistoriaClinicaControllerTests.TEST_PACIENTE_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica")).andExpect(MockMvcResultMatchers.view().name("pacientes/historiaClinicaDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerTests.TEST_PACIENTE_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica")).andExpect(MockMvcResultMatchers.model().attributeExists("paciente"))
			.andExpect(MockMvcResultMatchers.view().name(HistoriaClinicaControllerTests.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM));
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
	void testInitUpdateForm() throws Exception{
		
		Paciente paciente = new Paciente();
	    paciente.setId(HistoriaClinicaControllerTests.TEST_PACIENTE_ID);
	    BDDMockito.given(this.pacienteService.findHistoriaClinicaByPaciente(paciente)).willReturn(new HistoriaClinica());
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/edit", HistoriaClinicaControllerTests.TEST_PACIENTE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("paciente"))
		.andExpect(MockMvcResultMatchers.view().name(HistoriaClinicaControllerTests.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM));
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
