
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Medico;
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

	
	private static final String ACCESS_NOT_AUTHORIZED = "accessNotAuthorized";
	private static final String PACIENTE_MODEL_NAME = "paciente";
	private static final String VIEW_CREATE_OR_UPDATE_FORM = "citas/createOrUpdateCitaForm";
	private static final String MESSAGE = "message";
	private static final String FECHA = "fecha";
	private static final String VIEW_CITAS = "redirect:/citas";
	
	
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
		int idMedico = this.userService.getCurrentMedico().getId();
	

		if (medicoId != idMedico) {
			return ACCESS_NOT_AUTHORIZED;
		} else {
			Collection<Cita> citas = this.citaService.findCitasByMedicoId(medicoId);
			if (citas.isEmpty()) {
				return "redirect:/";
			} else {
				
				modelMap.put("selections", citas);
				modelMap.put("dateOfToday", LocalDate.now());
				return vista;
			}
		}

	}

	@GetMapping(path = "/new/{pacienteId}")
	public String crearCita(@PathVariable("pacienteId") final int pacienteId, final ModelMap modelMap) {
		Cita cita = new Cita();
		Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();
		Medico medic = this.userService.getCurrentMedico();
		// cita.setPaciente(paciente);
		// cita.setName("paciente");

		if (paciente.getMedico().equals(medic)) {
			modelMap.addAttribute(PACIENTE_MODEL_NAME, paciente);
			modelMap.addAttribute("cita", cita);
			return VIEW_CREATE_OR_UPDATE_FORM;
		} else {
			return ACCESS_NOT_AUTHORIZED;
		}
	}

	@PostMapping(path = "/save")
	public String salvarCita(@Valid final Cita cita, final BindingResult result, final ModelMap modelMap)
			throws InvalidAttributeValueException {

		if (result.hasErrors()) {
			modelMap.addAttribute("cita", cita);
			modelMap.addAttribute(PACIENTE_MODEL_NAME, cita.getPaciente());
			return VIEW_CREATE_OR_UPDATE_FORM;
		} else if (cita.getFecha().isBefore(LocalDate.now())) {
			modelMap.addAttribute("cita", cita);
			modelMap.addAttribute(PACIENTE_MODEL_NAME, cita.getPaciente());
			modelMap.addAttribute(MESSAGE, "La fecha debe estar en presente o futuro");
			result.rejectValue(FECHA, "error.fecha", "La fecha debe estar en presente o futuro");
			return VIEW_CREATE_OR_UPDATE_FORM;
		} else if (this.citaService.existsCitaPacienteDate(cita.getFecha(), cita.getPaciente())) {
			modelMap.addAttribute("cita", cita);
			modelMap.addAttribute(PACIENTE_MODEL_NAME, cita.getPaciente());
			result.rejectValue(FECHA, "error.citasamedate", "Ya existe una cita para este paciente en esta fecha");
			return VIEW_CREATE_OR_UPDATE_FORM;
		} else {
			this.citaService.save(cita);
			modelMap.addAttribute(MESSAGE, "Cita successfully created");
			return VIEW_CITAS;
		}
	}

	@GetMapping(path = "/delete/{citaId}")
	public String borrarCita(@PathVariable("citaId") final int citaId, final ModelMap modelMap) {
		Medico actualMedic = this.userService.getCurrentMedico();
		Optional<Cita> cita = this.citaService.findCitaById(citaId);

		if (cita.isPresent() && cita.get().getInforme() == null
				&& actualMedic.equals(cita.get().getPaciente().getMedico()) && cita.get().getFecha() != null) {
			if (cita.get().getFecha().isAfter(LocalDate.now()) || cita.get().getFecha().isEqual(LocalDate.now())) {
				modelMap.addAttribute(MESSAGE, "Cita successfully deleted");
				this.citaService.delete(cita.get());

				return VIEW_CITAS;
			} else {
				return VIEW_CITAS;
			}
		} else if (cita.isPresent() && !actualMedic.equals(cita.get().getPaciente().getMedico())) {
			return ACCESS_NOT_AUTHORIZED;
		} else {
			modelMap.addAttribute(MESSAGE, "Cita cant be deleted");
			return VIEW_CITAS;
		}
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
		Medico medic = this.userService.getCurrentMedico();
		// allow parameterless GET request for /citas to return all records
		if (cita.getFecha() == null) {
			cita.setFecha(LocalDate.now()); // empty string signifies broadest possible search
		}

		// find citas by fecha
		Collection<Cita> citas = this.citaService.findCitasByFecha(cita.getFecha(), medic);

		if (citas.isEmpty()) {
			// no citas found
			result.rejectValue(FECHA, "error.citaNotFound", "No se encontró ninguna cita en el día introducido");
			return "citas/findCitas";
		} else {
			// multiple citas found
			model.put("selections", citas);
			model.put("dateOfToday", LocalDate.now());
			return "citas/listCitas";
		}
	}

}
