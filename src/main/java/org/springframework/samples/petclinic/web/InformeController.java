
package org.springframework.samples.petclinic.web;


import java.time.LocalDate;

import java.util.Optional;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Informe;

import org.springframework.samples.petclinic.model.Paciente;

import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/citas/{citaId}")
public class InformeController {

	private static final String				VIEWS_INFORME_CREATE_OR_UPDATE_FORM	= "informes/createOrUpdateInformeForm";
	private final InformeService			informeService;
	private final PacienteService			pacienteService;
	private final MedicoService				medicoService;
	private final UserService				userService;
	private final CitaService				citaService;
	private final HistoriaClinicaService	historiaClinicaService;


	@Autowired
	public InformeController(final PacienteService pacienteService, final MedicoService medicoService, final UserService userService, final AuthoritiesService authoritiesService, final CitaService citaService,
		final HistoriaClinicaService historiaClinicaService, final InformeService informeService) {
		this.informeService = informeService;
		this.pacienteService = pacienteService;
		this.medicoService = medicoService;
		this.userService = userService;
		this.citaService = citaService;
		this.historiaClinicaService = historiaClinicaService;
	}

	//	@InitBinder
	//	public void setAllowedFields(final WebDataBinder dataBinder) {
	//		dataBinder.setDisallowedFields("id");
	//	}

	@ModelAttribute("cita")
	public Cita findCita(@PathVariable("citaId") final int citaId) {
		return this.citaService.findCitaById(citaId).get();
	}

	@InitBinder("cita")
	public void initCitaBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}	
	@GetMapping(path = "/delete/{informeId}")
	public String borrarInforme(@PathVariable("informeId") final int informeId, final ModelMap modelMap) {
		Optional<Informe> informe = this.informeService.findInformeById(informeId);
		LocalDate today = LocalDate.now();
		
		if((informe.get().getCita().getFecha().isBefore(today.plusDays(1))) && informe.get().getHistoriaClinica() == null) {
			this.informeService.deleteInforme(informeId);
			modelMap.addAttribute("message", "Informe succesfully deleted");
		}else if(!(informe.get().getCita().getFecha().isBefore(today.plusDays(1)))){
			modelMap.addAttribute("message", "Informe is older than 24 hours");
		}else if(!(informe.get().getHistoriaClinica() == null)) {
			modelMap.addAttribute("message", "Informe has not a clinic history");
		}
		return "redirect:/informe";
	}
	
	@GetMapping(value = "/informes/{informeId}")
	public ModelAndView showInforme(@PathVariable("informeId") final int informeId) {
		ModelAndView mav = new ModelAndView("informes/informeDetails");
		Informe informe = this.informeService.findInformeById(informeId).get();
		
		mav.addObject(informe);
		
		Cita cita = this.citaService.findCitaById(informe.getCita().getId()).get();
		Paciente paciente = cita.getPaciente();
		mav.addObject(paciente);

		Boolean canBeDeleted = informe.getCita().getFecha().isBefore(LocalDate.now().plusDays(1)) && informe.getHistoriaClinica() == null;
		mav.getModel().put("canbedeleted", canBeDeleted);
		return mav;
	}
	
	

	@GetMapping(value = "/informes/new")
	public String initCreationForm(final Cita cita, final ModelMap model) {
		Informe informe = new Informe();
		informe.setCita(cita);
		model.put("informe", informe);
		return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/informes/new")
	public String processCreationForm(final Cita cita, @Valid final Informe informe, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("informe", informe);
			return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
		} else {
			informe.setCita(cita);
			this.informeService.saveInforme(informe);
		}
		return "redirect:/citas/" + informe.getCita().getPaciente().getMedico().getId();
	}


}
