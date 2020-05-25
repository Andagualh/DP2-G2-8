package org.springframework.samples.petclinic.web.E2E;

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
import java.util.List;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.PetclinicApplication;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cita;
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

	private int TEST_PACIENTE_ID;
	private int TEST_PACIENTE_ID_2;
	private static final int TEST_MEDICO_ID = 1;
	private static final String TEST_MEDICOUSER_ID = "alvaroMedico";
	
	@Autowired
	private PacienteService			pacienteService;
	@Autowired
	private MedicoService			medicoService;
	@Autowired
	private UserService				userService;
	@Autowired
	private CitaService				citaService;
	@Autowired
	private HistoriaClinicaService	historiaClinicaService;
	
	@Autowired
	private MockMvc 			mockMvc;

	private Medico 				medico;

	private Paciente 			javier;
	private Paciente 			manuel;
	
	//Creacion de objetos de apoyo para las pruebas
	@BeforeEach
	void setup() throws InvalidAttributeValueException{
		medico = this.medicoService.getMedicoById(TEST_MEDICO_ID);
		
		javier = new Paciente();
		javier.setNombre("Javier");
		javier.setApellidos("Silva");
		javier.setF_nacimiento(LocalDate.of(1997, 6, 8));
		javier.setDNI("12345678Z");
		javier.setDomicilio("Ecija");
		javier.setN_telefono(612345987);
		javier.setEmail("javier_silva@gmail.com");
		javier.setF_alta(LocalDate.now());
		javier.setMedico(this.medico);
		pacienteService.savePaciente(this.javier);
		TEST_PACIENTE_ID = javier.getId();
		
		manuel = new Paciente();
		manuel.setNombre("Manuel");
		manuel.setApellidos("Gonzalez");
		manuel.setF_nacimiento(LocalDate.of(1998, 7, 16));
		manuel.setDNI("12345679Z");
		manuel.setDomicilio("La Puebla");
		manuel.setN_telefono(612345903);
		manuel.setEmail("manuel_gonzalez@gmail.com");
		manuel.setF_alta(LocalDate.now());
		manuel.setMedico(this.medico);
		pacienteService.savePaciente(this.manuel);
		TEST_PACIENTE_ID_2 = manuel.getId();
	
	}
	
	@AfterEach
	void undo() throws DataAccessException, IllegalAccessException {
		this.pacienteService.pacienteDelete(this.javier.getId());
		TEST_PACIENTE_ID = 0;
		this.pacienteService.pacienteDelete(this.manuel.getId());
		TEST_PACIENTE_ID_2 = 0;
		
	}
	
	//Creacion de los test necesarios
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testShowPacienteWithCitas() throws Exception {
		mockMvc.perform(get("/pacientes/{pacienteId}", 1))
		.andExpect(status().isOk())
		.andExpect(view().name("pacientes/pacienteDetails"))
		.andExpect(model().attributeExists("paciente"))
		.andExpect(model().attributeExists("medicoCheck"))
		.andExpect(model().attributeExists("canBeDeleted"));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testShowPacienteCanBeDeleted() throws Exception {		
				
		mockMvc.perform(get("/pacientes/{pacienteId}", 9))
		.andExpect(status().isOk())
		.andExpect(view().name("pacientes/pacienteDetails"))
		.andExpect(model().attributeExists("paciente"))
		.andExpect(model().attributeExists("medicoCheck"))
		.andExpect(model().attributeExists("canBeDeleted"));
		
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testShowPacienteCanBeDeleted2() throws Exception {		
				
		mockMvc.perform(get("/pacientes/{pacienteId}", 8))
		.andExpect(status().isOk())
		.andExpect(view().name("pacientes/pacienteDetails"))
		.andExpect(model().attributeExists("paciente"))
		.andExpect(model().attributeExists("medicoCheck"))
		.andExpect(model().attributeExists("canBeDeleted"));
		
	}

	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testInitFindForm() throws Exception {
		mockMvc.perform(get("/pacientes/find"))
		.andExpect(status().isOk())
		.andExpect(view().name("pacientes/findPacientes"))
		.andExpect(model().attributeExists("paciente"));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessFindFormSeveral() throws Exception {
		List<Paciente> l = new ArrayList<Paciente>();
		l.add(javier);
		l.add(manuel);
		
		mockMvc.perform(get("/pacientes"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("selections"))
		.andExpect(view().name("pacientes/pacientesList"));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessFindFormOnlyOne() throws Exception {
		
		mockMvc.perform(get("/pacientes").param("apellidos", "Silva"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/pacientes/" + this.javier.getId()));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessFindFormEmpty() throws Exception {

		
		mockMvc.perform(get("/pacientes").param("apellidos", "González Gutierrez"))
		.andExpect(status().isOk())
		.andExpect(view().name("pacientes/findPacientes"));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testInitFindMedForm() throws Exception{
		
		mockMvc.perform(get("/pacientes/findByMedico"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/pacientes/findByMedico/" + TEST_MEDICO_ID));	
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessFindMedFormEmpty() throws Exception {
		
		//El médico con id=4 no tiene pacientes asociados
		mockMvc.perform(get("/pacientes/findByMedico/{medicoId}", 4))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/pacientes/"));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessFindMedFormSuccess() throws Exception {
		List<Paciente> l = new ArrayList<Paciente>();
		l.add(this.javier);
		l.add(this.manuel);	
		
		mockMvc.perform(get("/pacientes/findByMedico/{medicoId}", TEST_MEDICO_ID))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("selections"))
		.andExpect(view().name("pacientes/pacientesListMedico"));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testBorrarPacienteSuccess() throws Exception {
			
		mockMvc.perform(post("/pacientes/{pacienteId}/delete",9)
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				//.andExpect(model().attributeExists("message"))
				.andExpect(view().name("redirect:/pacientes"));
	}
	
	@WithMockUser(username="pedroMedico",authorities= {"medico"})
    @Test
    void testBorrarPacienteOtroMedico() throws Exception {
	
		mockMvc.perform(post("/pacientes/{pacienteId}/delete", 1)
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/pacientes/" +1));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testBorrarPacienteCantDelete() throws Exception {
			
		mockMvc.perform(post("/pacientes/{pacienteId}/delete",1)
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				//.andExpect(model().attributeExists("message"))
				.andExpect(view().name("redirect:/pacientes/"+1));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testBorrarPacienteNoEncontrado() throws Exception {
			
		mockMvc.perform(post("/pacientes/{pacienteId}/delete",20)
				.with(csrf()))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testInitUpdatePacienteForm() throws Exception {
			
		mockMvc.perform(get("/pacientes/{pacienteId}/edit", TEST_PACIENTE_ID))
		.andExpect(model().attributeExists("paciente"))
		.andExpect(model().attributeExists("medicoList"))
		.andExpect(model().attributeExists("isNewPaciente"))
		.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testInitUpdatePacienteFormError() throws Exception {
			
		mockMvc.perform(get("/pacientes/{pacienteId}/edit", 7))
		.andExpect(view().name("redirect:/pacientes/" + 7));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessUpdatePacienteFormWithoutContacto() throws Exception {
		
		mockMvc.perform(post("/pacientes/{pacienteId}/edit", javier.getId())
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Mateos")
				.param("f_nacimiento", "1990/03/21")
				.param("DNI", "12345674Z")
				.param("f_alta", "2020/05/08")
				.param("n_telefono", "")
				.param("email", "")
				.param("domicilio", ""))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("medicoList"))
			.andExpect(model().attributeExists("isNewPaciente"))
			.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessUpdatePacienteFormTelefonoInvalido() throws Exception {
		
		mockMvc.perform(post("/pacientes/{pacienteId}/edit", javier.getId())
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Mateos")
				.param("f_nacimiento", "1990/03/21")
				.param("DNI", "12345674Z")
				.param("f_alta", "2020/05/08")
				.param("n_telefono", "456")
				.param("email", "pacomateos@gmail.com")
				.param("domicilio", "Su Casa, 45"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("medicoList"))
			.andExpect(model().attributeExists("isNewPaciente"))
			.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessUpdatePacienteFormDniInvalido() throws Exception {
		
		mockMvc.perform(post("/pacientes/{pacienteId}/edit", javier.getId())
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Mateos")
				.param("f_nacimiento", "1990/03/21")
				.param("DNI", "12345678Z")
				.param("f_alta", "2020/05/08")
				.param("n_telefono", "456678543")
				.param("email", "pacomateos@gmail.com")
				.param("domicilio", "Su Casa, 45"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("medicoList"))
			.andExpect(model().attributeExists("isNewPaciente"))
			.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessUpdatePacienteFormSuccess() throws Exception {
		
		mockMvc.perform(post("/pacientes/{pacienteId}/edit", javier.getId())
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Mateos")
				.param("f_nacimiento", "1990/03/21")
				.param("DNI", "53279183M")
				.param("f_alta", "2020/05/08")
				.param("n_telefono", "666666666")
				.param("email", "pacomateos@gmail.com")
				.param("domicilio", "Calle cualquiera")
				.param("medico.id", Integer.toString(TEST_MEDICO_ID))
				.param("medico.nombre", medico.getNombre())
				.param("medico.apellidos", medico.getApellidos())
				.param("medico.DNI", medico.getDNI())
				.param("medico.n_telefono", medico.getN_telefono())
				.param("medico.domicilio", medico.getDomicilio())
				.param("medico.user.username", TEST_MEDICOUSER_ID)
				.param("medico.user.password", "entrar")
				.param("medico.user.enabled", "true"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/pacientes/" + javier.getId()));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testInitCreationForm() throws Exception {
				
		mockMvc.perform(get("/pacientes/new"))
		.andExpect(model().attributeExists("paciente"))
		.andExpect(model().attributeExists("medicoList"))
		.andExpect(model().attributeExists("isNewPaciente"))
		.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessCreatePacienteFormWithoutContacto() throws Exception {
		
		mockMvc.perform(post("/pacientes/new")
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Mateos")
				.param("f_nacimiento", "1990/03/21")
				.param("DNI", "12345674Z")
				.param("f_alta", "2020/05/08")
				.param("n_telefono", "")
				.param("email", "")
				.param("domicilio", "")
				.param("medico.id", Integer.toString(TEST_MEDICO_ID))
				.param("medico.nombre", medico.getNombre())
				.param("medico.apellidos", medico.getApellidos())
				.param("medico.DNI", medico.getDNI())
				.param("medico.n_telefono", medico.getN_telefono())
				.param("medico.domicilio", medico.getDomicilio())
				.param("medico.user.username", TEST_MEDICOUSER_ID)
				.param("medico.user.password", "entrar")
				.param("medico.user.enabled", "true"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("medicoList"))
			.andExpect(model().attributeExists("isNewPaciente"))
			.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessCreatePacienteFormTelefonoInvalido() throws Exception {
		
		mockMvc.perform(post("/pacientes/new")
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Mateos")
				.param("f_nacimiento", "1990/03/21")
				.param("DNI", "12345674Z")
				.param("f_alta", "2020/05/08")
				.param("n_telefono", "456")
				.param("email", "pacomateos@gmail.com")
				.param("domicilio", "Su Casa, 45")
				.param("medico.id", Integer.toString(TEST_MEDICO_ID))
				.param("medico.nombre", medico.getNombre())
				.param("medico.apellidos", medico.getApellidos())
				.param("medico.DNI", medico.getDNI())
				.param("medico.n_telefono", medico.getN_telefono())
				.param("medico.domicilio", medico.getDomicilio())
				.param("medico.user.username", TEST_MEDICOUSER_ID)
				.param("medico.user.password", "entrar")
				.param("medico.user.enabled", "true"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("medicoList"))
			.andExpect(model().attributeExists("isNewPaciente"))
			.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="pedroMedico",authorities= {"medico"})
    @Test
    void testProcessCreatePacienteFormOtroMedico() throws Exception {
		
		mockMvc.perform(post("/pacientes/new")
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Mateos")
				.param("f_nacimiento", "1990/03/21")
				.param("DNI", "53279183M")
				.param("f_alta", "2020/05/08")
				.param("n_telefono", "666666666")
				.param("email", "pacomateos@gmail.com")
				.param("domicilio", "Calle cualquiera")
				.param("medico.id", Integer.toString(TEST_MEDICO_ID))
				.param("medico.nombre", medico.getNombre())
				.param("medico.apellidos", medico.getApellidos())
				.param("medico.DNI", medico.getDNI())
				.param("medico.n_telefono", medico.getN_telefono())
				.param("medico.domicilio", medico.getDomicilio())
				.param("medico.user.username", TEST_MEDICOUSER_ID)
				.param("medico.user.password", "entrar")
				.param("medico.user.enabled", "true"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessCreatePacienteFormDniIncorrecto() throws Exception {
		
		mockMvc.perform(post("/pacientes/new")
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Mateos")
				.param("f_nacimiento", "1990/03/21")
				.param("DNI", "12345678M")
				.param("f_alta", "2020/05/08")
				.param("n_telefono", "666666666")
				.param("email", "pacomateos@gmail.com")
				.param("domicilio", "Calle cualquiera")
				.param("medico.id", Integer.toString(TEST_MEDICO_ID))
				.param("medico.nombre", medico.getNombre())
				.param("medico.apellidos", medico.getApellidos())
				.param("medico.DNI", medico.getDNI())
				.param("medico.n_telefono", medico.getN_telefono())
				.param("medico.domicilio", medico.getDomicilio())
				.param("medico.user.username", TEST_MEDICOUSER_ID)
				.param("medico.user.password", "entrar")
				.param("medico.user.enabled", "true"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessCreatePacienteFormSuccess() throws Exception {
		
		int prox_id = this.pacienteService.pacienteCount()+3;
		
		mockMvc.perform(post("/pacientes/new")
				.with(csrf())
				.param("nombre", "Paco")
				.param("apellidos", "Mateos")
				.param("f_nacimiento", "1990/03/21")
				.param("DNI", "53279183M")
				.param("f_alta", "2020/05/08")
				.param("n_telefono", "666666666")
				.param("email", "pacomateos@gmail.com")
				.param("domicilio", "Calle cualquiera")
				.param("medico.id", Integer.toString(TEST_MEDICO_ID))
				.param("medico.nombre", medico.getNombre())
				.param("medico.apellidos", medico.getApellidos())
				.param("medico.DNI", medico.getDNI())
				.param("medico.n_telefono", medico.getN_telefono())
				.param("medico.domicilio", medico.getDomicilio())
				.param("medico.user.username", TEST_MEDICOUSER_ID)
				.param("medico.user.password", "entrar")
				.param("medico.user.enabled", "true"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/pacientes/" + prox_id));
	}
}
