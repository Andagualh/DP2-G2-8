
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
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = PacienteController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PacienteControllerTest2 {

	private static final int	TEST_PACIENTE_ID	= 1;
	private static final int	TEST_MEDICO_ID		= 1;
	private static final String	TEST_MEDICOUSER_ID	= "medico1";
	private static final String	TEST_AUTHORITIES_ID	= PacienteControllerTest2.TEST_MEDICOUSER_ID;

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

	private Paciente			laura;

	private Medico				medico1;

	private User				medico1User;

	private Authorities			authorities;


	@BeforeEach
	void setup() {
		this.medico1 = new Medico();
		this.medico1.setId(PacienteControllerTest2.TEST_MEDICO_ID);
		this.medico1.setNombre("Medico 2");
		this.medico1.setApellidos("Apellidos");
		this.medico1.setDNI("12345678Z");
		this.medico1.setN_telefono("123456789");
		this.medico1.setDomicilio("Domicilio");

		this.medico1User = new User();
		this.medico1User.setUsername(PacienteControllerTest2.TEST_MEDICOUSER_ID);
		this.medico1User.setPassword("medico1");
		this.medico1User.setEnabled(true);

		this.medico1.setUser(this.medico1User);
		this.medico1.getUser().setEnabled(true);

		this.authorities = new Authorities();
		this.authorities.setUsername(PacienteControllerTest2.TEST_MEDICOUSER_ID);
		this.authorities.setAuthority("medico");

		this.javier = new Paciente();
		this.javier.setId(PacienteControllerTest2.TEST_PACIENTE_ID);
		this.javier.setNombre("Javier");
		this.javier.setApellidos("Silva");
		this.javier.setF_nacimiento(LocalDate.of(1997, 6, 8));
		this.javier.setDNI("12345678Z");
		this.javier.setDomicilio("Ecija");
		this.javier.setN_telefono(612345987);
		this.javier.setEmail("javier_silva@gmail.com");
		this.javier.setF_alta(LocalDate.now());
		this.javier.setMedico(this.medico1);

		this.laura = new Paciente();
		this.laura.setId(2);
		this.laura.setNombre("Laura");
		this.laura.setApellidos("Silva");
		this.laura.setF_nacimiento(LocalDate.of(1997, 2, 20));
		this.laura.setDNI("12345678Z");
		this.laura.setDomicilio("Ecija");
		this.laura.setN_telefono(612345988);
		this.laura.setEmail("lauraricharte@gmail.com");
		this.laura.setF_alta(LocalDate.now());
		this.laura.setMedico(this.medico1);

		BDDMockito.given(this.userService.findUserByUsername(PacienteControllerTest2.TEST_MEDICOUSER_ID)).willReturn(Optional.of(this.medico1User));
		BDDMockito.given(this.medicoService.getMedicoById(PacienteControllerTest2.TEST_MEDICO_ID)).willReturn(this.medico1);
		BDDMockito.given(this.pacienteService.findPacienteById(PacienteControllerTest2.TEST_PACIENTE_ID)).willReturn(Optional.of(this.javier));
		BDDMockito.given(this.pacienteService.findPacienteById(2)).willReturn(Optional.of(this.laura));

	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdatePacienteFormSuccess() throws Exception {

		//BDDMockito.given(this.userService.getCurrentMedico().getId()).willReturn(1);
		//		BDDMockito.given(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("admin")))
		//			.willReturn(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("admin")));

		//BDDMockito.when(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("admin")))
		//	.thenReturn(false);
		//Mockito.when(this.userService.getCurrentMedico().getId()).thenReturn(1);

		//		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		//		authorities.add(new SimpleGrantedAuthority("admin"));
		//		Authentication authentication = Mockito.mock(Authentication.class);
		//		BDDMockito.given(authentication.isAuthenticated()).willReturn(true);
		//		BDDMockito.given(authentication.getAuthorities()).willReturn(grantedAuths);
		//		
		//		
		//		
		//		
		//		
		//		
		//		
		//		
		//		//Mockito.when(authentication.getAuthorities()).thenReturn(new String("admin"));
		//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		//		SecurityContextHolder.setContext(securityContext);

		Medico medic = new Medico();
		medic.setId(PacienteControllerTest2.TEST_MEDICO_ID);
		BDDMockito.given(this.userService.getCurrentMedico()).willReturn(medic);

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/edit", PacienteControllerTest2.TEST_PACIENTE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("currentMedico", "1").param("nombre", "Javier").param("apellidos", "Silva")
				.param("f_nacimiento", "1997/06/08").param("DNI", "12345678Z").param("domicilio", "Ecija").param("n_telefono", "612345987").param("email", "javier_silva@gmail.com").param("f_alta", "2020/03/25")
				.param("medico.id", Integer.toString(PacienteControllerTest2.TEST_MEDICO_ID)).param("medico.nombre", "Medico 2").param("medico.apellidos", "Apellidos").param("medico.DNI", "12345678Z").param("medico.n_telefono", "123456789")
				.param("medico.domicilio", "Domicilio").param("medico.user.username", PacienteControllerTest2.TEST_MEDICOUSER_ID).param("medico.user.password", "medico1").param("medico.user.enabled", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/{pacienteId}"));
	}
}
