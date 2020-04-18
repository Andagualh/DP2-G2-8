
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

	@GetMapping(value = "/informes/new")
	public String initCreationForm(final Cita cita, final ModelMap model) {
		LocalDate today = LocalDate.now();
		if (cita.getFecha().equals(today)) {
			Informe informe = new Informe();
			informe.setCita(cita);
			model.put("informe", informe);
			return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
		} else {
			return "redirect:/citas/" + cita.getPaciente().getMedico().getId();
		}
	}

	@PostMapping(value = "/informes/new")
	public String processCreationForm(final Cita cita, @Valid final Informe informe, final BindingResult result, final ModelMap model) throws DataAccessException, IllegalAccessException {
		if (result.hasErrors()) {
			model.put("informe", informe);
			return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
		} else {
			informe.setCita(cita);
			this.informeService.saveInforme(informe);
		}
		return "redirect:/citas/" + informe.getCita().getPaciente().getMedico().getId();
	}

	@GetMapping("/informes/{informeId}")
	public ModelAndView showInforme(@PathVariable("informeId") final int informeId) {
		ModelAndView mav = new ModelAndView("informes/informeDetails");
		System.out.println("informeDetails" + this.informeService.findInformeById(informeId).get());
		mav.addObject(this.informeService.findInformeById(informeId).get());
		return mav;
	}

}
