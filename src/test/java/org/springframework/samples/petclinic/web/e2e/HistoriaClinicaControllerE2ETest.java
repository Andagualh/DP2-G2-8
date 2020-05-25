
package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.MedicoService;
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

public class HistoriaClinicaControllerE2ETest {

	private static final int	TEST_PACIENTESINHISTORIA_ID	= 7;
	private static final int	TEST_PACIENTECONHISTORIA_ID	= 8;
	private static final int	TEST_MEDICO_ID	= 1;
	
	@Autowired
	private MedicoService medicoService;
	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testShowHistoriaClinicaNotNull() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica")).andExpect(MockMvcResultMatchers.view().name("pacientes/historiaClinicaDetails"));
	}
	

	@WithMockUser(username = "alvaroMedico", authorities = {
			"medico"
		})
		@Test
		void testShowHistoriaClinicaNull() throws Exception {
		
		Medico m  = this.medicoService.getMedicoById(TEST_MEDICO_ID);
		
		Paciente javier = new Paciente();
		javier.setNombre("Javier");
		javier.setApellidos("Silva");
		javier.setF_nacimiento(LocalDate.of(1997, 6, 8));
		javier.setDNI("12345678Z");
		javier.setDomicilio("Ecija");
		javier.setN_telefono(612345987);
		javier.setEmail("javier_silva@gmail.com");
		javier.setF_alta(LocalDate.now());
		javier.setMedico(m);
		
		this.pacienteService.savePaciente(javier);
		
		int id = javier.getId();
		
			this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica", id)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica")).andExpect(MockMvcResultMatchers.view().name("pacientes/historiaClinicaDetails"));
		}
	
	@WithMockUser(username = "pabloMedico", authorities = {
		"medico"
	})
	@Test
	void testInitCreationFormAuthorized() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerE2ETest.TEST_PACIENTESINHISTORIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pacientes/createOrUpdateHistoriaClinicaForm")).andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica"));
	}
	
	@WithMockUser(username = "alvaroMedico", authorities = {
			"medico"
		})
		@Test
		void testInitCreationFormNotAuthorized() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerE2ETest.TEST_PACIENTESINHISTORIA_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes"));
		}

	//La descripcion no puede estar vacia
	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerE2ETest.TEST_PACIENTESINHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", ""))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pacientes/createOrUpdateHistoriaClinicaForm"));

	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerE2ETest.TEST_PACIENTESINHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", "Depresion"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/{pacienteId}"));
	}

	//El paciente ya tiene una historia clinica
	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessCreationFormFailure() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/new", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", "error"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));

	}

	@WithMockUser(username = "pabloMedico", authorities = {
		"medico"
	})
	@Test
	void testInitUpdateFormAuthorized() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/edit", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("historiaclinica")).andExpect(MockMvcResultMatchers.model().attributeExists("paciente")).andExpect(MockMvcResultMatchers.view().name("pacientes/createOrUpdateHistoriaClinicaForm"));

	}

	
	@WithMockUser(username = "alvaroMedico", authorities = {
			"medico"
		})
		@Test
		void testInitUpdateFormNotAuthorized() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{pacienteId}/historiaclinica/edit", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes"));

		}

	
	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/edit", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", "Historia actualizada")
				.param("paciente", Integer.toString(HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID)))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/{pacienteId}"));
	}

	//La descripcion esta vacia
	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pacientes/{pacienteId}/historiaclinica/edit", HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("descripcion", "")
			.param("paciente", Integer.toString(HistoriaClinicaControllerE2ETest.TEST_PACIENTECONHISTORIA_ID))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pacientes/createOrUpdateHistoriaClinicaForm"));
	}
}
