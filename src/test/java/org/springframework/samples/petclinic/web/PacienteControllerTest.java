
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PacienteController.class, 
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PacienteControllerTest {

	private static final int	TEST_PACIENTE_ID	= 1;

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


	@BeforeEach
	void setup() {

		this.medico.setNombre("Medico 2");
		this.medico.setApellidos("Apellidos");
		this.medico.setDNI("12345678Z");
		this.medico.setN_telefono("123456789");
		this.medico.setDomicilio("Domicilio");
		this.medicoUser.setUsername("medico2");
		this.medicoUser.setPassword("medico2");
		this.medicoUser.setEnabled(true);
		this.medico.setUser(this.medicoUser);
		this.authorities.setUsername(this.medicoUser.getUsername());
		this.authorities.setAuthority("medico");

		this.javier.setNombre("Javier");
		this.javier.setApellidos("Silva");
		this.javier.setF_nacimiento(LocalDate.of(1997, 6, 8));
		this.javier.setDNI("12345678Z");
		this.javier.setDomicilio("");
		this.javier.setEmail("javier_silva@gmail.com");
		this.javier.setF_alta(LocalDate.now());
		this.javier.setMedico(this.medico);

		BDDMockito.given(this.userService.findUserByUsername(this.medicoUser.getUsername()).get()).willReturn(this.medicoUser);
		BDDMockito.given(this.medicoService.getMedicoById(this.medico.getId())).willReturn(this.medico);
		BDDMockito.given(this.pacienteService.findPacienteById(this.javier.getId()).get()).willReturn(this.javier);
	}
}
