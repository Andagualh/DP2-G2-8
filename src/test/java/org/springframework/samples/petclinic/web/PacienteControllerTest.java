
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;


import org.hamcrest.beans.HasProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Medico;
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

	private Medico				medico;

	private User				medicoUser;

	private Authorities			authorities;

	private static final int TEST_MEDICO_ID = 1; 
    private static final int TEST_PACIENTE_ID = 1;
    private static final String TEST_USER_ID = "1";


	@BeforeEach
	void setup() {
		this.javier = new Paciente();
		this.medico = new Medico();
		
		this.medico.setId(TEST_MEDICO_ID);
		this.medico.setNombre("Medico 2");
		this.medico.setApellidos("Apellidos");
		this.medico.setDNI("12345678Z");
		this.medico.setN_telefono("123456789");
		this.medico.setDomicilio("Domicilio");
		
		this.javier.setId(TEST_PACIENTE_ID);
		this.javier.setNombre("Javier");
		this.javier.setApellidos("Silva");
		this.javier.setF_nacimiento(LocalDate.of(1997, 6, 8));
		this.javier.setDNI("12345678Z");
		this.javier.setEmail("javier_silva@gmail.com");
		this.javier.setF_alta(LocalDate.of(2020, 4,4));
		this.javier.setMedico(this.medico);
			
		given(this.userService.findUserByUsername(TEST_USER_ID)).willReturn(Optional.of(new User()));
		given(this.medicoService.getMedicoById(TEST_MEDICO_ID)).willReturn(this.medico);
		given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(javier));
		
	
	}

	@WithMockUser(value= "spring")
		@Test
		void TestInitUpdatePacienteForm() throws Exception{
			mockMvc.perform(get("/pacientes/{pacienteId}/edit", TEST_PACIENTE_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("paciente"))
			.andExpect(model().attribute("paciente", hasProperty("nombre", is("Javier"))))
			.andExpect(model().attribute("paciente", hasProperty("apellidos", is("Silva"))))
			.andExpect(model().attribute("paciente", hasProperty("f_nacimiento", is(LocalDate.of(1997, 6,8)))))
			.andExpect(model().attribute("paciente", hasProperty("DNI", is("12345678Z"))))
			.andExpect(model().attribute("paciente", hasProperty("email", is("javier_silva@gmail.com"))))
			.andExpect(model().attribute("paciente", hasProperty("f_alta", is(LocalDate.of(2020, 4,4)))))
			.andExpect(model().attribute("paciente", hasProperty("medico", is(this.medico))))
			.andExpect(view().name("pacientes/createOrUpdatePacientesForm")
			);
		}

	@WithMockUser(value="spring")
		@Test
		void TestProcessUpdatePacienteFormSucccess() throws Exception{
			mockMvc.perform(post("/pacientes/{pacienteId}/edit", TEST_PACIENTE_ID)
					.with(csrf())
					.param("id", Integer.toString(TEST_PACIENTE_ID))
					.param("nombre", "Laura")
					.param("apellidos", "Richarte")
					.param("f_nacimiento", "1997/11/11")
					.param("DNI", "12342312K")
					.param("n_telefono", "122348727")
					.param("domicilio", "Calle Cordoba 11")
					.param("email", "laricharte@hotmail.es")
					.param("f_alta", "2020/04/04")
					.param("medico.id", Integer.toString(TEST_MEDICO_ID))
					.param("medico.nombre", "Medico 2")
					.param("medico.apellidos", "Apellidos")
					.param("medico.domicilio", "Domicilio"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/pacientes/{pacienteId}"));

			}	

}
