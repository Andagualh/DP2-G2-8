
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.util.DniValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

	private static final String VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM = "pacientes/createOrUpdatePacientesForm";
	@Autowired
	private final PacienteService pacienteService;
	private final MedicoService medicoService;
	private final UserService userService;
	private final CitaService citaService;
	private final HistoriaClinicaService historiaClinicaService;

	@Autowired
	public PacienteController(final PacienteService pacienteService, final MedicoService medicoService,
			final UserService userService, final AuthoritiesService authoritiesService, final CitaService citaService,
			final HistoriaClinicaService historiaClinicaService) {
		this.pacienteService = pacienteService;
		this.medicoService = medicoService;
		this.userService = userService;
		this.citaService = citaService;
		this.historiaClinicaService = historiaClinicaService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/pacientes/{pacienteId}")
	public ModelAndView showPaciente(@PathVariable("pacienteId") final int pacienteId) {
		ModelAndView mav = new ModelAndView("pacientes/pacienteDetails");
		Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();

		mav.addObject(this.pacienteService.findPacienteById(pacienteId).get());

		Collection<Cita> citas = this.citaService.findAllByPaciente(paciente);
		boolean canBeDeleted = citas.isEmpty();
		if (!citas.isEmpty()) {
			LocalDate ultimaCita = this.citaService.findAllByPaciente(paciente).stream().map(Cita::getFecha)
					.max(LocalDate::compareTo).get();
			LocalDate hoy = LocalDate.now();
			canBeDeleted = hoy.compareTo(ultimaCita) >= 6
					|| hoy.compareTo(ultimaCita) == 5 && hoy.getDayOfYear() >= hoy.getDayOfYear();
		}

		canBeDeleted = canBeDeleted && this.historiaClinicaService.findHistoriaClinicaByPaciente(paciente) == null;

		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.contains(new SimpleGrantedAuthority("admin"))) {
			canBeDeleted = true;
		} else {
			canBeDeleted = canBeDeleted && paciente.getMedico().equals(this.userService.getCurrentMedico());
		}

		mav.getModel().put("canBeDeleted", true);
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
		int idMedico = this.userService.getCurrentMedico().getId();
		return "redirect:/pacientes/findByMedico/" + idMedico;
	}

	@GetMapping(value = "/pacientes/findByMedico/{medicoId}")
	public String processFindMedForm(final Map<String, Object> model, @PathVariable("medicoId") final int medicoId) {

		Collection<Paciente> results = this.pacienteService.findPacienteByMedicoId(medicoId);
		if (results.isEmpty()) {
			// Falta la página de error
			return "redirect:/pacientes/";
		} else {
			model.put("selections", results);
			return "pacientes/pacientesListMedico";
		}
	}

	// @GetMapping(value = "/paciente/save/{pacienteId}")
	// public String savePaciente(@Valid final Paciente paciente, final
	// BindingResult result, final ModelMap modelMap) {
	// String view = "/pacientes";
	//
	// if (result.hasErrors()) {
	// modelMap.addAttribute("paciente", paciente);
	// return "pacientes/editPaciente";
	// } else {
	// this.pacienteService.savePacienteByMedico(paciente,
	// this.userService.getCurrentMedico().getId());
	// modelMap.addAttribute("message", "Paciente guardado exitosamente");
	// }
	//
	// return view;
	// }

	@RequestMapping(value = "/pacientes/{pacienteId}/delete")
	public String borrarPaciente(@PathVariable("pacienteId") final int pacienteId, final ModelMap modelMap) {
		String view = "/pacientes";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Paciente> paciente = this.pacienteService.findPacienteById(pacienteId);

		if (paciente.isPresent()) {
			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
				this.pacienteService.pacienteDelete(pacienteId);
			} else {
				this.pacienteService.deletePacienteByMedico(pacienteId, this.userService.getCurrentMedico().getId());
			}
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
		// Paciente paciente = this.pacienteService.getPacienteById(pacientesId);
		model.addAttribute("paciente", paciente);
		model.addAttribute("medicoList", this.medicoService.getMedicos());
		model.addAttribute("isNewPaciente", false);
		return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pacientes/{pacienteId}/edit")
	public String processUpdatePacienteForm(@Valid final Paciente paciente, final BindingResult result,
			@PathVariable("pacienteId") final int pacienteId, final ModelMap modelMap) {

		boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.contains(new SimpleGrantedAuthority("admin"));
		boolean noTieneContacto = paciente.getN_telefono() == null && paciente.getDomicilio().isEmpty()
				&& paciente.getEmail().isEmpty();

		if (result.hasErrors() || noTieneContacto) {
			modelMap.addAttribute("medicoList", this.medicoService.getMedicos());
			modelMap.addAttribute("isNewPaciente", false);
			result.rejectValue("domicilio", "error.formaContacto", "No tiene forma de contacto.");
			return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
		} else if (isAdmin) {
			paciente.setId(pacienteId);
			this.pacienteService.savePaciente(paciente);
			return "redirect:/pacientes/{pacienteId}";
		} else {
			int currentMedico = this.userService.getCurrentMedico().getId();
			paciente.setId(pacienteId);
			this.pacienteService.savePacienteByMedico(paciente, currentMedico);
			return "redirect:/pacientes/{pacienteId}";
		}
	}

	@GetMapping(value = "/pacientes/new")
	public String initCreationForm(final Map<String, Object> model) {
		Paciente paciente = new Paciente();
		paciente.setMedico(this.userService.getCurrentMedico());
		paciente.setF_alta(LocalDate.now());
		model.put("paciente", paciente);
		model.put("medicoList", this.medicoService.getMedicos());
		model.put("isNewPaciente", true);

		return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pacientes/new")
	public String processCreationForm(@Valid final Paciente paciente, final BindingResult result,
			final ModelMap modelMap) {
		boolean noTieneContacto = paciente.getN_telefono() == null && paciente.getDomicilio().isEmpty()
				&& paciente.getEmail().isEmpty();
		boolean dniOk = new DniValidator(paciente.getDNI()).validar();
		boolean pacienteValid = noTieneContacto && !dniOk;

		if (result.hasErrors() || pacienteValid) {
			modelMap.addAttribute("medicoList", this.medicoService.getMedicos());
			modelMap.addAttribute("isNewPaciente", true);
			if (noTieneContacto) {
				result.rejectValue("domicilio", "error.formaContacto", "No tiene forma de contacto.");
			}
			if (!dniOk)
				result.rejectValue("DNI", "error.DNI", "DNI invalido.");
			return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			this.pacienteService.savePacienteByMedico(paciente, this.userService.getCurrentMedico().getId());
			HistoriaClinica hc = new HistoriaClinica();
			hc.setPaciente(paciente);
			this.historiaClinicaService.saveHistoriaClinica(hc);
			return "redirect:/pacientes/" + paciente.getId();
		}
	}
}
