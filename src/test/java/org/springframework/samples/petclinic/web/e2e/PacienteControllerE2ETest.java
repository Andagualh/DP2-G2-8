package org.springframework.samples.petclinic.web.e2e;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.PetclinicApplication;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.PetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class PacienteControllerE2ETest {

	//Creacion de atributos para la clase de test
	private static final String				VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM	= "pacientes/createOrUpdatePacientesForm";

	private static final int TEST_PACIENTE_ID = 1;
	private static final int TEST_PACIENTE_ID_2 = 2;
	private static final int TEST_MEDICO_ID = 1;
	private static final String TEST_MEDICOUSER_ID = "medico1";
	
	@MockBean
	private PacienteService			pacienteService;
	@MockBean
	private MedicoService			medicoService;
	@MockBean
	private UserService				userService;
	@MockBean
	private CitaService				citaService;
	@MockBean
	private HistoriaClinicaService	historiaClinicaService;
	
	@Autowired
	private MockMvc 			mockMvc;

	private Medico 				medico1;

	private User 				medico1User;

	private Paciente 			javier;
	private Paciente 			manuel;
	
	private Authorities			authorities;
	
	//Creacion de objetos de apoyo para las pruebas
	@BeforeEach
	void setup() {
		this.medico1 = new Medico();
		this.medico1.setId(TEST_MEDICO_ID);
		this.medico1.setNombre("Medico 1");
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
		
		this.manuel = new Paciente();
		this.manuel.setId(TEST_PACIENTE_ID_2);
		this.manuel.setNombre("Manuel");
		this.manuel.setApellidos("Gonzalez");
		this.manuel.setF_nacimiento(LocalDate.of(1998, 7, 16));
		this.manuel.setDNI("12345679Z");
		this.manuel.setDomicilio("La Puebla");
		this.manuel.setN_telefono(612345903);
		this.manuel.setEmail("manuel_gonzalez@gmail.com");
		this.manuel.setF_alta(LocalDate.now());
		this.manuel.setMedico(this.medico1);
		
		BDDMockito.given(this.userService.findUserByUsername(TEST_MEDICOUSER_ID)).willReturn(Optional.of(this.medico1User));
		BDDMockito.given(this.medicoService.getMedicoById(TEST_MEDICO_ID)).willReturn(this.medico1);
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(this.javier));
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID_2)).willReturn(Optional.of(this.manuel));
	
	}
	//Creacion de los test necesarios
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testShowPaciente() throws Exception {
		mockMvc.perform(get("/pacientes/{pacienteId}", TEST_PACIENTE_ID))
		.andExpect(status().isOk())
		.andExpect(view().name("pacientes/pacienteDetails"))
		.andExpect(model().attributeExists("paciente"))
		.andExpect(model().attributeExists("canBeDeleted"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testInitFindForm() throws Exception {
		mockMvc.perform(get("/pacientes/find"))
		.andExpect(status().isOk())
		.andExpect(view().name("pacientes/findPacientes"))
		.andExpect(model().attributeExists("paciente"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessFindFormSeveral() throws Exception {
		List<Paciente> l = new ArrayList<Paciente>();
		l.add(javier);
		l.add(manuel);
		BDDMockito.given(this.pacienteService.getPacientes()).willReturn(l);
		
		mockMvc.perform(get("/pacientes"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("selections"))
		.andExpect(view().name("pacientes/pacientesList"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessFindFormOnlyOne() throws Exception {
		BDDMockito.given(this.pacienteService.findPacienteByApellidos(this.javier.getApellidos()))
		.willReturn(Lists.newArrayList(this.javier));
		//BDDMockito.given(this.pacienteService.getPacientes()).willReturn(l);
		
		mockMvc.perform(get("/pacientes").param("apellidos", "Silva"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/pacientes/" + this.javier.getId()));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessFindFormEmpty() throws Exception {
		List<Paciente> l = new ArrayList<Paciente>();
		BDDMockito.given(this.pacienteService.findPacienteByApellidos(this.javier.getApellidos()))
		.willReturn(l);	
		
		mockMvc.perform(get("/pacientes"))
		.andExpect(status().isOk())
		.andExpect(view().name("pacientes/findPacientes"));
	}
	
//	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
//    @Test
//    void testInitFindMedForm() throws Exception{
//		BDDMockito.given(this.userService.getCurrentMedico().getId()).willReturn(TEST_MEDICO_ID);
//		
//		mockMvc.perform(get("/pacientes/findByMedico"))
//		.andExpect(status().is2xxSuccessful())
//		.andExpect(view().name("redirect:/pacientes/findByMedico/" + TEST_MEDICO_ID));	
//	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessFindMedFormEmpty() throws Exception {
		List<Paciente> l = new ArrayList<Paciente>();
		BDDMockito.given(this.pacienteService.findPacienteByMedicoId(TEST_MEDICO_ID))
		.willReturn(l);	
		
		mockMvc.perform(get("/pacientes/findByMedico/{medicoId}", TEST_MEDICO_ID))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/pacientes/"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessFindMedFormSuccess() throws Exception {
		List<Paciente> l = new ArrayList<Paciente>();
		l.add(this.javier);
		l.add(this.manuel);
		BDDMockito.given(this.pacienteService.findPacienteByMedicoId(TEST_MEDICO_ID))
		.willReturn(l);	
		
		mockMvc.perform(get("/pacientes/findByMedico/{medicoId}", TEST_MEDICO_ID))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("selections"))
		.andExpect(view().name("pacientes/pacientesListMedico"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testBorrarPacienteSuccess() throws Exception {
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID))
		.willReturn(Optional.of(this.javier));
	    BDDMockito.given(this.userService.getCurrentMedico()).willReturn(this.medico1);

	
		mockMvc.perform(post("/pacientes/{pacienteId}/delete", TEST_PACIENTE_ID)
				.with(csrf())
				.param("pacienteId", "1"))
				.andExpect(status().is3xxRedirection())
				//.andExpect(model().attributeExists("message"))
				.andExpect(view().name("redirect:/pacientes"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testBorrarPacienteNoExiste() throws Exception {
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID))
		.willReturn(Optional.of(this.javier));
	    BDDMockito.given(this.userService.getCurrentMedico()).willReturn(this.medico1);

	
		mockMvc.perform(post("/pacientes/{pacienteId}/delete", 25)
				.with(csrf())
				.param("pacienteId", "25"))
				.andExpect(status().isOk())
				.andExpect(model().attributeDoesNotExist("paciente"))
				.andExpect(model().attributeExists("message"))
				.andExpect(view().name("/pacientes"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testInitUpdatePacienteForm() throws Exception {
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID))
		.willReturn(Optional.of(this.javier));
		
		mockMvc.perform(get("/pacientes/{pacienteId}/edit", TEST_PACIENTE_ID))
		.andExpect(model().attributeExists("paciente"))
		.andExpect(model().attributeExists("medicoList"))
		.andExpect(model().attributeExists("isNewPaciente"))
		.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
//	@WithMockUser(username="admin1",authorities= {"admin"})
//    @Test
//    void testProcessUpdatePacienteFormWithoutContacto() throws Exception {
//		
//		mockMvc.perform(post("/pacientes/{pacienteId}/edit")
//				.with(csrf())
//				.param("nombre", "Paco")
//				.param("apellidos", "Mateos")
//				.param("f_nacimiento", "1990/03/21")
//				.param("DNI", "12345674Z")
//				.param("f_alta", "2020/05/08")
//				.param("n_telefono", "")
//				.param("email", "")
//				.param("domicilio", ""))
//			.andExpect(status().isOk())
//			.andExpect(model().attributeExists("medicoList"))
//			.andExpect(model().attributeExists("isNewPaciente"))
////			.andExpect(model().attributeHasFieldErrors("paciente", "n_telefono"))
////			.andExpect(model().attributeHasFieldErrors("paciente", "email"))
////			.andExpect(model().attributeHasFieldErrors("paciente", "domicilio"))
//			.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
//	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testInitCreationForm() throws Exception {
		
		BDDMockito.given(this.userService.getCurrentMedico()).willReturn(this.medico1);
		
		mockMvc.perform(get("/pacientes/new"))
		.andExpect(model().attributeExists("paciente"))
		.andExpect(model().attributeExists("medicoList"))
		.andExpect(model().attributeExists("isNewPaciente"))
		.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessCreationForm() throws Exception {
		
		mockMvc.perform(post("/pacientes/new")
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Silva")
				.param("f_nacimiento", "1997/06/08")
				.param("DNI", "12345678Z")
				.param("domicilio", "")
				.param("n_telefono", "")
				.param("email", "")
				.param("f_alta", "2020/03/25"))
		.andExpect(model().attributeExists("medicoList"))
		.andExpect(model().attributeExists("isNewPaciente"))
		.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
}
