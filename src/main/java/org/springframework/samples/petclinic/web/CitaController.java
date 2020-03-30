
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;

import org.springframework.samples.petclinic.model.Paciente;

import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/citas")
public class CitaController {

	@Autowired
	private CitaService		citaService;
	@Autowired
	private PacienteService	pacienteService;
	@Autowired
	private UserService userService;


	
	//CUANDO ALGUIEN HAGA EL CITADETAILS POR FAVOR QUE USE ESTA URL EN EL MAPPING: "/citas/citaDetails/{citaId}" 
	
	@GetMapping()
	public String initList(ModelMap modelMap){
		int idMedico = this.userService.getCurrentMedico().getId();
		return "redirect:/citas/" + idMedico;
	}

	@GetMapping(path="/{medicoId}")
	public String listadoCitas(ModelMap modelMap, @PathVariable("medicoId") int medicoId) {
		String vista = "citas/listCitas";
		Collection<Cita> citas = citaService.findCitasByMedicoId(medicoId);
		if (citas.isEmpty()) {
			//TODO: Si no se han encontrado citas devuelve al index, pensad en una alternativa a esto como un popup o pagina que indique no se ha encontrado
			return "redirect:/";
		}else {
			modelMap.put("selections", citas);
			return vista;
		}

	}

	@GetMapping(path ="/new/{pacienteId}")
	public String crearCita(@PathVariable("pacienteId") int pacienteId, final ModelMap modelMap) {
		Cita cita = new Cita();
		Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();
		//cita.setPaciente(paciente);
		//cita.setName("paciente");
		modelMap.addAttribute("paciente",paciente);
		modelMap.addAttribute("cita", cita);
		return "citas/createOrUpdateCitaForm";
	}

	@PostMapping(path ="/save")
	public String salvarCita(@Valid final Cita cita, final BindingResult result, final ModelMap modelMap) {
		
		if (result.hasErrors()) {
			modelMap.addAttribute("cita", cita);
			return "citas/createOrUpdateCitaForm";
		} else {
			Paciente paciente = pacienteService.findPacienteById(cita.getPaciente().getId()).get();
			cita.setPaciente(paciente);
			this.citaService.save(cita);
			modelMap.addAttribute("message", "Cita successfully created");
			return "redirect:/citas";
		}
	}

	@GetMapping(path = "/delete/{citaId}")
	public String borrarCita(@PathVariable("citaId") int citaId, final ModelMap modelMap) {
		
		Optional<Cita> cita = this.citaService.findCitaById(citaId);
		
		if (cita.isPresent()) {
			this.citaService.delete(cita.get());
			modelMap.addAttribute("message", "Cita successfully deleted");
		} else {
			modelMap.addAttribute("message", "Cita not found");
		}
		return "redirect:/citas";
	}

	@GetMapping(value = "/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("cita", new Cita());
		return "citas/findCitas";
	}

	//TODO: Falta el caso para una sola cita, que muestre directamente los detalles, no se ha incluido porque genera conflicto con otro método
	//TODO: Cuando este hecho el details se incluye	
	@GetMapping(value = "/porfecha")
	public String processFindForm(Cita cita, BindingResult result, Map<String, Object> model) {

		// allow parameterless GET request for /citas to return all records
		if (cita.getFecha() == null) {
			cita.setFecha(LocalDate.now()); // empty string signifies broadest possible search
		}

		// find citas by fecha
		Collection<Cita> results = this.citaService.findCitasByFecha(cita.getFecha());
		if (results.isEmpty()) {
			// no citas found
			result.rejectValue("fecha", "notFound", "not found");
			return "citas/findCitas";
		}
		else {
			// multiple citas found
			model.put("selections", results);
			return "citas/listCitas";
		}
	}

}
