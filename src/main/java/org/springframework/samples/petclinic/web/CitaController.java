
package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
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
	public String listadoCitas(final ModelMap modelMap) {
		String vista = "citas/listCitas";
		Iterable<Cita> citas = this.citaService.findAll();
		modelMap.addAttribute("citas", citas);
		return vista;
	}

	@GetMapping(path = "/new/{pacienteId}")
	public String crearCita(final int pacienteId, final ModelMap modelMap) {
		String view = "citas/editCita";
		Cita cita = new Cita();
		cita.setPaciente(this.pacienteService.findPacienteById(pacienteId).get());
		modelMap.addAttribute("cita", new Cita());
		return view;
	}

	@PostMapping(path = "/save")
	public String salvarEvento(@Valid final Cita cita, final BindingResult result, final ModelMap modelMap) {
		String view = "citas/listCitas";
		if (result.hasErrors()) {
			modelMap.addAttribute("cita", cita);
			return "citas/editCita";
		} else {
			this.citaService.save(cita);
			modelMap.addAttribute("messsage", "Cita successfully created");
		}
		return view;
	}

	@GetMapping(path = "/delete/{citaId}")
	public String borrarCita(@PathParam("citaId") final int citaId, final ModelMap modelMap) {
		String view = "citas/listCitas";
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
