
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.PacienteService;
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
	private final PacienteService			pacienteService;
	@Autowired
	private final UserService				userService;


	@Autowired
	public HistoriaClinicaController(final HistoriaClinicaService historiaclinicaService, final PacienteService pacienteService, final UserService userService, final AuthoritiesService authoritiesService) {
		this.historiaclinicaService = historiaclinicaService;
		this.pacienteService = pacienteService;
		this.userService = userService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	public Boolean medicoOk(Paciente paciente) {
		return paciente.getMedico().equals(this.userService.getCurrentMedico());
	}
	
	@GetMapping(value = "/pacientes/{pacienteId}/historiaclinica")
	public ModelAndView showHistoriaClinica(@PathVariable("pacienteId") final int pacienteId) {
			Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();
			ModelAndView mav = new ModelAndView("pacientes/historiaClinicaDetails");
			mav.addObject(this.pacienteService.findPacienteById(pacienteId).get());
			
			HistoriaClinica historiaclinica = this.pacienteService.findHistoriaClinicaByPaciente(paciente);
			if (this.pacienteService.findHistoriaClinicaByPaciente(paciente) == null) {
				HistoriaClinica hc = new HistoriaClinica();
				historiaclinica = hc;
			}
			mav.addObject("historiaclinica", historiaclinica);
			return mav;
	}

	@GetMapping(value = "/pacientes/{pacienteId}/historiaclinica/new")
	public String initCreationForm(@PathVariable("pacienteId") final int pacienteId, final ModelMap model) {
		HistoriaClinica historiaclinica = new HistoriaClinica();
		Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();
		boolean medicoOk = medicoOk(paciente);
		if(medicoOk) {
			model.put("historiaclinica", historiaclinica);
			model.put("paciente", paciente);
			return HistoriaClinicaController.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM;
		}else{
			model.addAttribute("errorhc", "No puede crear una historia clinica para otro medico");
			return "redirect:/pacientes";
		}
	}

	@PostMapping(value = "/pacientes/{pacienteId}/historiaclinica/new")
	public String processCreationForm(@PathVariable("pacienteId") final int pacienteId, final HistoriaClinica historiaclinica, final BindingResult result, final ModelMap model) {
		Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();
		boolean descripcionVacia = historiaclinica.getDescripcion() == "";
			if (result.hasErrors() || descripcionVacia) {
				model.addAttribute("historiaclinica", historiaclinica);
				model.addAttribute("paciente", paciente);
				model.put("errord", "La descripcion no puede estar vacia.");
				return HistoriaClinicaController.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM;
			} else if (this.pacienteService.findHistoriaClinicaByPaciente(paciente) != null) {
				return "redirect:/oups";
			} else {
				historiaclinica.setPaciente(paciente);
				this.historiaclinicaService.saveHistoriaClinica(historiaclinica);
				return "redirect:/pacientes/{pacienteId}";
			}
	}

	@GetMapping(value = "/pacientes/{pacienteId}/historiaclinica/edit")
	public String initUpdateForm(@PathVariable("pacienteId") final int pacienteId, final ModelMap model) {
		Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();
		if(this.medicoOk(paciente)) {
			HistoriaClinica historiaclinica = this.pacienteService.findHistoriaClinicaByPaciente(paciente);
			historiaclinica.setPaciente(this.pacienteService.findPacienteById(pacienteId).get());
			model.put("historiaclinica", historiaclinica);
			model.put("paciente", this.pacienteService.findPacienteById(pacienteId).get());
	
			return HistoriaClinicaController.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM;
		}else {
			return "redirect:/pacientes";
		}
	}

	@PostMapping(value = "/pacientes/{pacienteId}/historiaclinica/edit")
	public String processUpdateHistoriaClinicaForm(@Valid final HistoriaClinica historiaclinica, final BindingResult result, @PathVariable("pacienteId") final int pacienteId, final ModelMap model) {
		Paciente paciente = this.pacienteService.findPacienteById(pacienteId).get();
		boolean descripcionVacia = historiaclinica.getDescripcion() == "";
		if (result.hasErrors() || descripcionVacia) {
			model.addAttribute("historiaclinica", this.pacienteService.findHistoriaClinicaByPaciente(paciente));
			model.addAttribute("paciente", paciente);
			model.put("errord", "La descripcion no puede estar vacia.");
			return HistoriaClinicaController.VIEWS_HISTORIACLINICA_CREATE_OR_UPDATE_FORM;
		} else {

			historiaclinica.setId(this.pacienteService.findHistoriaClinicaByPaciente(paciente).getId());
			this.historiaclinicaService.saveHistoriaClinica(historiaclinica);
			return "redirect:/pacientes/{pacienteId}";
		}
	}
}
