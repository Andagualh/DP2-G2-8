
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
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PacienteController {

	private static final String VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM = "pacientes/createOrUpdatePacientesForm";
	private static final String REDIRECT_PACIENTES = "redirect:/pacientes/";
	private static final String PACIENTE_STRING = "paciente";
	private static final String MEDICO_LIST_URL = "medicoList";
	private static final String IS_NEW_PACIENTE_LABEL = "isNewPaciente";

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

		Paciente paciente = checkPacienteIsPresent(pacienteId);

		mav.addObject(paciente);
		mav.getModel().put("canBeDeleted", checkDeleteforCitas(paciente));
		mav.getModel().put("medicoCheck", sameMedico(paciente));
		return mav;
	}

	@GetMapping(value = "/pacientes/find")
	public String initFindForm(final Map<String, Object> model) {
		model.put(PACIENTE_STRING, new Paciente());
		return "pacientes/findPacientes";
	}

	@GetMapping(value = "/pacientes")
	public String processFindForm(Paciente paciente, final BindingResult result, final Map<String, Object> model) {
		if (paciente.getApellidos() == null) {
			paciente.setApellidos("");
		}

		Collection<Paciente> results;

		if (paciente.getApellidos().equals("")) {
			results = this.pacienteService.getPacientes();
		} else {
			results = this.pacienteService.findPacienteByApellidos(paciente.getApellidos());
		}

		if (results.isEmpty()) {
			result.rejectValue("apellidos", "notFound", "not found");
			return "pacientes/findPacientes";
		} else if (results.size() == 1) {
			paciente = results.iterator().next();
			return REDIRECT_PACIENTES + paciente.getId();
		} else {
			model.put("selections", results);
			return "pacientes/pacientesList";
		}
	}

	@GetMapping(value = "/pacientes/findByMedico")
	public String initFindMedForm(final Map<String, Object> model) {
		int idMedico = this.userService.getCurrentMedico().getId();
		return REDIRECT_PACIENTES + "findByMedico/" + idMedico;
	}

	@GetMapping(value = "/pacientes/findByMedico/{medicoId}")
	public String processFindMedForm(final Map<String, Object> model, @PathVariable("medicoId") final int medicoId) {

		Collection<Paciente> results = this.pacienteService.findPacienteByMedicoId(medicoId);
		if (results.isEmpty()) {
			return REDIRECT_PACIENTES;
		} else {
			model.put("selections", results);
			return "pacientes/pacientesListMedico";
		}
	}

	@GetMapping(value = "/pacientes/{pacienteId}/delete")
	public String borrarPaciente(@PathVariable("pacienteId") final int pacienteId, final ModelMap modelMap) {
		String view;
		Optional<Paciente> optionalPaciente = this.pacienteService.findPacienteById(pacienteId);
		String labelMessage = "message";

		if (optionalPaciente.isPresent()) {
			Paciente paciente = optionalPaciente.get();
			boolean puedeBorrarse = checkDeleteforCitas(paciente) && sameMedico(paciente)
					&& paciente.getMedico().getUser().isEnabled();

			if (puedeBorrarse) {
				this.pacienteService.deletePacienteByMedico(pacienteId, this.userService.getCurrentMedico().getId());
				modelMap.addAttribute(labelMessage, "Paciente borrado exitosamiente");
				view = "redirect:/pacientes";
			} else if (!sameMedico(paciente)) {
				modelMap.addAttribute(labelMessage, "No tiene acceso para borrar a este paciente");
				view = REDIRECT_PACIENTES + pacienteId;
			} else {
				modelMap.addAttribute(labelMessage, "Paciente no puede borrarse");
				view = REDIRECT_PACIENTES + pacienteId;
			}
		} else {
			modelMap.addAttribute(labelMessage, "Paciente no encontrado");
			view = "accessNotAuthorized";
		}
		return view;
	}

	@GetMapping(value = "/pacientes/{pacienteId}/edit")
	public String initUpdatePacientesForm(@PathVariable("pacienteId") final int pacienteId, final ModelMap model) {

		Paciente paciente = checkPacienteIsPresent(pacienteId);

		if (sameMedico(paciente)) {
			model.addAttribute(PACIENTE_STRING, paciente);
			model.addAttribute(MEDICO_LIST_URL, this.medicoService.getMedicos());
			model.addAttribute(IS_NEW_PACIENTE_LABEL, false);
			return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			return REDIRECT_PACIENTES + pacienteId;

		}
	}

	@PostMapping(value = "/pacientes/{pacienteId}/edit")
	public String processUpdatePacienteForm(@Valid final Paciente paciente, final BindingResult result,
			@PathVariable("pacienteId") final int pacienteId, final ModelMap model) {

		boolean dniOk = new DniValidator(paciente.getDNI()).validar();
		boolean pacienteValid = !hasContact(paciente) && dniOk;

		model.addAttribute(IS_NEW_PACIENTE_LABEL, false);

		if (result.hasErrors() || !pacienteValid || !telefonoOk(paciente)) {
			model.addAttribute(MEDICO_LIST_URL, this.medicoService.getMedicos());
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
			this.pacienteService.savePacienteByMedico(paciente, paciente.getMedico().getId());
			return REDIRECT_PACIENTES + pacienteId;
		}
	}

	@GetMapping(value = "/pacientes/new")
	public String initCreationForm(final Map<String, Object> model) {
		Paciente paciente = new Paciente();
		paciente.setMedico(this.userService.getCurrentMedico());
		paciente.setF_alta(LocalDate.now());
		model.put(PACIENTE_STRING, paciente);
		model.put(MEDICO_LIST_URL, this.medicoService.getMedicos());
		model.put(IS_NEW_PACIENTE_LABEL, true);

		return PacienteController.VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pacientes/new")
	public String processCreationForm(@Valid final Paciente paciente, final BindingResult result,
			final ModelMap model) {

		boolean dniOk = new DniValidator(paciente.getDNI()).validar();
		boolean pacienteValid = !hasContact(paciente) && dniOk;

		model.addAttribute(IS_NEW_PACIENTE_LABEL, true);

		if (result.hasErrors() || !pacienteValid || !telefonoOk(paciente) || !sameMedico(paciente)) {
			model.addAttribute(MEDICO_LIST_URL, this.medicoService.getMedicos());
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

			return REDIRECT_PACIENTES + pacienteId;
		}
	}

	public Paciente checkPacienteIsPresent(int pacienteId) {
		return this.pacienteService.findPacienteById(pacienteId).orElse(null);
	}

	public boolean sameMedico(Paciente paciente) {
		return paciente.getMedico().equals(this.userService.getCurrentMedico());
	}

	public boolean checkDeleteforCitas(Paciente paciente) {
		Collection<Cita> citas = this.citaService.findAllByPaciente(paciente);
		if (!citas.isEmpty()) {
			LocalDate ultimaCita = citas.stream().filter(c -> c.getFecha() != null).map(Cita::getFecha)
					.max(LocalDate::compareTo).orElse(null);
			if (ultimaCita != null) {
				LocalDate hoy = LocalDate.now();

				int compareDate = hoy.compareTo(ultimaCita);

				return compareDate >= 6 || compareDate >= 5 && hoy.getDayOfYear() > ultimaCita.getDayOfYear();
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	public boolean hasContact(Paciente paciente) {
		return paciente.getN_telefono() == null && paciente.getDomicilio().isEmpty() && paciente.getEmail().isEmpty();
	}

	public boolean telefonoOk(Paciente paciente) {
		if ((paciente.getN_telefono() == null)) {
			return true;
		} else {
			return paciente.getN_telefono().toString().length() == 9;
		}
	}
}
