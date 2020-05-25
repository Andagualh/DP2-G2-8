
package org.springframework.samples.petclinic.web.E2E;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

public class HistoriaClinicaControllerE2ETest {

	private static final int	TEST_PACIENTESINHISTORIA_ID	= 7;
	private static final int	TEST_PACIENTECONHISTORIA_ID	= 8;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testShowHistoriaClinica() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica")).andExpect(MockMvcResultMatchers.view().name("pacientes/historiaClinicaDetails"));
	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerE2ETest.TEST_PACIENTESINHISTORIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pacientes/createOrUpdateHistoriaClinicaForm")).andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica"));
	}

	//La descripcion no puede estar vacia
	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerE2ETest.TEST_PACIENTESINHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", ""))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pacientes/createOrUpdateHistoriaClinicaForm"));

	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerE2ETest.TEST_PACIENTESINHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", "Depresion"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/{pacienteId}"));
	}

	//El paciente ya tiene una historia clinica
	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationFormFailure() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", "error"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));

	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/edit", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica")).andExpect(MockMvcResultMatchers.model().attributeExists("paciente")).andExpect(MockMvcResultMatchers.view().name("pacientes/createOrUpdateHistoriaClinicaForm"));

	}

	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/edit", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", "Historia actualizada")
				.param("paciente", Integer.toString(HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID)))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/{pacienteId}"));
	}

	//La descripcion esta vacia
	@WithMockUser(username = "admin1", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/edit", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", "")
			.param("paciente", Integer.toString(HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pacientes/createOrUpdateHistoriaClinicaForm"));
	}
}
