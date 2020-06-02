
package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalDate;

import javax.management.InvalidAttributeValueException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.InformeService;
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
public class TestInformeControladorE2E {

	@Autowired
	private PacienteService			pacienteService;
	@Autowired
	private CitaService				citaService;
	@Autowired
	private InformeService			informeService;
	@Autowired
	private HistoriaClinicaService	hcService;

	@Autowired
	private MockMvc					mockMvc;

	private static final int		TEST_MEDICO_ID		= 1;
	private static final int		TEST_PACIENTE_ID	= 1;
	private static final String		TEST_USER_ID		= "1";
	private static final String		TEST_MEDICOUSER_ID	= "1";

	private int						TEST_INFORME_ID;
	private int						TEST_CITA_ID;
	private Cita					cita;


	//Los datos se crean en BBDD y borran según se usan

	@BeforeEach
	void setUp() throws InvalidAttributeValueException {
		this.cita = new Cita();
		this.cita.setFecha(LocalDate.now());
		this.cita.setLugar("Lugar");
		this.cita.setPaciente(this.pacienteService.findPacienteById(TestInformeControladorE2E.TEST_PACIENTE_ID).get());
		this.citaService.save(this.cita);
		this.TEST_CITA_ID = this.cita.getId();

	}

	@AfterEach
	void undo() throws DataAccessException, IllegalAccessException {
		this.citaService.delete(this.cita);
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testCreateInforme() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/new", this.TEST_CITA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("informes/createOrUpdateInformeForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("informe"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testCreateInformeForCitaDifferentDateWithInforme() throws Exception {

		Cita citaTemp = this.citaService.findCitaById(1).get();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/new", citaTemp.getId())).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/citas/1/informes/1"));
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testCreateInformeForCitaDifferentDate() throws Exception {

		Cita cita2 = new Cita();
		cita2.setFecha(LocalDate.now().minusDays(1));
		cita2.setLugar("Lugar");
		cita2.setPaciente(this.pacienteService.findPacienteById(TestInformeControladorE2E.TEST_PACIENTE_ID).get());
		this.citaService.saveOldDate(cita2);
		int TEST_CITA2_ID = cita2.getId();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/new", TEST_CITA2_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/citas/" + TestInformeControladorE2E.TEST_MEDICO_ID));

		this.citaService.delete(cita2);
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testCreateInformeCitaWithInforme() throws Exception {

		Cita cita2 = new Cita();
		cita2.setFecha(LocalDate.now());
		cita2.setLugar("Lugar");
		cita2.setPaciente(this.pacienteService.findPacienteById(TestInformeControladorE2E.TEST_PACIENTE_ID).get());
		this.citaService.save(cita2);
		int TEST_CITA2_ID = cita2.getId();

		Informe informe = new Informe();
		informe.setCita(cita2);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		int TEST_CITA2_INFORME_ID = informe.getId();

		cita2.setInforme(informe);
		this.citaService.save(cita2);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/new", TEST_CITA2_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/citas/" + TEST_CITA2_ID + "/informes/" + TEST_CITA2_INFORME_ID));

		this.informeService.deleteInforme(informe.getId());
		this.citaService.delete(cita2);
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSaveInformeSuccess() throws Exception {

		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/citas/{citaId}/informes/new", this.TEST_CITA_ID).with(SecurityMockMvcRequestPostProcessors.csrf())
			/*
			 * .param("id", Integer.toString(TEST_INFORME_ID))
			 * .param("motivo_consulta", "Consulta")
			 * .param("diagnostico", "diagnostico")
			 * .param("cita", "${cita}")
			 */
			.flashAttr("informe", informe)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/citas/" + TestInformeControladorE2E.TEST_MEDICO_ID));

		this.informeService.deleteInforme(informe.getId());

	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSaveInformeHasErrors() throws Exception {

		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setMotivo_consulta("motivo");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/citas/{citaId}/informes/new", this.TEST_CITA_ID).with(SecurityMockMvcRequestPostProcessors.csrf())
			/*
			 * .param("id", Integer.toString(TEST_INFORME_ID))
			 * .param("motivo_consulta", "Consulta")
			 * .param("diagnostico", "diagnostico")
			 * .param("cita", "${cita}")
			 */
			.flashAttr("informe", informe)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("informes/createOrUpdateInformeForm")).andExpect(MockMvcResultMatchers.model().attributeExists("informe"));

	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitUpdateInforme() throws Exception {

		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}/edit", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("informes/createOrUpdateInformeForm")).andExpect(MockMvcResultMatchers.model().attributeExists("informe")).andExpect(MockMvcResultMatchers.model().attributeExists("motivo_consulta"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("diagnostico")).andExpect(MockMvcResultMatchers.model().attributeExists("cita"));

		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSalvarInformeEditSuccess() throws Exception {

		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();
		informe.setDiagnostico("diagnosticoTest");
		informe.setMotivo_consulta("motivoTest");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/citas/{citaId}/informes/{informeId}/edit", this.TEST_CITA_ID, this.TEST_INFORME_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).flashAttr("informe", informe))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/citas/" + TestInformeControladorE2E.TEST_MEDICO_ID));
		this.informeService.deleteInforme(this.TEST_INFORME_ID);
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSalvarInformeEditHasErrors() throws Exception {

		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setMotivo_consulta("motivo");
		informe.setDiagnostico("Diagnostico");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();
		informe.setDiagnostico("");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/citas/{citaId}/informes/{informeId}/edit", this.TEST_CITA_ID, this.TEST_INFORME_ID).with(SecurityMockMvcRequestPostProcessors.csrf())
			/*
			 * .param("id", Integer.toString(TEST_INFORME_ID))
			 * .param("motivo_consulta", "Consulta")
			 * .param("diagnostico", "diagnostico")
			 * .param("cita", "${cita}")
			 */
			.flashAttr("informe", informe)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("informes/createOrUpdateInformeForm")).andExpect(MockMvcResultMatchers.model().attributeExists("informe"));
		this.informeService.deleteInforme(this.TEST_INFORME_ID);
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarInformeSuccess() throws Exception {
		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");

		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/delete/{informeId}", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarInformeCantDelete() throws Exception {
		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informe.setHistoriaClinica(this.hcService.findHistoriaClinicaByPaciente(this.cita.getPaciente()));
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/delete/{informeId}", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarInformeCantDeletePastHC() throws Exception {
		Informe informe = this.informeService.findInformeById(3).get();
		this.TEST_INFORME_ID = informe.getId();
		Cita pastCita = informe.getCita();
		int TEST_CITA3_ID = pastCita.getId();
		informe.setHistoriaClinica(this.hcService.findHistoriaClinicaByPaciente(pastCita.getPaciente()));
		this.informeService.saveInformeWithHistoriaClinica(informe);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/delete/{informeId}", TEST_CITA3_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testShowInforme() throws Exception {

		Informe informe = new Informe();
		//Cita con LocalDate now
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.TEST_CITA_ID = informe.getCita().getId();
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();
		

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("informes/informeDetails")).andExpect(MockMvcResultMatchers.model().attributeExists("informe")).andExpect(MockMvcResultMatchers.model().attribute("cannotbedeleted", false))
			.andExpect(MockMvcResultMatchers.model().attribute("canbeedited", true)

			);
		this.informeService.deleteInforme(this.TEST_INFORME_ID);
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testShowInformeDifferentDate() throws Exception {

		Informe informe = this.informeService.findInformeById(3).get();
		this.TEST_INFORME_ID = informe.getId();
		int TEST_CITA3_ID = informe.getCita().getId();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}", TEST_CITA3_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("informes/informeDetails"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("informe")).andExpect(MockMvcResultMatchers.model().attribute("cannotbedeleted", true)).andExpect(MockMvcResultMatchers.model().attribute("canbeedited", false));
		this.TEST_INFORME_ID = 0;

	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testShowInformeWithHCPast() throws Exception {

		Informe informe = this.informeService.findInformeById(3).get();
		this.TEST_INFORME_ID = informe.getId();
		Cita pastCita = informe.getCita();
		int TEST_CITA3_ID = pastCita.getId();
		informe.setHistoriaClinica(this.hcService.findHistoriaClinicaByPaciente(pastCita.getPaciente()));
		this.informeService.saveInformeWithHistoriaClinica(informe);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}", TEST_CITA3_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("informes/informeDetails"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("informe")).andExpect(MockMvcResultMatchers.model().attribute("cannotbedeleted", true)).andExpect(MockMvcResultMatchers.model().attribute("canbeedited", false));
		this.TEST_INFORME_ID = 0;

	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testShowInformeWithHC() throws Exception {

		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();

		informe.setHistoriaClinica(this.hcService.findHistoriaClinicaByPaciente(this.cita.getPaciente()));
		this.informeService.saveInformeWithHistoriaClinica(informe);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("informes/informeDetails")).andExpect(MockMvcResultMatchers.model().attributeExists("informe")).andExpect(MockMvcResultMatchers.model().attribute("cannotbedeleted", true))
			.andExpect(MockMvcResultMatchers.model().attribute("canbeedited", true));

		this.informeService.deleteInformeToHistoriaClinica(informe);
		this.informeService.deleteInforme(this.TEST_INFORME_ID);
		this.TEST_INFORME_ID = 0;

	}

	@WithMockUser(username = "alvaroMedico", authorities = {"medico"})
	@Test
	void testShowInformeWithManyTratamientos() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}", 1, 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("informes/informeDetails"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("informe"))
			.andExpect(MockMvcResultMatchers.model().attribute("cannotbedeleted", true))
			.andExpect(MockMvcResultMatchers.model().attribute("canbeedited", false))
			.andExpect(MockMvcResultMatchers.model().attribute("editTratamientoOk", false))
			.andExpect(MockMvcResultMatchers.model().attributeExists("tratamientos"))
			.andExpect(MockMvcResultMatchers.model().attribute("tratapages", 1)
			);
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testAddHistoriaClinicaToInforme() throws Exception {
		Informe informe = new Informe();

		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();
		informe.setHistoriaClinica(this.hcService.findHistoriaClinicaByPaciente(this.cita.getPaciente()));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}/addtohistoriaclinica", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/pacientes/" + TestInformeControladorE2E.TEST_PACIENTE_ID + "/historiaclinica"));

		this.informeService.deleteInformeToHistoriaClinica(informe);
		this.informeService.deleteInforme(this.TEST_INFORME_ID);
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testDeleteHistoriaClinicaToInforme() throws Exception {
		Informe informe = new Informe();

		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();
		informe.setHistoriaClinica(this.hcService.findHistoriaClinicaByPaciente(this.cita.getPaciente()));
		this.informeService.saveInformeWithHistoriaClinica(informe);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}/detelefromhistoriaclinica", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/citas/" + TestInformeControladorE2E.TEST_MEDICO_ID + "/informes/" + this.TEST_INFORME_ID));

		this.informeService.deleteInforme(this.TEST_INFORME_ID);
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "pabloMedico", authorities = {
		"medico"
	})
	@Test
	void testCreateInformeMedicoIncorrecto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/new", this.TEST_CITA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));
	}

	@WithMockUser(username = "pabloMedico", authorities = {
		"medico"
	})
	@Test
	void testShowInformeMedicoIncorrecto() throws Exception {

		Informe informe = new Informe();
		//Cita con LocalDate now
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.TEST_CITA_ID = informe.getCita().getId();
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));

		this.informeService.deleteInforme(this.TEST_INFORME_ID);
	}

	@WithMockUser(username = "pabloMedico", authorities = {
		"medico"
	})
	@Test
	void testBorrarInformeMedicoIncorrecto() throws Exception {
		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");

		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/delete/{informeId}", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));
	}

	@WithMockUser(username = "pabloMedico", authorities = {
		"medico"
	})
	@Test
	void testUpdateInformeMedicoIncorrecto() throws Exception {

		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}/edit", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));
	}

	//Comprueba que el medico puede ver un informe que no es suyo si está asociado a una historia clinica
	@WithMockUser(username = "pabloMedico", authorities = {
		"medico"
	})
	@Test
	void testShowInformeWithHCMedicoDiferente() throws Exception {

		Informe informe = new Informe();
		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();

		informe.setHistoriaClinica(this.hcService.findHistoriaClinicaByPaciente(this.cita.getPaciente()));
		this.informeService.saveInformeWithHistoriaClinica(informe);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("informes/informeDetails")).andExpect(MockMvcResultMatchers.model().attributeExists("informe")).andExpect(MockMvcResultMatchers.model().attribute("cannotbedeleted", true))
			.andExpect(MockMvcResultMatchers.model().attribute("canbeedited", true));

		this.informeService.deleteInformeToHistoriaClinica(informe);
		this.informeService.deleteInforme(this.TEST_INFORME_ID);
		this.TEST_INFORME_ID = 0;

	}

	@WithMockUser(username = "pabloMedico", authorities = {
		"medico"
	})
	@Test
	void testAddHistoriaClinicaToInformeMedicoIncorrecto() throws Exception {
		Informe informe = new Informe();

		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();
		informe.setHistoriaClinica(this.hcService.findHistoriaClinicaByPaciente(this.cita.getPaciente()));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}/addtohistoriaclinica", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));

		this.informeService.deleteInformeToHistoriaClinica(informe);
		this.informeService.deleteInforme(this.TEST_INFORME_ID);
		this.TEST_INFORME_ID = 0;
	}

	@WithMockUser(username = "pabloMedico", authorities = {
		"medico"
	})
	@Test
	void testDeleteHistoriaClinicaToInformeMedicoIncorrecto() throws Exception {
		Informe informe = new Informe();

		informe.setCita(this.cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		this.informeService.saveInforme(informe);
		this.TEST_INFORME_ID = informe.getId();
		informe.setHistoriaClinica(this.hcService.findHistoriaClinicaByPaciente(this.cita.getPaciente()));
		this.informeService.saveInformeWithHistoriaClinica(informe);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/citas/{citaId}/informes/{informeId}/detelefromhistoriaclinica", this.TEST_CITA_ID, this.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("accessNotAuthorized"));

	}

}
