
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.TratamientoService;
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
	private static final String				VIEWS_ACCESS_NOT_AUTHORIZED			= "accessNotAuthorized";
	private static final String 			REDIRECTION_TO_CITA = "redirect:/citas/";
	private static final String 			INFORME_MODELO = "informe";
	private final InformeService			informeService;
	private final UserService				userService;
	private final CitaService				citaService;
	private final HistoriaClinicaService	historiaClinicaService;
	private final TratamientoService		tratamientoService;


	@Autowired
	public InformeController(final UserService userService, final AuthoritiesService authoritiesService, 
		final CitaService citaService,final HistoriaClinicaService historiaClinicaService, final InformeService informeService, 
		final TratamientoService tratamientoService) {
		this.informeService = informeService;
		this.userService = userService;
		this.citaService = citaService;
		this.historiaClinicaService = historiaClinicaService;
		this.tratamientoService = tratamientoService;
	}

	@ModelAttribute("cita")
	public Cita findCita(@PathVariable("citaId") final int citaId) {
		return this.citaService.findCitaById(citaId).orElse(null);
	}

	@InitBinder("cita")
	public void initCitaBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(path = "informes/delete/{informeId}")
	public String borrarInforme(@PathVariable("informeId") final int informeId, final ModelMap modelMap) throws IllegalAccessException {
		Informe informe = checkInformeIsPresent(informeId);
		

		if (!sameMedico(informe)) {
			return InformeController.VIEWS_ACCESS_NOT_AUTHORIZED;
		} else {
			if (informe.getHistoriaClinica() == null) {
				this.informeService.deleteInforme(informeId);
				modelMap.addAttribute("message", "Informe succesfully deleted");
			} else {
				modelMap.addAttribute("message", "Informe has not a clinic history");
			}
			return "redirect:/";
		}
	}

	@GetMapping(value = "/informes/{informeId}")
	public ModelAndView showInforme(@PathVariable("informeId") final int informeId, 
	@PageableDefault(value = 5, page= 0) Pageable pageable){

		ModelAndView mav = new ModelAndView("informes/informeDetails");
		Informe informe = checkInformeIsPresent(informeId);

		if (!sameMedico(informe) && informe.getHistoriaClinica() == null) {
			return new ModelAndView(VIEWS_ACCESS_NOT_AUTHORIZED);
		} else {

			mav.addObject(informe);

			Cita cita = findCita(informe.getCita().getId());
			Paciente paciente = cita.getPaciente();
			mav.addObject(paciente);

			Boolean canBeDeleted = informe.getCita().getFecha().plusDays(1).equals(LocalDate.now().plusDays(1)) && informe.getHistoriaClinica() == null;
			mav.getModel().put("cannotbedeleted", !canBeDeleted);
			
			if(informe.getTratamientos() != null){
			Page<Tratamiento> tratamientos = this.tratamientoService.findTrata(informeId, pageable);
			
			mav.getModel().put("editTratamientoOk", cita.getFecha().equals(LocalDate.now()));
			mav.getModel().put("tratamientos", tratamientos.getContent());
			mav.getModel().put("tratapages", tratamientos.getTotalPages()-1);
				}

			Boolean canBeEdited = informe.getCita().getFecha().equals(LocalDate.now());
			mav.getModel().put("canbeedited", canBeEdited);

			return mav;
		}
	}

	@GetMapping(value = "/informes/new")
	public String initCreationForm(final Cita cita, final ModelMap model) {
		
		
		if (!sameMedico(cita)) {
			return InformeController.VIEWS_ACCESS_NOT_AUTHORIZED;
		} else {
			if (cita.getFecha().equals(LocalDate.now()) && !hasCitaInforme(cita)) {
				Informe informe = new Informe();
				informe.setCita(cita);
				model.put(InformeController.INFORME_MODELO, informe);
				return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
			} else if (hasCitaInforme(cita)) {
				return InformeController.REDIRECTION_TO_CITA + cita.getId() + "/informes/" + cita.getInforme().getId();
			} else {
				return InformeController.REDIRECTION_TO_CITA + cita.getPaciente().getMedico().getId();

			}
		}
	}

	@PostMapping(value = "/informes/new")
	public String processCreationForm(final Cita cita, @Valid final Informe informe, final BindingResult result, final ModelMap model) throws IllegalAccessException {

		if (result.hasErrors()) {
			model.put(InformeController.INFORME_MODELO, informe);
			return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
		} else {
			informe.setCita(cita);
			this.informeService.saveInforme(informe);
		}
		return InformeController.REDIRECTION_TO_CITA + informe.getCita().getPaciente().getMedico().getId();
	}

	@GetMapping(value = "/informes/{informeId}/addtohistoriaclinica")
	public String addHistoriaClinicaToInforme(@PathVariable("informeId") final int informeId) throws IllegalAccessException {
		Informe informe = checkInformeIsPresent(informeId);

		if (!sameMedico(informe)) {
			return InformeController.VIEWS_ACCESS_NOT_AUTHORIZED;
		} else {

			HistoriaClinica hc = this.historiaClinicaService.findHistoriaClinicaByPaciente(informe.getCita().getPaciente());
			informe.setHistoriaClinica(hc);
			this.informeService.saveInformeWithHistoriaClinica(informe);
			return "redirect:/pacientes/" + informe.getCita().getPaciente().getId() + "/historiaclinica";
		}
	}

	@GetMapping(value = "/informes/{informeId}/detelefromhistoriaclinica")
	public String deleteFromHistoriaClinica(@PathVariable("informeId") final int informeId) {
		Informe informe = checkInformeIsPresent(informeId);
		

		if (!sameMedico(informe)) {
			return InformeController.VIEWS_ACCESS_NOT_AUTHORIZED;
		} else {
			this.informeService.deleteInformeToHistoriaClinica(informe);
			return InformeController.REDIRECTION_TO_CITA + informe.getCita().getPaciente().getMedico().getId() + "/informes/" + informe.getId();
		}
	}

	@GetMapping(value = "/informes/{informeId}/edit")
	public String initUpdateInformeForm(@PathVariable("informeId") final int informeId, final ModelMap model) {

		Informe informe = checkInformeIsPresent(informeId);
	

		if (!sameMedico(informe)) {
			return InformeController.VIEWS_ACCESS_NOT_AUTHORIZED;
		} else {
			model.put(InformeController.INFORME_MODELO, informe);
			model.put("motivo_consulta", informe.getMotivo_consulta());
			model.put("diagnostico", informe.getDiagnostico());
			model.put("cita", informe.getCita());

			return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
		}
	}

	@PostMapping(value = "/informes/{informeId}/edit")
	public String processUpdateInformeForm(final Cita cita, @Valid final Informe informe, final BindingResult result, @PathVariable("informeId") final int informeId, final ModelMap model) throws IllegalAccessException {
		if (result.hasErrors()) {
			model.put(InformeController.INFORME_MODELO, informe);
			return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
		} else {
			informe.setId(informeId);
			this.informeService.updateInforme(informe);
		}
		return InformeController.REDIRECTION_TO_CITA + informe.getCita().getPaciente().getMedico().getId();
	}


	public Informe checkInformeIsPresent(int informeId){
		return this.informeService.findInformeById(informeId).orElse(null);
	}

	public boolean sameMedico(Informe informe){
		return informe.getCita().getPaciente().getMedico().equals(this.userService.getCurrentMedico());
		}

	public boolean sameMedico(Cita cita){
		return cita.getPaciente().getMedico().equals(this.userService.getCurrentMedico());
	}

	public boolean hasCitaInforme(Cita cita){
		return this.informeService.citaHasInforme(cita);
	}
}
