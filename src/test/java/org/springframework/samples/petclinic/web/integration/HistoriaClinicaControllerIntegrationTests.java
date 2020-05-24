
package org.springframework.samples.petclinic.web.integration;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.web.HistoriaClinicaController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class HistoriaClinicaControllerIntegrationTests {

	private static final int			TEST_PACIENTESINHISTORIA_ID	= 7;
	private static final int			TEST_PACIENTECONHISTORIA_ID	= 8;
	private static final int			TEST_PACIENTESHOW_ID		= 6;

	@Autowired
	private HistoriaClinicaController	historiaController;

	@Autowired
	private HistoriaClinicaService		historiaService;

	@Autowired
	private PacienteService				pacienteService;


//	@Test
	void testShowHistoriaClinica() throws Exception {
		Medico medico = new Medico();
		medico.setId(1);
		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345672Z");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");

		User medicoUser = new User();
		medicoUser.setUsername("medico");
		medicoUser.setPassword("medico1");
		medicoUser.setEnabled(true);

		medico.setUser(medicoUser);
		medico.getUser().setEnabled(true);

		Authorities authorities = new Authorities();
		authorities.setUsername("medico");
		authorities.setAuthority("medico");

		Paciente pepe = new Paciente();
		pepe.setId(TEST_PACIENTESHOW_ID);
		pepe.setNombre("Pepe");
		pepe.setApellidos("Rodriguez");
		pepe.setF_nacimiento(LocalDate.of(1996, 2, 8));
		pepe.setDNI("12345671Z");
		pepe.setDomicilio("Ecija");
		pepe.setN_telefono(615345987);
		pepe.setEmail("pepeloa@gmail.com");
		pepe.setF_alta(LocalDate.now());
		pepe.setMedico(medico);
		
		ModelAndView view = this.historiaController.showHistoriaClinica(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTESHOW_ID);

		Assertions.assertEquals(view.getViewName(), "pacientes/historiaClinicaDetails");
		Assertions.assertNotNull(view.getModel().get("historiaclinica"));
		Assertions.assertNotNull(view.getModel().get("paciente"));
	}

	@Test
	void testInitCreationForm() throws Exception {
		ModelMap model = new ModelMap();

		String view = this.historiaController.initCreationForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTESINHISTORIA_ID, model);

		Assertions.assertEquals(view, "pacientes/createOrUpdateHistoriaClinicaForm");
		Assertions.assertNotNull(model.get("historiaclinica"));
		Assertions.assertNotNull(model.get("paciente"));
	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		HistoriaClinica hc = new HistoriaClinica();
		hc.setDescripcion("Depresion");
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		String view = this.historiaController.processCreationForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTESINHISTORIA_ID, hc, bindingResult, model);

		Assertions.assertEquals(view, "redirect:/pacientes/{pacienteId}");
	}

	//La descripcion no puede estar vacia
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		HistoriaClinica hc = new HistoriaClinica();
		hc.setDescripcion("");
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		String view = this.historiaController.processCreationForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTESINHISTORIA_ID, hc, bindingResult, model);

		Assertions.assertEquals(view, "pacientes/createOrUpdateHistoriaClinicaForm");
	}

	//El paciente ya tiene historia clinica
	@Test
	void testProcessCreationFormFailure() throws Exception {
		ModelMap model = new ModelMap();
		HistoriaClinica hc = new HistoriaClinica();
		hc.setDescripcion("Depresion");
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		String view = this.historiaController.processCreationForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID, hc, bindingResult, model);

		Assertions.assertEquals(view, "redirect:/oups");
	}

	@Test
	void testInitUpdateForm() throws Exception {
		ModelMap model = new ModelMap();

		String view = this.historiaController.initUpdateForm(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID, model);

		Assertions.assertEquals(view, "pacientes/createOrUpdateHistoriaClinicaForm");
		Assertions.assertNotNull(model.get("historiaclinica"));
		Assertions.assertNotNull(model.get("paciente"));
	}

	@Test
	void testProcessUpdateFormSucces() throws Exception {
		ModelMap model = new ModelMap();
		Paciente paciente = this.pacienteService.findPacienteById(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID).get();
		HistoriaClinica hc = this.historiaService.findHistoriaClinicaByPacienteId(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID);
		hc.setDescripcion("Depresion modificada");
		hc.setPaciente(paciente);
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		String view = this.historiaController.processUpdateHistoriaClinicaForm(hc, bindingResult, HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID, model);

		Assertions.assertEquals(view, "redirect:/pacientes/{pacienteId}");

	}

	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		Paciente paciente = this.pacienteService.findPacienteById(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID).get();
		HistoriaClinica hc = this.historiaService.findHistoriaClinicaByPacienteId(HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID);
		hc.setDescripcion("");
		hc.setPaciente(paciente);
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");

		String view = this.historiaController.processUpdateHistoriaClinicaForm(hc, bindingResult, HistoriaClinicaControllerIntegrationTests.TEST_PACIENTECONHISTORIA_ID, model);

		Assertions.assertEquals(view, "pacientes/createOrUpdateHistoriaClinicaForm");

	}
}
