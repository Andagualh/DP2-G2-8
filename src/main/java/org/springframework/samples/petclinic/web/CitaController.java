
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;
import javax.validation.Valid;

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
	private CitaService citaService;
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private UserService userService;

	// CUANDO ALGUIEN HAGA EL CITADETAILS POR FAVOR QUE USE ESTA URL EN EL MAPPING:
	// "/citas/citaDetails/{citaId}"

	@GetMapping()
	public String initList(final ModelMap modelMap) {
		int idMedico = this.userService.getCurrentMedico().getId();
		return "redirect:/citas/" + idMedico;
	}

	@GetMapping(path = "/{medicoId}")
	public String listadoCitas(final ModelMap modelMap, @PathVariable("medicoId") final int medicoId) {
		String vista = "citas/listCitas";
		Collection<Cita> citas = this.citaService.findCitasByMedicoId(medicoId);
		if (citas.isEmpty()) {
			// TODO: Si no se han encontrado citas devuelve al index, pensad en una
			// alternativa a esto como un popup o pagina que indique no se ha encontrado
			return "redirect:/";
		} else {
			modelMap.put("selections", citas);
			modelMap.put("dateOfToday", LocalDate.now());
			return vista;
		}

	}

	@GetMapping(path = "/new/{pacienteId}")
	public String crearCita(@PathVariable("pacienteId") final int pacienteId, final ModelMap modelMap) {
		Cita cita = new Cita();
		Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();
		// cita.setPaciente(paciente);
		// cita.setName("paciente");
		modelMap.addAttribute("paciente", paciente);
		modelMap.addAttribute("cita", cita);
		return "citas/createOrUpdateCitaForm";
	}

	@PostMapping(path = "/save")
	public String salvarCita(@Valid final Cita cita, final BindingResult result, final ModelMap modelMap)
			throws InvalidAttributeValueException {
		if (result.hasErrors()) {
			modelMap.addAttribute("cita", cita);
			modelMap.addAttribute("paciente", cita.getPaciente());
			return "citas/createOrUpdateCitaForm";
		} else if (cita.getFecha().isBefore(LocalDate.now())) {
			System.out.println("entraalerror");
			modelMap.addAttribute("cita", cita);
			modelMap.addAttribute("paciente", cita.getPaciente());
			modelMap.addAttribute("message", "La fecha debe estar en presente o futuro");
			return "citas/createOrUpdateCitaForm";
		} else {
			this.citaService.save(cita);
			modelMap.addAttribute("message", "Cita successfully created");
			return "redirect:/citas";
		}
	}

	@GetMapping(path = "/delete/{citaId}")
	public String borrarCita(@PathVariable("citaId") final int citaId, final ModelMap modelMap) {

		Optional<Cita> cita = this.citaService.findCitaById(citaId);

		if (cita.isPresent() && cita.get().getInforme() == null) {
			modelMap.addAttribute("message", "Cita successfully deleted");
			this.citaService.delete(cita.get());
		} else {
			modelMap.addAttribute("message", "Cita cant be deleted");
		}
		return "redirect:/citas";
	}

	@GetMapping(value = "/find")
	public String initFindForm(final Map<String, Object> model) {
		Cita cita = new Cita();
		cita.setFecha(LocalDate.now());
		model.put("cita", cita);
		return "citas/findCitas";
	}

	// TODO: Falta el caso para una sola cita, que muestre directamente los
	// detalles, no se ha incluido porque genera conflicto con otro método
	// TODO: Cuando este hecho el details se incluye
	@GetMapping(value = "/porfecha")
	public String processFindForm(final Cita cita, final BindingResult result, final Map<String, Object> model) {

		// allow parameterless GET request for /citas to return all records
		if (cita.getFecha() == null) {
			cita.setFecha(LocalDate.now()); // empty string signifies broadest possible search
		}

		// find citas by fecha
		Collection<Cita> citas = this.citaService.findCitasByFecha(cita.getFecha());

		if (citas.isEmpty()) {
			// no citas found
			result.rejectValue("fecha", "notFound", "not found");
			return "citas/findCitas";
		} else {
			// multiple citas found
			model.put("selections", citas);
			return "citas/listCitas";
		}
	}

}
