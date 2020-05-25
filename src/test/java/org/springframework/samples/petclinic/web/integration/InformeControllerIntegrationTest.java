package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Collections;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.CitaServiceTest;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.web.InformeController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class InformeControllerIntegrationTest {

	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private CitaService citaService;
	@Autowired
	private InformeService informeService;
	@Autowired
	private HistoriaClinicaService hcService;
	@Autowired
	private InformeController informeController;

	private static int TEST_PACIENTE_ID = 1;

	@Test
	void initCreateInforme() throws Exception {
		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);
		int TEST_CITA_ID = cita.getId();

		ModelMap model = new ModelMap();

		String view = informeController.initCreationForm(cita, model);

		assertEquals(view, "informes/createOrUpdateInformeForm");
		assertNotNull("informe");

		citaService.delete(cita);
	}

	@Test
	void testInitCreateInformeForCitaDifferentDateWithInforme() throws Exception {
		Cita citaTemp = citaService.findCitaById(1).get();
		ModelMap model = new ModelMap();
		String view = informeController.initCreationForm(citaTemp, model);

		assertEquals("redirect:/citas/1/informes/1", view);
	}

	@Test
	void testInitCreateInformeForCitaDifferentDate() throws Exception {
		Cita cita2 = new Cita();
		cita2.setFecha(LocalDate.now().minusDays(1));
		cita2.setLugar("Lugar");
		cita2.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.saveOldDate(cita2);
		int TEST_CITA2_ID = cita2.getId();

		ModelMap model = new ModelMap();
		String view = informeController.initCreationForm(cita2, model);

		assertEquals("redirect:/citas/" + cita2.getPaciente().getMedico().getId(), view);
		citaService.delete(cita2);
	}

	@Test
	void testInitCreateInformeForCitaWithInforme() throws Exception {
		Cita cita2 = new Cita();
		cita2.setFecha(LocalDate.now());
		cita2.setLugar("Lugar");
		cita2.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita2);

		Informe informe = new Informe();
		informe.setCita(cita2);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);

		cita2.setInforme(informe);
		citaService.save(cita2);

		ModelMap model = new ModelMap();
		String view = informeController.initCreationForm(cita2, model);
		assertEquals("redirect:/citas/" + cita2.getId() + "/informes/" + cita2.getInforme().getId(), view);
		informeService.deleteInforme(informe.getId());
		citaService.delete(cita2);

	}

	@Test
	void testProcessCreateInformeSuccess() throws Exception {

		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		ModelMap model = new ModelMap();
		String view = informeController.processCreationForm(cita, informe, bindingResult, model);
		assertEquals("redirect:/citas/" + cita.getPaciente().getMedico().getId(), view);

		informeService.deleteInforme(informe.getId());
		citaService.delete(cita);
	}

	@Test
	void testProcessCreateInformeHasErrors() throws Exception {

		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setMotivo_consulta("motivo");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("diagnostico", "Required Field!");

		ModelMap model = new ModelMap();
		String view = informeController.processCreationForm(cita, informe, bindingResult, model);

		assertEquals("informes/createOrUpdateInformeForm", view);
		assertNotNull("informe");

		citaService.delete(cita);
	}

	@Test
	void testInitUpdateInforme() throws Exception {

		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);

		ModelMap model = new ModelMap();
		String view = informeController.initUpdateInformeForm(informe.getId(), model);

		assertEquals("informes/createOrUpdateInformeForm", view);
		assertNotNull("informe");

		informeService.deleteInforme(informe.getId());
		citaService.delete(cita);

	}

	@Test
	void testProcessEditInformeSuccess() throws Exception {
		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);
		informe.setDiagnostico("diagnosticoTest");
		informe.setMotivo_consulta("motivoTest");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		ModelMap model = new ModelMap();
		String view = informeController.processUpdateInformeForm(cita, informe, bindingResult, informe.getId(), model);

		assertEquals("redirect:/citas/" + informe.getCita().getPaciente().getMedico().getId(), view);

		informeService.deleteInforme(informe.getId());
		citaService.delete(cita);

	}

	@Test
	void testProcessEditInformeEditHasErrors() throws Exception {
		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);
		informe.setDiagnostico("diagnosticoTest");
		informe.setMotivo_consulta("");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("motivo_consulta", "Required Field!");

		ModelMap model = new ModelMap();
		String view = informeController.processUpdateInformeForm(cita, informe, bindingResult, informe.getId(), model);

		assertEquals("informes/createOrUpdateInformeForm", view);
		assertNotNull("informe");

		informeService.deleteInforme(informe.getId());
		citaService.delete(cita);

	}

	@Test
	void testBorrarInformeSuccess() throws Exception {

		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);

		ModelMap model = new ModelMap();

		String view = informeController.borrarInforme(informe.getId(), model);
		assertEquals("redirect:/", view);

		citaService.delete(cita);
	}

	@Test
	void testBorrarInformeCantDelete() throws Exception {

		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);
		informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(cita.getPaciente()));
		informeService.saveInformeWithHistoriaClinica(informe);

		ModelMap model = new ModelMap();

		String view = informeController.borrarInforme(informe.getId(), model);

		assertEquals("redirect:/", view);
		assertNotNull(informeService.findInformeById(informe.getId()).get());

		informeService.deleteInformeToHistoriaClinica(informe);
		informeService.deleteInforme(informe.getId());
		citaService.delete(cita);
	}

	@Test
	void testBorrarInformeCantDeletePastHC() throws Exception {
		Informe informe = informeService.findInformeById(3).get();
		Cita pastCita = informe.getCita();
		informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(pastCita.getPaciente()));
		informeService.saveInformeWithHistoriaClinica(informe);

		ModelMap model = new ModelMap();
		String view = informeController.borrarInforme(informe.getId(), model);

		assertEquals("redirect:/", view);
		assertNotNull(informeService.findInformeById(informe.getId()).get());

		informeService.deleteInformeToHistoriaClinica(informe);
	}

	@Test
	void testShowInforme() throws Exception {

		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		// Cita con LocalDate now
		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);

		ModelAndView mav = informeController.showInforme(informe.getId());

		assertEquals("informes/informeDetails", mav.getViewName());
		assertEquals(mav.getModel().get("cannotbedeleted"), false);
		assertEquals(mav.getModel().get("canbeedited"), true);

		informeService.deleteInforme(informe.getId());
		citaService.delete(cita);
	}

	@Test
	void testShowInformeDifferentDate() throws Exception {
		Informe informe = informeService.findInformeById(3).get();

		ModelAndView mav = informeController.showInforme(informe.getId());

		assertEquals("informes/informeDetails", mav.getViewName());
		assertEquals(mav.getModel().get("cannotbedeleted"), true);
		assertEquals(mav.getModel().get("canbeedited"), false);
	}

	@Test
	void testShowInformeWithHCPast() throws Exception {
		Informe informe = informeService.findInformeById(3).get();
		Cita pastCita = informe.getCita();
		informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(pastCita.getPaciente()));
		informeService.saveInformeWithHistoriaClinica(informe);

		ModelAndView mav = informeController.showInforme(informe.getId());

		assertEquals("informes/informeDetails", mav.getViewName());
		assertEquals(mav.getModel().get("cannotbedeleted"), true);
		assertEquals(mav.getModel().get("canbeedited"), false);

	}

	@Test
	void testShowInformeWithHC() throws Exception {

		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);

		informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(cita.getPaciente()));
		informeService.saveInformeWithHistoriaClinica(informe);

		ModelAndView mav = informeController.showInforme(informe.getId());

		assertEquals("informes/informeDetails", mav.getViewName());
		assertEquals(mav.getModel().get("cannotbedeleted"), true);
		assertEquals(mav.getModel().get("canbeedited"), true);
		assertNotNull(mav.getModel().get("informe"));

		informeService.deleteInformeToHistoriaClinica(informe);
		informeService.deleteInforme(informe.getId());
		citaService.delete(cita);

	}

	@Test
	void testAddHistoriaClinicaToInforme() throws Exception {

		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();

		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);

		informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(cita.getPaciente()));

		String view = informeController.addHistoriaClinicaToInforme(informe.getId());

		assertEquals("redirect:/pacientes/" + cita.getPaciente().getId() + "/historiaclinica", view);
		assertNotNull(informeService.findInformeById(informe.getId()).get().getHistoriaClinica());

		informeService.deleteInformeToHistoriaClinica(informe);
		informeService.deleteInforme(informe.getId());
		citaService.delete(cita);
	}

	@Test
	void testDeleteHistoriaClinicaToInforme() throws Exception {
		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		cita.setLugar("Lugar");
		cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
		citaService.save(cita);

		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setDiagnostico("Diag");
		informe.setMotivo_consulta("motivo");
		informeService.saveInforme(informe);

		informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(cita.getPaciente()));
		informeService.saveInformeWithHistoriaClinica(informe);

		String view = informeController.deleteFromHistoriaClinica(informe.getId());

		assertEquals("redirect:/citas/" + cita.getPaciente().getMedico().getId() + "/informes/" + informe.getId(),
				view);
		assertNull(informeService.findInformeById(informe.getId()).get().getHistoriaClinica());

		informeService.deleteInformeToHistoriaClinica(informe);
		informeService.deleteInforme(informe.getId());
		citaService.delete(cita);

	}

}