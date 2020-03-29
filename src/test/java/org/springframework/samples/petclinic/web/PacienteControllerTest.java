
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
import java.util.Optional;

import org.assertj.core.util.Lists;
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
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PacienteController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PacienteControllerTest {

	private static final int  TEST_PACIENTE_ID = 1;
	private static final int  TEST_MEDICO_ID = 1;
	private static final String TEST_MEDICOUSER_ID = "medico1";
	private static final String TEST_AUTHORITIES_ID = TEST_MEDICOUSER_ID;
	
	@Autowired
	private PacienteController	pacienteController;

	@MockBean
	private PacienteService		pacienteService;

	@MockBean
	private MedicoService		medicoService;

	@MockBean
	private UserService			userService;

	@MockBean
	private AuthoritiesService	authoritiesService;

	@Autowired
	private MockMvc				mockMvc;

	private Paciente			javier;

	private Medico				medico1;

	private User				medico1User;

	private Authorities			authorities;


	@BeforeEach
	void setup() {
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
	
		BDDMockito.given(this.userService.findUserByUsername(TEST_MEDICOUSER_ID)).willReturn(Optional.of(this.medico1User));
		BDDMockito.given(this.medicoService.getMedicoById(TEST_MEDICO_ID)).willReturn(this.medico1);
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(this.javier));
	}
	
		@WithMockUser(value = "spring")
	@Test
	void testShowPaciente() throws Exception {
		mockMvc.perform(get("/pacientes/{pacienteId}", TEST_PACIENTE_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("paciente"))
				.andExpect(model().attribute("paciente", hasProperty("nombre", is("Javier"))))
				.andExpect(model().attribute("paciente", hasProperty("apellidos", is("Silva"))))
				.andExpect(model().attribute("paciente", hasProperty("f_nacimiento", is(LocalDate.of(1997, 6, 8)))))
				.andExpect(model().attribute("paciente", hasProperty("DNI", is("12345678Z"))))
				.andExpect(model().attribute("paciente", hasProperty("domicilio", is("Ecija"))))
				.andExpect(model().attribute("paciente", hasProperty("n_telefono", is(612345987))))
				.andExpect(model().attribute("paciente", hasProperty("email", is("javier_silva@gmail.com"))))
				.andExpect(model().attribute("paciente", hasProperty("f_alta", is(LocalDate.now()))))
				.andExpect(model().attribute("paciente", hasProperty("medico", is(this.medico1))))
				.andExpect(view().name("pacientes/pacienteDetails"));
	}
	
		@WithMockUser(value = "spring")
	@Test
	void testInitFindForm() throws Exception {
		mockMvc.perform(get("/pacientes")).andExpect(status().isOk())
				.andExpect(model().attributeExists("paciente"))
				.andExpect(model().attribute("paciente", hasProperty("apellidos", is(""))))
				.andExpect(view().name("pacientes/findPacientes"));
	}
		
		@WithMockUser(value = "spring")
	@Test
	void testProcessFindForm() throws Exception {
		mockMvc.perform(get("/pacientes")).andExpect(status().isOk())
				.andExpect(model().attributeExists("paciente"))
				.andExpect(model().attribute("paciente", hasProperty("apellidos", is(""))))
				.andExpect(view().name("pacientes/findPacientes"));
	}
				
		@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormFindOne() throws Exception {
			BDDMockito.given(this.pacienteService.findPacienteByApellidos(javier.getApellidos()))
				.willReturn(Lists.newArrayList(javier));

			mockMvc.perform(get("/pacientes").param("apellidos", "Silva"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/pacientes/" + TEST_PACIENTE_ID));
	}
	
        @WithMockUser(value = "spring")
	@Test
	void testProcessFindFormNoPacientesFound() throws Exception {
		mockMvc.perform(get("/pacientes").param("apellidos", "Unknown Surname")).andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("paciente", "apellidos"))
				.andExpect(model().attributeHasFieldErrorCode("paciente", "apellidos", "notFound"))
				.andExpect(view().name("pacientes/findPacientes"));
	}
       
//		@WithMockUser(value = "spring")
//	@Test
//	void testInitFindMedForm() throws Exception {
//		mockMvc.perform(get("/pacientes/findByMedico"))
//				.andExpect(status().isOk()).andExpect(model().attributeExists("medicoId"))
//				.andExpect(view().name("redirect:/pacientes/findByMedico/" + TEST_MEDICO_ID));
//	}
        
        
        
        
        
        
        
        
        
		
    	@WithMockUser(value = "spring")
	@Test
	void testInitUpdatePacientesForm() throws Exception {
		mockMvc.perform(get("/pacientes/{pacienteId}/edit", TEST_PACIENTE_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("paciente"))
				.andExpect(model().attributeExists("medicoList"))
				.andExpect(model().attribute("paciente", hasProperty("nombre", is("Javier"))))
				.andExpect(model().attribute("paciente", hasProperty("apellidos", is("Silva"))))
				.andExpect(model().attribute("paciente", hasProperty("f_nacimiento", is(LocalDate.of(1997, 6, 8)))))
				.andExpect(model().attribute("paciente", hasProperty("DNI", is("12345678Z"))))
				.andExpect(model().attribute("paciente", hasProperty("domicilio", is("Ecija"))))
				.andExpect(model().attribute("paciente", hasProperty("n_telefono", is(612345987))))
				.andExpect(model().attribute("paciente", hasProperty("email", is("javier_silva@gmail.com"))))
				.andExpect(model().attribute("paciente", hasProperty("f_alta", is(LocalDate.now()))))
				.andExpect(model().attribute("paciente", hasProperty("medico", is(this.medico1))))
				.andExpect(view().name("pacientes/createOrUpdatePacientesForm"));
	}
    	
        @WithMockUser(value = "spring")
	@Test
	void testProcessUpdatePacienteFormSuccess() throws Exception {
    	Medico medic = new Medico();
    	medic.setId(PacienteControllerTest.TEST_MEDICO_ID);
    	BDDMockito.given(this.userService.getCurrentMedico()).willReturn(medic);
        
    	mockMvc.perform(post("/pacientes/{pacienteId}/edit", TEST_PACIENTE_ID)
							.with(csrf())
							.param("nombre", "Javier")
							.param("apellidos", "Silva")
							.param("f_nacimiento", "1997/06/08")
							.param("DNI", "12345678Z")
							.param("domicilio", "Ecija")
							.param("n_telefono", "612345987")
							.param("email", "javier_silva@gmail.com")
							.param("f_alta", "2020/03/25")
							.param("medico.id", Integer.toString(TEST_MEDICO_ID))
							.param("medico.nombre", "Medico 2")
							.param("medico.apellidos", "Apellidos")
							.param("medico.DNI", "12345678Z")
							.param("medico.n_telefono", "123456789")
							.param("medico.domicilio", "Domicilio")
							.param("medico.user.username", TEST_MEDICOUSER_ID)
							.param("medico.user.password", "medico1")
							.param("medico.user.enabled", "true"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/pacientes/{pacienteId}"));
	}
    

		@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
	    	BDDMockito.given(this.userService.getCurrentMedico()).willReturn(this.medico1);
			
			mockMvc.perform(post("/pacientes/{pacienteId}/edit", TEST_PACIENTE_ID)
							.with(csrf())
							.param("nombre", "Javier")
							.param("apellidos", "Silva")
							.param("f_nacimiento", "1997/06/08")
							.param("DNI", "12345678Z")
							.param("domicilio", "")
							.param("n_telefono", "")
							.param("email", "")
							.param("f_alta", "2020/03/25")
							.param("medico.id", Integer.toString(TEST_MEDICO_ID))
							.param("medico.nombre", "Medico 2")
							.param("medico.apellidos", "Apellidos")
							.param("medico.DNI", "12345678Z")
							.param("medico.n_telefono", "123456789")
							.param("medico.domicilio", "Domicilio")
							.param("medico.user.username", TEST_MEDICOUSER_ID)
							.param("medico.user.password", "medico1")
							.param("medico.user.enabled", "true"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("paciente"))
				.andExpect(model().attributeHasFieldErrors("paciente", "domicilio"))
				.andExpect(view().name("pacientes/createOrUpdatePacientesForm"));
	}
		
        @WithMockUser(value = "spring")
	@Test
	void testBorrarPaciente() throws Exception {
	    BDDMockito.given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        	
        mockMvc.perform(post("/pacientes/{pacienteId}/delete", TEST_PACIENTE_ID)
							.with(csrf())
							.param("pacienteId", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/pacientes"));
	}
        
        @WithMockUser(value = "spring")
	@Test
	void testBorrarPacienteNoPacienteFound() throws Exception {
	    BDDMockito.given(this.userService.getCurrentMedico()).willReturn(this.medico1);
        	
        mockMvc.perform(get("/pacientes/{pacienteId}/delete", 10)
							.with(csrf())
							.param("pacienteId", "10"))
				.andExpect(status().isOk())
				.andExpect(model().attributeDoesNotExist("paciente"))
				.andExpect(view().name("/pacientes"));
	}
}		