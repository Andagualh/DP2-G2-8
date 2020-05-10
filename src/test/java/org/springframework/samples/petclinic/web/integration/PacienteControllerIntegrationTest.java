package org.springframework.samples.petclinic.web.integration;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.PacienteController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
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
	
	@Test
	void testShowPaciente() throws Exception{
//		Paciente p = this.pacienteService.getPacienteById(TEST_PACIENTE_ID);
//		p.setMedico(this.medicoService.findMedicoByUsername("alvaroMedico"));
//		Cita cita = new Cita();
//		cita.setFecha(LocalDate.now());
//		cita.setLugar("Lugar");
//		cita.setPaciente(p);
//		this.citaService.save(cita);
		
		
		ModelAndView mav = this.pacienteController.showPaciente(TEST_PACIENTE_ID);
		
		Assertions.assertEquals(mav.getViewName(), "pacientes/pacienteDetails");
		Assertions.assertNotNull(mav.getModel().get("paciente"));
	}
	
	@Test
	void testInitFindForm() throws Exception{
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.initFindForm(model);
		
		Assertions.assertEquals("pacientes/findPacientes", view);
	}
	
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
	
	@Test
	void testProcessFindFormApellidosVarios() throws Exception{
		
		Paciente paciente = this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		paciente.setApellidos("Salas Sala");
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.processFindForm(paciente, result, model);
	
		Assertions.assertEquals(view, "pacientes/pacientesList");
	}
	
	@Test
	void testProcessFindFormApellidosUnicos() throws Exception{
		
		Paciente paciente = this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get();
		BindingResult result = new MapBindingResult(Collections.emptyMap(),"");
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		
		String view = this.pacienteController.processFindForm(paciente, result, model);
		
		Assertions.assertEquals(view, "redirect:/pacientes/" + paciente.getId());
	}
	
	@Test
	void testInitFindMedForm() throws Exception{
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		String view = this.pacienteController.initFindMedForm(model);
		
		Assertions.assertEquals("redirect:/pacientes/findByMedico/1", view);
	}
	
	@Test
	void testProcessFindMedFormEmpty() throws Exception {
		TreeMap<String, Object> model = new TreeMap<String, Object>();

		String view = this.pacienteController.processFindMedForm(model, 4);
		
		Assertions.assertEquals("redirect:/pacientes/", view);
	}
	
	@Test
	void testProcessFindMedForm() throws Exception {
		TreeMap<String, Object> model = new TreeMap<String, Object>();

		String view = this.pacienteController.processFindMedForm(model, 1);
		
		Assertions.assertEquals("pacientes/pacientesListMedico", view);
	}
	
	@Test
	void testBorrarPacienteSuccess() throws Exception {
		ModelMap m = new ModelMap();
		String view = this.pacienteController.borrarPaciente(1, m);
		Assertions.assertEquals("redirect:/pacientes", view);
	}
	
	//--------------------------------------
	
	@Test
	void testInitUpdatePacienteForm() throws Exception {
	
		
	}

	
	
	
	
	
}
