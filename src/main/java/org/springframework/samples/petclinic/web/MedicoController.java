
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MedicoController {

	//private static final String		VIEWS_MEDICO_CREATE_OR_UPDATE_	= "pacientes/createOrUpdatePacientesForm";
	@Autowired
	private final MedicoService medicoService;


	@Autowired
	public MedicoController(final MedicoService medicoService, final UserService userService, final AuthoritiesService authoritiesService) {
		this.medicoService = medicoService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/medicos/{medicoId}")
	public ModelAndView showMedico(@PathVariable("medicoId") final int medicoId) {
		ModelAndView mav = new ModelAndView("medicos/medicoDetails");
		mav.addObject(this.medicoService.getMedicoById(medicoId));
		return mav;
	}

	@GetMapping(value = "/medicos/find")
	public String initFindForm(final Map<String, Object> model) {
		model.put("medico", new Medico());
		return "medicos/findMedicos";
	}

	@GetMapping(value = "/medicos")
	public String processFindForm(Medico medico, final BindingResult result, final Map<String, Object> model) {
		if (medico.getApellidos() == null) {
			medico.setApellidos("");
		}
		Collection<Medico> results;

		if (medico.getApellidos() == "") {
			results = this.medicoService.getMedicos();
		} else {
			results = this.medicoService.findMedicoByApellidos(medico.getApellidos());
		}

		if (results.isEmpty()) {
			result.rejectValue("apellidos", "notFound", "not found");
			return "medicos/findMedicos";
		} else if (results.size() == 1) {
			medico = results.iterator().next();
			return "redirect:/medicos/" + medico.getId();
		} else {
			model.put("selections", results);
			return "medicos/medicosList";
		}
	}
}
