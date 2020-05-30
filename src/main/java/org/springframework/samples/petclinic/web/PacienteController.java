
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.util.DniValidator;
import org.springframework.stereotype.Controller;
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

	@Autowired
	public PacienteController(final PacienteService pacienteService, final MedicoService medicoService,
			final UserService userService, final AuthoritiesService authoritiesService, final CitaService citaService,
			final HistoriaClinicaService historiaClinicaService) {
		this.pacienteService = pacienteService;
		this.medicoService = medicoService;
		this.userService = userService;
		this.citaService = citaService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/pacientes/{pacienteId}")
	public ModelAndView showPaciente(@PathVariable("pacienteId") final int pacienteId) {
		ModelAndView mav = new ModelAndView("pacientes/pacienteDetails");
		Paciente paciente = new Paciente();

		if (this.pacienteService.findPacienteById(pacienteId).isPresent()) {
			paciente = this.pacienteService.findPacienteById(pacienteId).get();
		}

		mav.addObject(paciente);
		mav.getModel().put("canBeDeleted", checkDeleteforCitas(paciente));
		mav.getModel().put("medicoCheck", sameMedico(paciente));
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
			return "redirect:/pacientes/";
		} else {
			model.put("selections", results);
			return "pacientes/pacientesListMedico";
		}
	}

	@RequestMapping(value = "/pacientes/{pacienteId}/delete")
	public String borrarPaciente(@PathVariable("pacienteId") final int pacienteId, final ModelMap modelMap) {
		String view = "/pacientes";

		Optional<Paciente> optionalPaciente = this.pacienteService.findPacienteById(pacienteId);

		if (optionalPaciente.isPresent()) {
			Paciente paciente = optionalPaciente.get();

			boolean puedeBorrarse = checkDeleteforCitas(paciente) && sameMedico(paciente)
					&& paciente.getMedico().getUser().isEnabled();

			if (puedeBorrarse) {
				this.pacienteService.deletePacienteByMedico(pacienteId, this.userService.getCurrentMedico().getId());
				modelMap.addAttribute("message", "Paciente borrado exitosamiente");
				view = "redirect:/pacientes";
			} else if (!sameMedico(paciente)) {
				modelMap.addAttribute("message", "No tiene acceso para borrar a este paciente");
				view = "redirect:/pacientes/" + pacienteId;
			} else {
				modelMap.addAttribute("message", "Paciente no puede borrarse");
				view = "redirect:/pacientes/" + pacienteId;
			}
		} else {
			modelMap.addAttribute("message", "Paciente no encontrado");
			view = "accessNotAuthorized";
		}
		return view;
	}

	@GetMapping(value = "/pacientes/{pacienteId}/edit")
	public String initUpdatePacientesForm(@PathVariable("pacienteId") final int pacientesId, final ModelMap model) {

		Paciente paciente = new Paciente();

		if (this.pacienteService.findPacienteById(pacientesId).isPresent()) {
			paciente = this.pacienteService.findPacienteById(pacientesId).get();
		}

		if (sameMedico(paciente)) {
			model.addAttribute("paciente", paciente);
			model.addAttribute("medicoList", this.medicoService.getMedicos());
			model.addAttribute("isNewPaciente", false);
			return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			return "redirect:/pacientes/" + pacientesId;

		}
	}

	@PostMapping(value = "/pacientes/{pacienteId}/edit")
	public String processUpdatePacienteForm(@Valid final Paciente paciente, final BindingResult result,
			@PathVariable("pacienteId") final int pacienteId, final ModelMap model) {

		boolean dniOk = new DniValidator(paciente.getDNI()).validar();
		boolean pacienteValid = !hasContact(paciente) && dniOk;

		model.addAttribute("isNewPaciente", false);

		if (result.hasErrors() || !pacienteValid || !telefonoOk(paciente)) {
			model.addAttribute("medicoList", this.medicoService.getMedicos());
			if (hasContact(paciente)) {
				result.rejectValue("domicilio", "error.formaContacto", "No tiene forma de contacto.");
			}
			if (!dniOk) {
				result.rejectValue("DNI", "error.DNI", "DNI invalido.");
			}
			if (!telefonoOk(paciente)) {
				result.rejectValue("n_telefono", "error.n_telefono", "Telefono debe tener 9 digitos");
			}
			return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			paciente.setId(pacienteId);
			this.pacienteService.savePacienteByMedico(paciente, this.userService.getCurrentMedico().getId());
			return "redirect:/pacientes/" + pacienteId;
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
			final ModelMap model) {
		
		boolean dniOk = new DniValidator(paciente.getDNI()).validar();
		boolean pacienteValid = !hasContact(paciente) && dniOk;

		model.addAttribute("isNewPaciente", true);

		if (result.hasErrors() || !pacienteValid || !telefonoOk(paciente) || !sameMedico(paciente)) {
			model.addAttribute("medicoList", this.medicoService.getMedicos());
			if (hasContact(paciente)) {
				result.rejectValue("domicilio", "error.formaContacto", "No tiene forma de contacto.");
			}
			if (!dniOk) {
				result.rejectValue("DNI", "error.DNI", "DNI invalido.");
			}
			if (!telefonoOk(paciente)) {
				result.rejectValue("n_telefono", "error.n_telefono", "Telefono debe tener 9 digitos");
			}
			if (!sameMedico(paciente)) {
				paciente.setMedico(this.userService.getCurrentMedico());
				result.rejectValue("medico", "error.medico", "No puedes crear un paciente para otro medico.");
				return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
			}
			return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			int pacienteId = this.pacienteService.savePacienteByMedico(paciente,
					this.userService.getCurrentMedico().getId());

			return "redirect:/pacientes/" + pacienteId;
		}
	}
	
	//Metodos Auxiliares
	
	public boolean sameMedico(Paciente paciente) {
		return paciente.getMedico().equals(this.userService.getCurrentMedico());
	}

	public boolean checkDeleteforCitas(Paciente paciente) {
		Collection<Cita> citas = this.citaService.findAllByPaciente(paciente);
		if (!citas.isEmpty()) {
			LocalDate ultimaCita = citas.stream().map(Cita::getFecha).max(LocalDate::compareTo).get();
			LocalDate hoy = LocalDate.now();
			return hoy.compareTo(ultimaCita) >= 6
					|| hoy.compareTo(ultimaCita) == 5 && hoy.getDayOfYear() > ultimaCita.getDayOfYear();
		} else {
			return true;
		}
	}

	public boolean hasContact(Paciente paciente) {
		return paciente.getN_telefono() == null && paciente.getDomicilio().isEmpty() && paciente.getEmail().isEmpty();
	}

	public boolean telefonoOk(Paciente paciente) {
		return ((paciente.getN_telefono() == null) ? true : (paciente.getN_telefono().toString().length() == 9));
	}
}
