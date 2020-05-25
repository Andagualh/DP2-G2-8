
package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.PacienteService;
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
/*
 * @TestPropertySource(
 * locations = "classpath:application-mysql.properties")
 */
public class CitaControllerE2ETest {

	@Autowired
	private PacienteService		pacienteService;
	@Autowired
	private CitaService			citaService;

	@Autowired
	private MockMvc				mockMvc;

	private static final int	TEST_MEDICO_ID		= 1;
	private static final int	TEST_PACIENTE_ID	= 1;
	private static final String	TEST_USER_ID		= "1";
	private static final int	TEST_CITA_ID		= 1;


	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testCrearCita() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/new/{pacienteId}", CitaControllerE2ETest.TEST_PACIENTE_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("citas/createOrUpdateCitaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("cita"));
	}

	//Prueba cuando intentas crear una cita para un paciente que no es tuyo

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testCrearCitaforPacienteAnotherMedico() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/new/{pacienteId}", 7)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSalvarCitaSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/citas/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("paciente.id", Integer.toString(CitaControllerE2ETest.TEST_PACIENTE_ID)).param("paciente.nombre", "test")
			.param("paciente.apellidos", "test").param("paciente.f_nacimiento", "1997/09/09").param("paciente.f_alta", "2020/08/08").param("paciente.DNI", "12345689Q").param("paciente.medico.id", Integer.toString(CitaControllerE2ETest.TEST_MEDICO_ID))
			.param("paciente.medico.nombre", "test").param("paciente.medico.apellidos", "test").param("paciente.medico.domicilio", "test").param("paciente.medico.user.username", "test").param("paciente.medico.user.password", "test")
			.param("fecha", "2020-08-08").param("lugar", "Seville")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/citas"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSalvarCitaHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/citas/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("paciente.id", Integer.toString(CitaControllerE2ETest.TEST_PACIENTE_ID)).param("paciente.nombre", "test")
				.param("paciente.apellidos", "test").param("paciente.f_nacimiento", "1997/09/09").param("paciente.f_alta", "2020/08/08").param("paciente.DNI", "12345689Q").param("paciente.medico.id", Integer.toString(CitaControllerE2ETest.TEST_MEDICO_ID))
				.param("paciente.medico.nombre", "test").param("paciente.medico.apellidos", "test").param("paciente.medico.domicilio", "test").param("paciente.medico.user.username", "test").param("paciente.medico.user.password", "test")
				.param("fecha", "2020-09-09").param("lugar", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("cita")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("citas/createOrUpdateCitaForm"));
	}

	//Este caso se da cuando un paciente ya tiene una cita ese mismo día
	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSalvarCitaWhenPacienteAlreadyHasThatFecha() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/citas/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("paciente.id", Integer.toString(CitaControllerE2ETest.TEST_PACIENTE_ID)).param("paciente.nombre", "test")
			.param("paciente.apellidos", "test").param("paciente.f_nacimiento", "1997/09/09").param("paciente.f_alta", "2020/08/08").param("paciente.DNI", "12345689Q").param("paciente.medico.id", Integer.toString(CitaControllerE2ETest.TEST_MEDICO_ID))
			.param("paciente.medico.nombre", "test").param("paciente.medico.apellidos", "test").param("paciente.medico.domicilio", "test").param("paciente.medico.user.username", "test").param("paciente.medico.user.password", "test")
			.param("fecha", "2020-08-08").param("lugar", "Seville")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/citas"));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/citas/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("paciente.id", Integer.toString(CitaControllerE2ETest.TEST_PACIENTE_ID)).param("paciente.nombre", "test")
			.param("paciente.apellidos", "test").param("paciente.f_nacimiento", "1997/09/09").param("paciente.f_alta", "2020/08/08").param("paciente.DNI", "12345689Q").param("paciente.medico.id", Integer.toString(CitaControllerE2ETest.TEST_MEDICO_ID))
			.param("paciente.medico.nombre", "test").param("paciente.medico.apellidos", "test").param("paciente.medico.domicilio", "test").param("paciente.medico.user.username", "test").param("paciente.medico.user.password", "test")
			.param("fecha", "2020-08-08").param("lugar", "Seville")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("citas/createOrUpdateCitaForm"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSalvarCitaPastDate() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/citas/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("paciente.id", Integer.toString(CitaControllerE2ETest.TEST_PACIENTE_ID)).param("paciente.nombre", "test")
				.param("paciente.apellidos", "test").param("paciente.f_nacimiento", "1997/09/09").param("paciente.f_alta", "2020/08/08").param("paciente.DNI", "12345689Q").param("paciente.medico.id", Integer.toString(CitaControllerE2ETest.TEST_MEDICO_ID))
				.param("paciente.medico.nombre", "test").param("paciente.medico.apellidos", "test").param("paciente.medico.domicilio", "test").param("paciente.medico.user.username", "test").param("paciente.medico.user.password", "test")
				.param("fecha", "2019-01-01").param("lugar", "Seville"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", "La fecha debe estar en presente o futuro")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("citas/createOrUpdateCitaForm"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSalvarCitaHasErrorsCoverage() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/citas/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("paciente.id", Integer.toString(CitaControllerE2ETest.TEST_PACIENTE_ID)).param("paciente.nombre", "test")
				.param("paciente.apellidos", "test").param("paciente.f_nacimiento", "1997/09/09").param("paciente.f_alta", "2020/08/08").param("paciente.DNI", "12345689Q").param("paciente.medico.id", Integer.toString(CitaControllerE2ETest.TEST_MEDICO_ID))
				.param("paciente.medico.nombre", "test").param("paciente.medico.apellidos", "test").param("paciente.medico.domicilio", "test").param("paciente.medico.user.username", "test").param("paciente.medico.user.password", "test")
				.param("fecha", "2019-01-01").param("lugar", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("cita")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cita", "lugar")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("citas/createOrUpdateCitaForm"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarCitas() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/delete/{citaId}", CitaControllerE2ETest.TEST_CITA_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/citas"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarCitaNoPresente() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/delete/{citaId}", 2)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/citas"));
	}

	@WithMockUser(username = "andresMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarCitasPacienteDifferentMedico() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/delete/{citaId}", 1))
			//.andExpect(model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/find")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("cita")).andExpect(MockMvcResultMatchers.view().name("citas/findCitas"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessFindFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/porfecha").param("fecha", "2020-03-09")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.view().name("citas/listCitas"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessFindFormNoDate() throws Exception {
		Cita cita1 = new Cita();
		cita1.setFecha(LocalDate.now());
		cita1.setLugar("LugarTest");
		cita1.setPaciente(this.pacienteService.findPacienteById(1).get());
		this.citaService.save(cita1);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/porfecha")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.view().name("citas/listCitas"));
		this.citaService.delete(cita1);
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessFindFormNoCitaFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/porfecha").param("fecha", "2020-08-07")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cita", "fecha"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("cita", "fecha", "error.citaNotFound")).andExpect(MockMvcResultMatchers.view().name("citas/findCitas"));
	}
	//Existe una cita para otro medico (AlvaroMedico) en esta misma fecha
	@WithMockUser(username = "andresMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessFindFormNoCitaFoundForThisMedicoOnDate() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/porfecha").param("fecha", "2020-03-09")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cita", "fecha"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("cita", "fecha", "error.citaNotFound")).andExpect(MockMvcResultMatchers.view().name("citas/findCitas"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/citas/" + CitaControllerE2ETest.TEST_USER_ID));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void listadoCitasSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{medicoId}", CitaControllerE2ETest.TEST_MEDICO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.view().name("citas/listCitas"));
	}

	@WithMockUser(username = "andresMedico", authorities = {
		"medico"
	})
	@Test
	void listadoCitasOfOtherMedico() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{medicoId}", CitaControllerE2ETest.TEST_MEDICO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));
	}

	@WithMockUser(username = "pedroMedico", authorities = {
		"medico"
	})
	@Test
	void listadoCitasIsEmpty() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{medicoId}", 4)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

}
