
package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.meta.When;

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
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = PacienteController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PacienteControllerTest2 {

	private static final int  TEST_PACIENTE_ID = 1;
	private static final int  TEST_MEDICO_ID = 1;
	private static final String TEST_MEDICOUSER_ID = "medico1";
	
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
	
	@MockBean
	private CitaService citaService;
	
	@MockBean
	private HistoriaClinicaService historiaClinicaService;

	@Autowired
	private MockMvc				mockMvc;

	private Paciente			javier;
	
	private Paciente			sara;

	private Medico				medico1;

	private User				medico1User;

	private Authorities			authorities;


	@BeforeEach
	void setup() {
		this.medico1 = new Medico();
		this.medico1.setId(TEST_MEDICO_ID);
		this.medico1.setNombre("Medico");
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
	void testCreatePacienteFormSuccess() throws Exception {
		ArrayList<Medico> medicos = new ArrayList<Medico>();
		medicos.add(this.medico1);
			
		BDDMockito.given(this.medicoService.getMedicos()).willReturn(medicos);
		BDDMockito.given(this.userService.getCurrentMedico()).willReturn(this.medico1);
		BDDMockito.given(this.pacienteService.savePacienteByMedico(BDDMockito.any(Paciente.class), BDDMockito.any(Medico.class).getId())).willReturn(TEST_PACIENTE_ID);
		
		mockMvc.perform(post("/pacientes/new")
							.with(csrf())
							.param("nombre", "Javier")
							.param("apellidos", "Silva")
							.param("f_nacimiento", "1997/06/08")
							.param("DNI", "12345678Z")
							.param("domicilio", "Ecija")
							.param("n_telefono", "")
							.param("email", "javier_silva@gmail.com")
							.param("f_alta", "2020/03/25")
							.param("medico.id", Integer.toString(TEST_MEDICO_ID))
							.param("medico.nombre", "Medico")
							.param("medico.apellidos", "Apellidos")
							.param("medico.DNI", "12345678Z")
							.param("medico.n_telefono", "123456789")
							.param("medico.domicilio", "Domicilio")
							.param("medico.user.username", TEST_MEDICOUSER_ID)
							.param("medico.user.password", "medico1")
							.param("medico.user.enabled", "true"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/pacientes/"+TEST_PACIENTE_ID));
	}
	
}		