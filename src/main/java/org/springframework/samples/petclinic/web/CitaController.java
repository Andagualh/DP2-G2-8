
package org.springframework.samples.petclinic.web;

import java.util.Collection;
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


	//Esto muestra todas las citas pero deberia mostrar solo las del medico.
	@GetMapping()
	public String listadoCitas(final ModelMap modelMap) {
		String vista = "citas/citasList";
		Iterable<Cita> citas = this.citaService.findAll();
		System.out.println(citas);
		modelMap.addAttribute("citas", citas);
		return vista;

	
	
	//CUANDO ALGUIEN HAGA EL CITADETAILS POR FAVOR QUE USE ESTA URL EN EL MAPPING: "/citas/citaDetails/{citaId}" 
	
	@GetMapping(path="/{medicoId}")
	public String listadoCitas(ModelMap modelMap, @PathVariable("medicoId") int medicoId) {
		String vista = "citas/listCitas";
		Collection<Cita> citas = citaService.findCitasByMedicoId(medicoId);
		if (citas.isEmpty()) {
			return "redirect:/";
		}else {
			modelMap.put("selections", citas);
			return vista;
		}

	}

	@GetMapping(path = "/new/{pacienteId}")
	public String crearCita(final Integer pacienteId, final ModelMap modelMap) {
		String view = "citas/createOrUpdateCitaForm";
		Cita cita = new Cita();
		Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();
		cita.setPaciente(paciente);
		modelMap.addAttribute("cita", cita);
		return view;
	}

	@PostMapping(path = "/save")
	public String salvarCita(@Valid final Cita cita, final BindingResult result, final ModelMap modelMap) {
		String view = "citas/createOrUpdateCitaForm";
		if (result.hasErrors()) {
			modelMap.addAttribute("cita", cita);
			return "citas/createOrUpdateCitaForm";
		} else {
			this.citaService.save(cita);
			modelMap.addAttribute("messsage", "Cita successfully created");
		}
		return view;
	}

	@GetMapping(path = "/delete/{citaId}")
	public String borrarCita(@PathParam("citaId") final Integer citaId, final ModelMap modelMap) {
		String view = "citas/citasList";
		Optional<Cita> cita = this.citaService.findCitaById(citaId);
		if (cita.isPresent()) {
			this.citaService.delete(cita.get());
			modelMap.addAttribute("message", "Cita successfully deleted");
		} else {
			modelMap.addAttribute("message", "Cita not found");
		}
		return view;
	}
}
