package org.springframework.samples.petclinic.web.integration;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.PacienteController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sun.javafx.collections.MappingChange.Map;

import junit.framework.Assert;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class PacienteControllerIntegrationTest {
	
	@Autowired
	private PacienteController pacienteController;
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private UserService userService;
	@Autowired
	private MedicoService medicoService;
	@Autowired
	private CitaService citaService;
	
	private static final int TEST_PACIENTE_ID = 1;
	private static final String VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM = "pacientes/createOrUpdatePacientesForm";

	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testShowPaciente() throws Exception{
		
		ModelAndView mav = this.pacienteController.showPaciente(TEST_PACIENTE_ID);
		
		Assertions.assertEquals(mav.getViewName(), "pacientes/pacienteDetails");
		Assertions.assertNotNull(mav.getModel().get("paciente"));
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testShowPacientePlus5Years() throws Exception{
		
		ModelAndView mav = this.pacienteController.showPaciente(8);
		
		Assertions.assertEquals(mav.getViewName(), "pacientes/pacienteDetails");
		Assertions.assertNotNull(mav.getModel().get("paciente"));
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testShowPacientePlus6Years() throws Exception{
		
		ModelAndView mav = this.pacienteController.showPaciente(9);
		
		Assertions.assertEquals(mav.getViewName(), "pacientes/pacienteDetails");
		Assertions.assertNotNull(mav.getModel().get("paciente"));
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testShowPacienteCitasEmpty() throws Exception{
		
		ModelAndView mav = this.pacienteController.showPaciente(7);
		
		Assertions.assertEquals(mav.getViewName(), "pacientes/pacienteDetails");
		Assertions.assertNotNull(mav.getModel().get("paciente"));
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testInitFindForm() throws Exception{
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.initFindForm(model);
		
		Assertions.assertEquals("pacientes/findPacientes", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessFindFormApellidosVacio() throws Exception{
		
		Paciente p = new Paciente();
		p.setNombre("Laura");
		p.setApellidos("");
		p.setDNI("53279183M");
		p.setDomicilio("Calle la Calle, 45");
		p.setEmail("lauracoletatoro@gmail.com");
		p.setN_telefono(555444333);
		p.setF_alta(LocalDate.now());
		p.setF_nacimiento(LocalDate.of(1990, 3, 23));
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.processFindForm(p, result, model);
		
		Assertions.assertEquals(view, "pacientes/pacientesList");
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessFindFormApellidosNull() throws Exception{
		
		Paciente p = new Paciente();
		p.setNombre("Laura");
		p.setApellidos(null);
		p.setDNI("53279183M");
		p.setDomicilio("Calle la Calle, 45");
		p.setEmail("lauracoletatoro@gmail.com");
		p.setN_telefono(555444333);
		p.setF_alta(LocalDate.now());
		p.setF_nacimiento(LocalDate.of(1990, 3, 23));
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.processFindForm(p, result, model);
		
		Assertions.assertEquals(view, "pacientes/pacientesList");
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessFindFormApellidosNoExiste() throws Exception{
		
		Paciente p = new Paciente();
		p.setNombre("Laura");
		p.setApellidos("Coleta Toro");
		p.setDNI("53279183M");
		p.setDomicilio("Calle la Calle, 45");
		p.setEmail("lauracoletatoro@gmail.com");
		p.setN_telefono(555444333);
		p.setF_alta(LocalDate.now());
		p.setF_nacimiento(LocalDate.of(1990, 3, 23));
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.processFindForm(p, result, model);
		
		Assertions.assertEquals("pacientes/findPacientes", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testSetAllowedFields() throws Exception {
		WebDataBinder wDB = new WebDataBinder(null);
		this.pacienteController.setAllowedFields(wDB);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessFindFormApellidosVarios() throws Exception{
		
		Paciente paciente = this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		paciente.setApellidos("Salas Sala");
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.processFindForm(paciente, result, model);
	
		Assertions.assertEquals(view, "pacientes/pacientesList");
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessFindFormApellidosUnicos() throws Exception{
		
		Paciente paciente = this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.processFindForm(paciente, result, model);
		
		Assertions.assertEquals(view, "redirect:/pacientes/" + paciente.getId());
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testInitFindMedForm() throws Exception{
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		String view = this.pacienteController.initFindMedForm(model);
		
		Assertions.assertEquals("redirect:/pacientes/findByMedico/1", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessFindMedFormEmpty() throws Exception {
		TreeMap<String, Object> model = new TreeMap<String, Object>();

		String view = this.pacienteController.processFindMedForm(model, 4);
		
		Assertions.assertEquals("redirect:/pacientes/", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessFindMedForm() throws Exception {
		TreeMap<String, Object> model = new TreeMap<String, Object>();

		String view = this.pacienteController.processFindMedForm(model, 1);
		
		Assertions.assertEquals("pacientes/pacientesListMedico", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testBorrarPacienteNoPuedeBorrarse() throws Exception {
		ModelMap m = new ModelMap();
		String view = this.pacienteController.borrarPaciente(1, m);
		Assertions.assertEquals("redirect:/pacientes/1", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testBorrarPacienteWrongMedico() throws Exception {
		int id = 8;
		ModelMap m = new ModelMap();
		String view = this.pacienteController.borrarPaciente(id, m);
		Assertions.assertEquals("/pacientes/" + id, view);
	}
	
	@WithMockUser(username="pabloMedico", authorities={"medico"})
	@Test
	void testBorrarPacienteSuccess() throws Exception {
		int id = 8;
		ModelMap m = new ModelMap();
		String view = this.pacienteController.borrarPaciente(id, m);
		Assertions.assertEquals("redirect:/pacientes", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testBorrarPacientePlus6Years() throws Exception {
		int id = 9;
		ModelMap m = new ModelMap();
		String view = this.pacienteController.borrarPaciente(id, m);
		Assertions.assertEquals("redirect:/pacientes", view);
	}
	
	@WithMockUser(username="pabloMedico", authorities={"medico"})
	@Test
	void testBorrarPacienteCitasEmpty() throws Exception {
		int id = 7;
		ModelMap m = new ModelMap();
		String view = this.pacienteController.borrarPaciente(id, m);
		Assertions.assertEquals("redirect:/pacientes", view);
	}
	
	@WithMockUser(username="andresMedico", authorities={"medico"})
	@Test
	void testBorrarPacienteUltimaCitaMayorQueHoy() throws Exception {
		int id = 6;
		ModelMap m = new ModelMap();
		String view = this.pacienteController.borrarPaciente(id, m);
		Assertions.assertEquals("redirect:/pacientes/" + id, view);
	}
	
	@WithMockUser(username="andresMedico", authorities={"medico"})
	@Test
	void testBorrarPacienteMedicoDisabled() throws Exception {
		this.medicoService.getMedicoById(3).getUser().setEnabled(false);
		int id = 6;
		ModelMap m = new ModelMap();
		String view = this.pacienteController.borrarPaciente(id, m);
		Assertions.assertEquals("redirect:/pacientes/" + id, view);
		this.medicoService.getMedicoById(3).getUser().setEnabled(true);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testBorrarPacienteMedicoDisabledCantDelete() throws Exception {
		this.medicoService.getMedicoById(3).getUser().setEnabled(false);
		int id = 1;
		ModelMap m = new ModelMap();
		String view = this.pacienteController.borrarPaciente(id, m);
		Assertions.assertEquals("redirect:/pacientes/" + id, view);
		this.medicoService.getMedicoById(3).getUser().setEnabled(true);
	}
	
	@WithMockUser(username="pabloMedico", authorities={"medico"})
	@Test
	void testBorrarPacienteNotFound() throws Exception {
		int id = 20;
		ModelMap m = new ModelMap();
		this.pacienteController.borrarPaciente(id, m);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testInitUpdatePacienteFormSuccess() throws Exception {
		
		ModelMap m = new ModelMap();
		String view = this.pacienteController.initUpdatePacientesForm(TEST_PACIENTE_ID, m);
		
		Assertions.assertEquals(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM, view);;
	}
	
	@WithMockUser(username="pabloMedico", authorities={"medico"})
	@Test
	void testInitUpdatePacienteFormWrongMedico() throws Exception {
		
		ModelMap m = new ModelMap();
		String view = this.pacienteController.initUpdatePacientesForm(TEST_PACIENTE_ID, m);
		
		Assertions.assertEquals("redirect:/pacientes/{pacienteId}", view);;
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessUpdatePacienteFormSuccess() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 34");
		p.setEmail("pacodelucia@gmail.com");
		p.setN_telefono(333555777);
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processUpdatePacienteForm(p, result, 1, m);
		
		Assertions.assertEquals("redirect:/pacientes/{pacienteId}", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessUpdatePacienteFormWrongTelefono() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("");
		p.setEmail("");
		p.setN_telefono(364);
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processUpdatePacienteForm(p, result, 1, m);
		
		Assertions.assertEquals(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM, view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessUpdatePacienteFormSinContacto() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("");
		p.setEmail("");
		p.setN_telefono(null);
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processUpdatePacienteForm(p, result, 1, m);
		
		Assertions.assertEquals(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM, view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessUpdatePacienteFormTelefonoNullWithDomicilioAndEmail() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 45");
		p.setEmail("correoejemplo@gmail.com");
		p.setN_telefono(null);
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processUpdatePacienteForm(p, result, 1, m);
		
		Assertions.assertEquals("redirect:/pacientes/{pacienteId}", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessUpdatePacienteFormTelefonoValidWithDomicilioNotEmail() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 45");
		p.setEmail("");
		p.setN_telefono(333555777);
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processUpdatePacienteForm(p, result, 1, m);
		
		Assertions.assertEquals("redirect:/pacientes/{pacienteId}", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessUpdatePacienteFormTelefonoNullWithDomicilioNotEmail() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 45");
		p.setEmail("");
		p.setN_telefono(null);
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processUpdatePacienteForm(p, result, 1, m);
		
		Assertions.assertEquals("redirect:/pacientes/{pacienteId}", view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessUpdatePacienteFormWrongDni() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("5327M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 34");
		p.setEmail("pacodelucia@gmail.com");
		p.setN_telefono(333555777);
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processUpdatePacienteForm(p, result, 1, m);
		
		Assertions.assertEquals(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM, view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testInitCreatePacienteForm() throws Exception {

		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.initCreationForm(model);
		
		Assertions.assertEquals(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM, view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessCreatePacienteFormSuccess() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 34");
		p.setEmail("pacodelucia@gmail.com");
		p.setN_telefono(333555777);
		p.setF_alta(LocalDate.now());
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		this.pacienteService.savePaciente(p);
		int id = p.getId();
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processCreationForm(p, result, m);
		
		Assertions.assertEquals("redirect:/pacientes/" + id, view);
		
		this.pacienteService.pacienteDelete(id);
	}
	
	@WithMockUser(username="pabloMedico", authorities={"medico"})
	@Test
	void testProcessCreatePacienteFormWrongMedico() throws Exception {

		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 34");
		p.setEmail("pacodelucia@gmail.com");
		p.setN_telefono(333555777);
		p.setF_alta(LocalDate.now());
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		this.pacienteService.savePaciente(p);
		int id = p.getId();
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processCreationForm(p, result, m);
		
		Assertions.assertEquals(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM, view);
		
		this.pacienteService.pacienteDelete(id);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessCreatePacienteFormWrongTelefono() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("");
		p.setEmail("");
		p.setN_telefono(364);
		p.setF_alta(LocalDate.now());
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processCreationForm(p, result, m);
		
		Assertions.assertEquals(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM, view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessCreatePacienteFormSinContacto() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("");
		p.setEmail("");
		p.setN_telefono(null);
		p.setF_alta(LocalDate.now());
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processCreationForm(p, result, m);
		
		Assertions.assertEquals(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM, view);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessCreatePacienteFormTelefonoNullWithDomicilioAndEmail() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 45");
		p.setEmail("correoejemplo@gmail.com");
		p.setN_telefono(null);
		p.setF_alta(LocalDate.now());
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		this.pacienteService.savePaciente(p);
		int id = p.getId();
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processCreationForm(p, result, m);
		
		Assertions.assertEquals("redirect:/pacientes/" + id, view);
		
		this.pacienteService.pacienteDelete(id);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessCreatePacienteFormTelefonoValidWithDomicilioNotEmail() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 45");
		p.setEmail("");
		p.setN_telefono(333555777);
		p.setF_alta(LocalDate.now());
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		this.pacienteService.savePaciente(p);
		int id = p.getId();
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processCreationForm(p, result, m);
		
		Assertions.assertEquals("redirect:/pacientes/" + id, view);
		
		this.pacienteService.pacienteDelete(id);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessCreatePacienteFormTelefonoNullWithDomicilioNotEmail() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("53279183M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 45");
		p.setEmail("");
		p.setN_telefono(null);
		p.setF_alta(LocalDate.now());
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		this.pacienteService.savePaciente(p);
		int id = p.getId();
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processCreationForm(p, result, m);
		
		Assertions.assertEquals("redirect:/pacientes/" + id, view);
		
		this.pacienteService.pacienteDelete(id);
	}
	
	@WithMockUser(username="alvaroMedico", authorities={"medico"})
	@Test
	void testProcessCreatePacienteFormWrongDni() throws Exception {

		
		Paciente p = new Paciente();
		p.setNombre("Paco");
		p.setApellidos("De Lucía");
		p.setDNI("5327M");
		p.setF_nacimiento(LocalDate.now().minusYears(25));
		p.setDomicilio("La calle de su casa, 34");
		p.setEmail("pacodelucia@gmail.com");
		p.setN_telefono(333555777);
		p.setF_alta(LocalDate.now());
		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		ModelMap m = new ModelMap();

		
		String view = this.pacienteController.processCreationForm(p, result, m);
		
		Assertions.assertEquals(VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM, view);
	}
	
	
}
