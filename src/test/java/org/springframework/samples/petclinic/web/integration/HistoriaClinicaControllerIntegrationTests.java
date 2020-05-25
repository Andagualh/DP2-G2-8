//
//package org.springframework.samples.petclinic.web.integration;
//
//import java.util.Collections;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.samples.petclinic.model.HistoriaClinica;
//import org.springframework.samples.petclinic.model.Paciente;
//import org.springframework.samples.petclinic.service.HistoriaClinicaService;
//import org.springframework.samples.petclinic.service.PacienteService;
//import org.springframework.samples.petclinic.web.HistoriaClinicaController;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.MapBindingResult;
//import org.springframework.web.servlet.ModelAndView;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//
//public class HistoriaClinicaControllerIntegrationTests {
//
//	private static final int			TEST_PACIENTESINHISTORIA_ID	= 7;
//	private static final int			TEST_PACIENTECONHISTORIA_ID	= 8;
//	private static final int			TEST_PACIENTESHOW_ID		= 6;
//
//	@Autowired
//	private HistoriaClinicaController	historiaController;
//
//	@Autowired
//	private HistoriaClinicaService		historiaService;
//
//	@Autowired
//	private PacienteService				pacienteService;
//
//
//	@Test
//	void testShowHistoriaClinica() throws Exception {
//		ModelAndView view = this.historiaController.showHistoriaClinica(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTESHOW_ID);
//
//		Assertions.assertEquals(view.getViewName(), "pacientes/historiaClinicaDetails");
//		Assertions.assertNotNull(view.getModel().get("historiaclinica"));
//		Assertions.assertNotNull(view.getModel().get("paciente"));
//	}
//
//	@Test
//	void testInitCreationForm() throws Exception {
//		ModelMap model = new ModelMap();
//
//		String view = this.historiaController.initCreationForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTESINHISTORIA_ID, model);
//
//		Assertions.assertEquals(view, "pacientes/createOrUpdateHistoriaClinicaForm");
//		Assertions.assertNotNull(model.get("historiaclinica"));
//		Assertions.assertNotNull(model.get("paciente"));
//	}
//
//	@Test
//	void testProcessCreationFormSuccess() throws Exception {
//		ModelMap model = new ModelMap();
//		HistoriaClinica hc = new HistoriaClinica();
//		hc.setDescripcion("Depresion");
//		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
//
//		String view = this.historiaController.processCreationForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTESINHISTORIA_ID, hc, bindingResult, model);
//
//		Assertions.assertEquals(view, "redirect:/pacientes/{pacienteId}");
//	}
//
//	//La descripcion no puede estar vacia
//	@Test
//	void testProcessCreationFormHasErrors() throws Exception {
//		ModelMap model = new ModelMap();
//		HistoriaClinica hc = new HistoriaClinica();
//		hc.setDescripcion("");
//		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
//
//		String view = this.historiaController.processCreationForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTESINHISTORIA_ID, hc, bindingResult, model);
//
//		Assertions.assertEquals(view, "pacientes/createOrUpdateHistoriaClinicaForm");
//	}
//
//	//El paciente ya tiene historia clinica
//	@Test
//	void testProcessCreationFormFailure() throws Exception {
//		ModelMap model = new ModelMap();
//		HistoriaClinica hc = new HistoriaClinica();
//		hc.setDescripcion("Depresion");
//		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
//
//		String view = this.historiaController.processCreationForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID, hc, bindingResult, model);
//
//		Assertions.assertEquals(view, "redirect:/oups");
//	}
//
//	@Test
//	void testInitUpdateForm() throws Exception {
//		ModelMap model = new ModelMap();
//
//		String view = this.historiaController.initUpdateForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID, model);
//
//		Assertions.assertEquals(view, "pacientes/createOrUpdateHistoriaClinicaForm");
//		Assertions.assertNotNull(model.get("historiaclinica"));
//		Assertions.assertNotNull(model.get("paciente"));
//	}
//
//	@Test
//	void testProcessUpdateFormSucces() throws Exception {
//		ModelMap model = new ModelMap();
//		Paciente paciente = this.pacienteService.findPacienteById(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID).get();
//		HistoriaClinica hc = this.historiaService.findHistoriaClinicaByPacienteId(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID);
//		hc.setDescripcion("Depresion modificada");
//		hc.setPaciente(paciente);
//		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
//
//		String view = this.historiaController.processUpdateHistoriaClinicaForm(hc, bindingResult, HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID, model);
//
//		Assertions.assertEquals(view, "redirect:/pacientes/{pacienteId}");
//
//	}
//
//	@Test
//	void testProcessUpdateFormHasErrors() throws Exception {
//		ModelMap model = new ModelMap();
//		Paciente paciente = this.pacienteService.findPacienteById(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID).get();
//		HistoriaClinica hc = this.historiaService.findHistoriaClinicaByPacienteId(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID);
//		hc.setDescripcion("");
//		hc.setPaciente(paciente);
//		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
//
//		String view = this.historiaController.processUpdateHistoriaClinicaForm(hc, bindingResult, HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID, model);
//
//		Assertions.assertEquals(view, "pacientes/createOrUpdateHistoriaClinicaForm");
//
//	}
//}
