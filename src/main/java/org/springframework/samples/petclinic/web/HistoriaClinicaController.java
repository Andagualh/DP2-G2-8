
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.UserService;
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
public class HistoriaClinicaController {

	private static final String				VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM	= "pacientes/createOrUpdateHistoriaClinicaForm";

	@Autowired
	private final HistoriaClinicaService	historiaclinicaService;


	@Autowired
	public HistoriaClinicaController(final HistoriaClinicaService historiaclinicaService, final UserService userService, final AuthoritiesService authoritiesService) {
		this.historiaclinicaService = historiaclinicaService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/pacientes/{pacienteId}/historiaclinica")
	public ModelAndView showHistoriaClinica(@PathVariable("pacienteId") final int pacienteId) {
		ModelAndView mav = new ModelAndView("pacientes/pacienteDetails/historiaclinica");
		mav.addObject(this.historiaclinicaService.findHistoriaClinicaByPacienteId(pacienteId));//le he quitado el get, quizas da fallo
		return mav;
	}

	@GetMapping(value = "/pacientes/{pacienteId}/historiaclinica/new")
	public String initCreationForm(final Paciente paciente, final ModelMap model) {
		HistoriaClinica historiaclinica = new HistoriaClinica();
		paciente.setHistoriaclinica(historiaclinica);
		model.put("historiaclinica", historiaclinica);
		return HistoriaClinicaController.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pacientes/{pacienteId}/historiaclinica/new")
	public String processCreationForm(final Paciente paciente, final HistoriaClinica historiaclinica, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("historiaclinica", historiaclinica);
			return HistoriaClinicaController.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM;
		} else {
			paciente.setHistoriaclinica(historiaclinica);
			this.historiaclinicaService.save(historiaclinica);
			return "redirect:/pacientes/{pacienteId}";
		}
	}

	@GetMapping(value = "/pacientes/{pacienteId}/historiaclinica/edit")
	public String initUpdateForm(@PathVariable("historiaclinicaId") final int historiaclinicaId, final ModelMap model) {
		HistoriaClinica historiaclinica = this.historiaclinicaService.findHistoriaClinicaById(historiaclinicaId);
		model.put("historiaclinica", historiaclinica);
		return HistoriaClinicaController.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pacientes/{pacienteId}/historiaclinica/edit")
	public String processUpdateHistoriaClinicaForm(@Valid final HistoriaClinica historiaclinica, final BindingResult result, @PathVariable("pacienteId") final int pacienteId) {
		if (result.hasErrors()) {
			return HistoriaClinicaController.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM;
		} else {
			historiaclinica.setId(pacienteId);
			this.historiaclinicaService.save(historiaclinica);
			//this.pacienteService.savePacienteByMedico(paciente, idMedico);
			return "redirect:/pacientes/{pacienteId}";
		}
	}

}
