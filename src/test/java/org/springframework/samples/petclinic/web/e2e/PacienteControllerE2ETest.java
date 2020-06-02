package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class PacienteControllerE2ETest {

	//Creacion de atributos para la clase de test
	private static final String		VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM	= "pacientes/createOrUpdatePacientesForm";

	private int						TEST_PACIENTE_ID;
	private int						TEST_PACIENTE_ID_2;
	private static final int		TEST_MEDICO_ID							= 1;
	private static final String		TEST_MEDICOUSER_ID						= "alvaroMedico";

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
	private MockMvc					mockMvc;

	private Medico					medico;

	private Paciente				javier;
	private Paciente				manuel;


	//Creacion de objetos de apoyo para las pruebas
	@BeforeEach
	void setup() throws InvalidAttributeValueException {
		this.medico = this.medicoService.getMedicoById(PacienteControllerE2ETest.TEST_MEDICO_ID);

		this.javier = new Paciente();
		this.javier.setNombre("Javier");
		this.javier.setApellidos("Silva");
		this.javier.setF_nacimiento(LocalDate.of(1997, 6, 8));
		this.javier.setDNI("12345678Z");
		this.javier.setDomicilio("Ecija");
		this.javier.setN_telefono(612345987);
		this.javier.setEmail("javier_silva@gmail.com");
		this.javier.setF_alta(LocalDate.now());
		this.javier.setMedico(this.medico);
		this.pacienteService.savePaciente(this.javier);
		this.TEST_PACIENTE_ID = this.javier.getId();

		this.manuel = new Paciente();
		this.manuel.setNombre("Manuel");
		this.manuel.setApellidos("Gonzalez");
		this.manuel.setF_nacimiento(LocalDate.of(1998, 7, 16));
		this.manuel.setDNI("12345679Z");
		this.manuel.setDomicilio("La Puebla");
		this.manuel.setN_telefono(612345903);
		this.manuel.setEmail("manuel_gonzalez@gmail.com");
		this.manuel.setF_alta(LocalDate.now());
		this.manuel.setMedico(this.medico);
		this.pacienteService.savePaciente(this.manuel);
		this.TEST_PACIENTE_ID_2 = this.manuel.getId();

	}

	@AfterEach
	void undo() throws DataAccessException, IllegalAccessException {
		this.pacienteService.pacienteDelete(this.javier.getId());
		this.TEST_PACIENTE_ID = 0;
		this.pacienteService.pacienteDelete(this.manuel.getId());
		this.TEST_PACIENTE_ID_2 = 0;

	}

	//Creacion de los test necesarios
	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testShowPacienteWithCitas() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pacientes/pacienteDetails"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("paciente")).andExpect(MockMvcResultMatchers.model().attributeExists("medicoCheck")).andExpect(MockMvcResultMatchers.model().attributeExists("canBeDeleted"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testShowPacienteCanBeDeleted() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}", 9)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pacientes/pacienteDetails"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("paciente")).andExpect(MockMvcResultMatchers.model().attributeExists("medicoCheck")).andExpect(MockMvcResultMatchers.model().attributeExists("canBeDeleted"));

	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testShowPacienteCanBeDeleted2() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}", 8)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pacientes/pacienteDetails"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("paciente")).andExpect(MockMvcResultMatchers.model().attributeExists("medicoCheck")).andExpect(MockMvcResultMatchers.model().attributeExists("canBeDeleted"));

	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/find")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pacientes/findPacientes"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("paciente"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessFindFormSeveral() throws Exception {
		List<Paciente> l = new ArrayList<Paciente>();
		l.add(this.javier);
		l.add(this.manuel);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.view().name("pacientes/pacientesList"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessFindFormOnlyOne() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes").param("apellidos", "Silva")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/" + this.javier.getId()));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessFindFormEmpty() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes").param("apellidos", "González Gutierrez")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pacientes/findPacientes"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitFindMedForm() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/findByMedico")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/findByMedico/" + PacienteControllerE2ETest.TEST_MEDICO_ID));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessFindMedFormEmpty() throws Exception {

		//El médico con id=4 no tiene pacientes asociados
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/findByMedico/{medicoId}", 4)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessFindMedFormSuccess() throws Exception {
		List<Paciente> l = new ArrayList<Paciente>();
		l.add(this.javier);
		l.add(this.manuel);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/findByMedico/{medicoId}", PacienteControllerE2ETest.TEST_MEDICO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.view().name("pacientes/pacientesListMedico"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarPacienteSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/delete", 9)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes"));
	}

	@WithMockUser(username = "pedroMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarPacienteOtroMedico() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/delete", 1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/" + 1));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarPacienteCantDelete() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/delete", 1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/" + 1));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarPacienteNoEncontrado() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/delete", 20)
		.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitUpdatePacienteForm() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/edit", this.TEST_PACIENTE_ID)).andExpect(MockMvcResultMatchers.model().attributeExists("paciente")).andExpect(MockMvcResultMatchers.model().attributeExists("medicoList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("isNewPaciente")).andExpect(MockMvcResultMatchers.view().name(PacienteControllerE2ETest.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitUpdatePacienteFormError() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/edit", 7)).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/" + 7));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessUpdatePacienteFormWithoutContacto() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/edit", this.javier.getId()).with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "Paco").param("apellidos", "Mateos").param("f_nacimiento", "1990/03/21")
				.param("DNI", "12345674Z").param("f_alta", "2020/05/08").param("n_telefono", "").param("email", "").param("domicilio", ""))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("medicoList")).andExpect(MockMvcResultMatchers.model().attributeExists("isNewPaciente"))
			.andExpect(MockMvcResultMatchers.view().name(PacienteControllerE2ETest.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessUpdatePacienteFormTelefonoInvalido() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/edit", this.javier.getId()).with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "Paco").param("apellidos", "Mateos").param("f_nacimiento", "1990/03/21")
				.param("DNI", "12345674Z").param("f_alta", "2020/05/08").param("n_telefono", "456").param("email", "pacomateos@gmail.com").param("domicilio", "Su Casa, 45"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("medicoList")).andExpect(MockMvcResultMatchers.model().attributeExists("isNewPaciente"))
			.andExpect(MockMvcResultMatchers.view().name(PacienteControllerE2ETest.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessUpdatePacienteFormDniInvalido() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/edit", this.javier.getId()).with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "Paco").param("apellidos", "Mateos").param("f_nacimiento", "1990/03/21")
				.param("DNI", "12345678Z").param("f_alta", "2020/05/08").param("n_telefono", "456678543").param("email", "pacomateos@gmail.com").param("domicilio", "Su Casa, 45"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("medicoList")).andExpect(MockMvcResultMatchers.model().attributeExists("isNewPaciente"))
			.andExpect(MockMvcResultMatchers.view().name(PacienteControllerE2ETest.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessUpdatePacienteFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/edit", this.javier.getId()).with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "Paco").param("apellidos", "Mateos").param("f_nacimiento", "1990/03/21")
			.param("DNI", "53279183M").param("f_alta", "2020/05/08").param("n_telefono", "666666666").param("email", "pacomateos@gmail.com").param("domicilio", "Calle cualquiera")
			.param("medico.id", Integer.toString(PacienteControllerE2ETest.TEST_MEDICO_ID)).param("medico.nombre", this.medico.getNombre()).param("medico.apellidos", this.medico.getApellidos()).param("medico.DNI", this.medico.getDNI())
			.param("medico.n_telefono", this.medico.getN_telefono()).param("medico.domicilio", this.medico.getDomicilio()).param("medico.user.username", PacienteControllerE2ETest.TEST_MEDICOUSER_ID).param("medico.user.password", "entrar")
			.param("medico.user.enabled", "true")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/" + this.javier.getId()));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitCreationForm() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/new")).andExpect(MockMvcResultMatchers.model().attributeExists("paciente")).andExpect(MockMvcResultMatchers.model().attributeExists("medicoList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("isNewPaciente")).andExpect(MockMvcResultMatchers.view().name(PacienteControllerE2ETest.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessCreatePacienteFormWithoutContacto() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "Paco").param("apellidos", "Mateos").param("f_nacimiento", "1990/03/21").param("DNI", "12345674Z")
				.param("f_alta", "2020/05/08").param("n_telefono", "").param("email", "").param("domicilio", "").param("medico.id", Integer.toString(PacienteControllerE2ETest.TEST_MEDICO_ID)).param("medico.nombre", this.medico.getNombre())
				.param("medico.apellidos", this.medico.getApellidos()).param("medico.DNI", this.medico.getDNI()).param("medico.n_telefono", this.medico.getN_telefono()).param("medico.domicilio", this.medico.getDomicilio())
				.param("medico.user.username", PacienteControllerE2ETest.TEST_MEDICOUSER_ID).param("medico.user.password", "entrar").param("medico.user.enabled", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("medicoList")).andExpect(MockMvcResultMatchers.model().attributeExists("isNewPaciente"))
			.andExpect(MockMvcResultMatchers.view().name(PacienteControllerE2ETest.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessCreatePacienteFormTelefonoInvalido() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "Paco").param("apellidos", "Mateos").param("f_nacimiento", "1990/03/21").param("DNI", "12345674Z")
				.param("f_alta", "2020/05/08").param("n_telefono", "456").param("email", "pacomateos@gmail.com").param("domicilio", "Su Casa, 45").param("medico.id", Integer.toString(PacienteControllerE2ETest.TEST_MEDICO_ID))
				.param("medico.nombre", this.medico.getNombre()).param("medico.apellidos", this.medico.getApellidos()).param("medico.DNI", this.medico.getDNI()).param("medico.n_telefono", this.medico.getN_telefono())
				.param("medico.domicilio", this.medico.getDomicilio()).param("medico.user.username", PacienteControllerE2ETest.TEST_MEDICOUSER_ID).param("medico.user.password", "entrar").param("medico.user.enabled", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("medicoList")).andExpect(MockMvcResultMatchers.model().attributeExists("isNewPaciente"))
			.andExpect(MockMvcResultMatchers.view().name(PacienteControllerE2ETest.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "pedroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessCreatePacienteFormOtroMedico() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "Paco").param("apellidos", "Mateos").param("f_nacimiento", "1990/03/21").param("DNI", "53279183M")
				.param("f_alta", "2020/05/08").param("n_telefono", "666666666").param("email", "pacomateos@gmail.com").param("domicilio", "Calle cualquiera").param("medico.id", Integer.toString(PacienteControllerE2ETest.TEST_MEDICO_ID))
				.param("medico.nombre", this.medico.getNombre()).param("medico.apellidos", this.medico.getApellidos()).param("medico.DNI", this.medico.getDNI()).param("medico.n_telefono", this.medico.getN_telefono())
				.param("medico.domicilio", this.medico.getDomicilio()).param("medico.user.username", PacienteControllerE2ETest.TEST_MEDICOUSER_ID).param("medico.user.password", "entrar").param("medico.user.enabled", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name(PacienteControllerE2ETest.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessCreatePacienteFormDniIncorrecto() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "Paco").param("apellidos", "Mateos").param("f_nacimiento", "1990/03/21").param("DNI", "12345678M")
				.param("f_alta", "2020/05/08").param("n_telefono", "666666666").param("email", "pacomateos@gmail.com").param("domicilio", "Calle cualquiera").param("medico.id", Integer.toString(PacienteControllerE2ETest.TEST_MEDICO_ID))
				.param("medico.nombre", this.medico.getNombre()).param("medico.apellidos", this.medico.getApellidos()).param("medico.DNI", this.medico.getDNI()).param("medico.n_telefono", this.medico.getN_telefono())
				.param("medico.domicilio", this.medico.getDomicilio()).param("medico.user.username", PacienteControllerE2ETest.TEST_MEDICOUSER_ID).param("medico.user.password", "entrar").param("medico.user.enabled", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name(PacienteControllerE2ETest.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM));
	}

	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
    @Test
    void testProcessCreatePacienteFormSuccess() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
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
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

			//Since this method on the controller only will redirect in case of a succesful action, it's not a necesity to check the view name since only gives troubles to recover the new paciente id

	}
}
