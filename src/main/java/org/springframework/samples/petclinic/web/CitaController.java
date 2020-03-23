
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/citas")
public class CitaController {

	@Autowired
	private CitaService		citaService;
	@Autowired
	private PacienteService	pacienteService;


	//Esto muestra todas las citas pero deberia mostrar solo las del medico.
	@GetMapping()
	public String listadoCitas(ModelMap modelMap) {
		String vista = "citas/citasList";
		Iterable<Cita> citas = citaService.findAll();
		System.out.println(citas);
		modelMap.addAttribute("citas", citas);
		return vista;
	}
	
	@GetMapping(path="/new/{pacienteId}")
	public String crearCita(Integer pacienteId, ModelMap modelMap) {
		String view = "citas/createOrUpdateCitaForm";
		Cita cita = new Cita();
		Paciente paciente = pacienteService.findPacienteById(pacienteId);
		cita.setPaciente(paciente);
		modelMap.addAttribute("cita",cita);
		return view;
	}
	
	@PostMapping(path="/save")
	public String salvarCita(@Valid Cita cita,BindingResult result, ModelMap modelMap) {
		String view ="citas/createOrUpdateCitaForm";
		if(result.hasErrors()) {
			modelMap.addAttribute("cita", cita);
			return "citas/createOrUpdateCitaForm";
		}else {
			citaService.save(cita);
			modelMap.addAttribute("messsage","Cita successfully created");
		}
		return view;
	}
	
	@GetMapping(path="/delete/{citaId}")
	public String borrarCita(@PathParam("citaId") Integer citaId,ModelMap modelMap) {
		String view = "citas/citasList";
		Optional<Cita> cita = citaService.findCitaById(citaId);
		if(cita.isPresent()) {
			citaService.delete(cita.get());
			modelMap.addAttribute("message", "Cita successfully deleted");
		} else {
			modelMap.addAttribute("message", "Cita not found");
		}
		return view;
	}
}
