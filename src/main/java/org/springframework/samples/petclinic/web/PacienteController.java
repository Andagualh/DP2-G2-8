
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PacienteController {

	private static final String		VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM	= "pacientes/createOrUpdatePacientesForm";
	@Autowired
	private final PacienteService	pacienteService;
	private final MedicoService		medicoService;


	@Autowired
	public PacienteController(final PacienteService pacienteService, final MedicoService medicoService, final UserService userService, final AuthoritiesService authoritiesService) {
		this.pacienteService = pacienteService;
		this.medicoService = medicoService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/pacientes/{pacienteId}")
	public ModelAndView showPaciente(@PathVariable("pacienteId") final int pacienteId) {
		ModelAndView mav = new ModelAndView("pacientes/pacienteDetails");
		mav.addObject(this.pacienteService.findPacienteById(pacienteId).get());
		return mav;
	}

	@GetMapping(value = "/pacientes/find")
	public String initFindForm(final Map<String, Object> model) {
		model.put("paciente", new Paciente());
		return "pacientes/findPacientes";
	}

	@GetMapping(value = "/pacientes")
	public String processFindForm(Paciente paciente, final BindingResult result, final Map<String, Object> model) {
		if (paciente.getApellidos() == null) {
			paciente.setApellidos("");
		}

		Collection<Paciente> results;

		if (paciente.getApellidos() == "") {
			results = this.pacienteService.getPacientes();
		} else {
			results = this.pacienteService.findPacienteByApellidos(paciente.getApellidos());

		}

		if (results.isEmpty()) {
			result.rejectValue("apellidos", "notFound", "not found");
			return "pacientes/findPacientes";
		} else if (results.size() == 1) {
			paciente = results.iterator().next();
			return "redirect:/pacientes/" + paciente.getId();
		} else {
			model.put("selections", results);
			return "pacientes/pacientesList";
		}
	}

	@GetMapping(value = "/pacientes/findByMedico")
	public String initFindMedForm(final Map<String, Object> model) {

		Collection<Paciente> results = this.pacienteService.getPacientes();
		if (results.isEmpty()) {
			//Falta la página de error
			return "redirect:/pacientes";
		} else {
			model.put("selections", results);
			return "pacientes/pacientesListMedico";
		}
	}

	@GetMapping(value = "/pacientes/findByMedico/{medicoId}")
	public String initFindMedForm(final Map<String, Object> model, @PathVariable("medicoId") final int medicoId) {

		Collection<Paciente> results = this.pacienteService.findPacienteByMedicoId(medicoId);
		if (results.isEmpty()) {
			//Falta la página de error
			return "redirect:/pacientes/";
		} else {
			model.put("selections", results);
			return "pacientes/pacientesListMedico";
		}
	}

	@GetMapping(value = "/paciente/save/{pacienteId}")
	public String savePaciente(@Valid final Paciente paciente, final BindingResult result, final ModelMap modelMap) {
		String view = "/pacientes";

		if (result.hasErrors()) {
			modelMap.addAttribute("paciente", paciente);
			return "pacientes/editPaciente";
		} else {
			this.pacienteService.savePacienteByMedico(paciente, 1);
			modelMap.addAttribute("message", "Paciente guardado exitosamente");
		}

		return view;
	}

	@RequestMapping(value = "/pacientes/{pacienteId}/delete")
	public String borrarPaciente(@PathVariable("pacienteId") final int pacienteId, final ModelMap modelMap) {
		String view = "/pacientes";
		Optional<Paciente> paciente = this.pacienteService.findPacienteById(pacienteId);
		//String id = SecurityContextHolder.getContext().getAuthentication().getName();
		if (paciente.isPresent()) {
			//this.pacienteService.deletePacienteByMedico(pacienteId, 1);
			this.pacienteService.pacienteDelete(pacienteId);
			modelMap.addAttribute("message", "Paciente borrado exitosamiente");
			view = "redirect:/pacientes";
		} else {
			modelMap.addAttribute("message", "Paciente no encontrado");
		}
		return view;
	}

	@GetMapping(value = "/pacientes/{pacienteId}/edit")
	public String initUpdatePacientesForm(@PathVariable("pacienteId") final int pacientesId, final Model model) {
		Paciente paciente = this.pacienteService.findPacienteById(pacientesId).get();
		model.addAttribute(paciente);
		model.addAttribute(this.medicoService.getMedicos());
		return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pacientes/{pacienteId}/edit")
	public String processUpdatePacienteForm(@Valid final Paciente paciente, final BindingResult result, @PathVariable("pacienteId") final int pacienteId) {
		if (result.hasErrors()) {
			return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			paciente.setId(pacienteId);
			this.pacienteService.savePaciente(paciente);
			//this.pacienteService.savePacienteByMedico(paciente, idMedico);
			return "redirect:/pacientes/{pacienteId}";
		}
	}

	@GetMapping(value = "/pacientes/new")
	public String initCreationForm(final Map<String, Object> model) {
		Paciente paciente = new Paciente();
		model.put("paciente", paciente);
		model.put("medicoList", this.medicoService.getMedicos());
		return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pacientes/new")
	public String processCreationForm(@Valid final Paciente paciente, final BindingResult result) {
		if (result.hasErrors()) {
			return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			//creating owner, user and authorities
			this.pacienteService.savePaciente(paciente);

			return "redirect:/pacientes/" + paciente.getId();
		}
	}

}
