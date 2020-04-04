
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

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
}
