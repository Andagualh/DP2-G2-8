package org.springframework.samples.petclinic.web;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(value=HistoriaClinicaController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)

class HistoriaClinicaControllerTests {

	private static final String				VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM	= "pacientes/createOrUpdateHistoriaClinicaForm";
	
	@Autowired
	private HistoriaClinicaController historiaController;
    @MockBean
    private HistoriaClinicaService historiaService;
    @MockBean
    private PacienteService pacienteService;
    @MockBean
    private MedicoService medicoService;    
    @MockBean
    private UserService userService;	
    @MockBean
    private AuthoritiesService authoritiesService;
    
    @Autowired
	private MockMvc mockMvc;
    
    private static final int TEST_HC_ID = 1;
	private static final int TEST_PACIENTE_ID = 1;
	private static final int TEST_MEDICO_ID = 1;
	private static final String TEST_USER_ID = "1";
	
	@BeforeEach
	void setup() {
		
		given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID))
			.willReturn(Optional.of(new Paciente()));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowHistoriaClinica() throws Exception{
		mockMvc.perform(get("/pacientes/{pacienteId}/historiaclinica", TEST_PACIENTE_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("pacientes/historiaClinicaDetails"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception{
		mockMvc.perform(get("/pacientes/{pacienteId}/historiaclinica/new", TEST_PACIENTE_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("historiaclinica"))
				.andExpect(model().attributeExists("paciente"))
				.andExpect(view().name(VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testCrearHistoriaClinicaHasErrors() throws Exception{
		mockMvc.perform(post("/pacientes/{pacienteId}/historiaclinica/new", TEST_PACIENTE_ID)
				.with(csrf())
				.param("descripcion", "Historia clinica paciente X"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM));
	}
	
	//Caso en el que el paciente tiene una historia clinica ya creada
	@WithMockUser(value = "spring")
	@Test
	void testCrearHistoriaClinicaFailure() throws Exception{
		
		Paciente pac = this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		
		HistoriaClinica hC = new HistoriaClinica();
		
		hC.setPaciente(pac);
		
		mockMvc.perform(post("/pacientes/{pacienteId}/historiaclinica/new", TEST_PACIENTE_ID))
				.andExpect(view().name("redirect:/oups"));		
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testCrearHistoriaClinica() throws Exception{
		mockMvc.perform(get("/pacientes/{pacienteId}/historiaclinica/new", TEST_PACIENTE_ID)
				.with(csrf())
				.param("descripcion", "Historia clinica paciente X")
				.param("paciente.id", Integer.toString(TEST_PACIENTE_ID)))
				.andExpect(status().isOk())
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/pacientes/{pacienteId}"));
	}
	
	    
}
