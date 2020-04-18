
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

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

@Controller
public class InformeController {

	private static final String				VIEWS_INFORME_CREATE_OR_UPDATE_FORM	= "informes/createOrUpdateInformesForm";
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

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	//	@GetMapping(value = "/pets/new")
	//	public String initCreationForm(Owner owner, ModelMap model) {
	//		Pet pet = new Pet();
	//		owner.addPet(pet);
	//		model.put("pet", pet);
	//		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	//	}
	//
	//	@PostMapping(value = "/pets/new")
	//	public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) {		
	//		if (result.hasErrors()) {
	//			model.put("pet", pet);
	//			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	//		}
	//		else {
	//                    try{
	//                    	owner.addPet(pet);
	//                    	this.petService.savePet(pet);
	//                    }catch(DuplicatedPetNameException ex){
	//                        result.rejectValue("name", "duplicate", "already exists");
	//                        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	//                    }
	//                    return "redirect:/owners/{ownerId}";
	//		}
	//	}
	
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
		
		return mav;
	}
	
	
}
