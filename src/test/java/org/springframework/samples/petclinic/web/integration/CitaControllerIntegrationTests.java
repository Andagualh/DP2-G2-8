package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.web.CitaController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;



@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CitaControllerIntegrationTests {
	private static final int TEST_PACIENTE_ID = 1;

	private static final int TEST_CITA_ID = 1;

	private static final int TEST_MEDICO_ID = 1;

	@Autowired
	private CitaController citaController;
	@Autowired
	private CitaService citaService;
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private InformeService informeService;
	
	@Test
	void testInitCreationCita() throws Exception {
		Paciente paciente = pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		ModelMap model = new ModelMap();

		String view = citaController.crearCita(paciente.getId(), model);

		assertEquals(view, "citas/createOrUpdateCitaForm");
		assertNotNull(model.get("cita"));
	}

	@Test
	void testProcessCreationCitaSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Paciente paciente = pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		
		Cita newCita = new Cita();
		newCita.setFecha(LocalDate.now());
		newCita.setLugar("Hospital Virgen del Rocio, Sevilla");
		newCita.setPaciente(paciente);
		
		BindingResult bindingResult = new MapBindingResult(new HashMap(), "");
		
		String view = citaController.salvarCita(newCita, bindingResult, model);

		assertEquals(view, "redirect:/citas");
	}

	@Test
	void testProcessCreationCitaEmptyField() throws Exception {
		ModelMap model = new ModelMap();
		Paciente paciente = pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		
		Cita newCita = new Cita();
		newCita.setFecha(LocalDate.now().plusDays(1));
		newCita.setPaciente(paciente);
		
		BindingResult bindingResult = new MapBindingResult(new HashMap(), "");
		bindingResult.reject("lugar", "no puede estar vacío");

		String view = citaController.salvarCita(newCita, bindingResult, model);

		assertEquals(view, "citas/createOrUpdateCitaForm");
	}
	
	@Test
	void testProcessCreationCitaDateInPast() throws Exception {
		ModelMap model = new ModelMap();
		Paciente paciente = pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		
		Cita newCita = new Cita();
		newCita.setFecha(LocalDate.now().minusDays(1));
		newCita.setLugar("Hospital Virgen del Rocio, Sevilla");
		newCita.setPaciente(paciente);
		
		BindingResult bindingResult = new MapBindingResult(new HashMap(), "");
		bindingResult.reject("fecha", "tiene que ser una fecha en el presente o en el futuro");

		String view = citaController.salvarCita(newCita, bindingResult, model);

		assertEquals(view, "citas/createOrUpdateCitaForm");
	}
	
	@Test
	void testInitFindCita() throws Exception {
		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		
		ModelMap model = new ModelMap();
		model.put("cita", cita);
		
		String view = citaController.initFindForm(model);

		assertEquals(view, "citas/findCitas");
		assertNotNull(model.get("cita"));
	}
	
	@Test
	void testProcessFindCita() throws Exception {
		ModelMap model = new ModelMap();
		
		Cita newCita = new Cita();
		newCita.setFecha(LocalDate.of(2023, 5, 9));

		BindingResult bindingResult = new MapBindingResult(new HashMap(), "");
//		bindingResult.reject("fecha", "tiene que ser una fecha en el presente o en el futuro");

		String view = citaController.processFindForm(newCita, bindingResult, model);

		assertEquals(view, "citas/listCitas");
	}
	
	@Test
	void testProcessFindCitaNotFound() throws Exception {
		ModelMap model = new ModelMap();
		
		Cita newCita = new Cita();
		newCita.setFecha(LocalDate.of(2019, 1, 1));

		BindingResult bindingResult = new MapBindingResult(new HashMap(), "");
		bindingResult.reject("fecha", "not found");

		String view = citaController.processFindForm(newCita, bindingResult, model);

		assertEquals(view, "citas/findCitas");
	}
	
	@Test
	void testInitFindMyCitas() throws Exception {
		ModelMap model = new ModelMap();
		
		String view = citaController.initFindForm(model);

		assertEquals(view, "citas/findCitas");
	}
		
	@Test
	void testProcessFindMyCitas() throws Exception {
		ModelMap model = new ModelMap();
		
		String view = citaController.listadoCitas(model,TEST_MEDICO_ID);

		assertEquals(view, "citas/listCitas");
		assertNotNull(model.get("selections"));
		assertNotNull(model.get("dateOfToday"));
	}
	
	@Test
	void testProcessFindMyCitasEmpty() throws Exception {
		ModelMap model = new ModelMap();
		int idMedicoWithoutCitas = 3;
		
		String view = citaController.listadoCitas(model,idMedicoWithoutCitas);

		assertEquals(view, "redirect:/");
	}
	
	@Test
	void testBorrarCita() throws Exception {
		Paciente paciente = pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		
		Cita newCita = new Cita();
		newCita.setFecha(LocalDate.now());
		newCita.setLugar("Hospital Virgen del Rocio, Sevilla");
		newCita.setPaciente(paciente);
		int idCitaABorrar = this.citaService.save(newCita).getId();
		
		ModelMap model = new ModelMap();
		
		String view = citaController.borrarCita(idCitaABorrar, model);

		assertEquals(view, "redirect:/citas");
		assertEquals(model.get("message"), "Cita successfully deleted");

	}
	
	@Test
		void testBorrarCitaInPast() throws Exception {
		ModelMap model = new ModelMap();
		
		String view = citaController.borrarCita(TEST_CITA_ID, model);

		assertEquals(view, "redirect:/citas");
		assertEquals(model.get("message"), "Cita cant be deleted");

	}
	
	@Test
	void testBorrarCitaWithInforme() throws Exception {
		Paciente paciente = pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		
		Cita newCita = new Cita();
		newCita.setFecha(LocalDate.now());
		newCita.setLugar("Hospital Virgen del Rocio, Sevilla");
		newCita.setPaciente(paciente);
		
		int idCitaABorrar = this.citaService.save(newCita).getId();
		
		Informe informe = new Informe();
		informe.setMotivo_consulta("Resfriado comun");
		informe.setDiagnostico("Reposo");
		informe.setCita(this.citaService.findCitaById(idCitaABorrar).get());
		
		this.informeService.saveInforme(informe);
		
		ModelMap model = new ModelMap();
		
		String view = citaController.borrarCita(idCitaABorrar, model);

		assertEquals(view, "redirect:/citas");
		assertEquals(model.get("message"), "Cita cant be deleted");

	}
}
