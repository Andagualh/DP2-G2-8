package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	private static final String		VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM	= "null";
	@Autowired
	private final PacienteService	pacienteService;
	private final MedicoService		medicoService;

    //@Autowired
	//public PacienteController(PacienteService pacienteService, UserService userService, AuthoritiesService authoritiesService) {
	//	this.pacienteService = pacienteService;
   // }



	@Autowired
	public PacienteController(final PacienteService pacienteService, final MedicoService medicoService, final UserService userService, final AuthoritiesService authoritiesService) {
		this.pacienteService = pacienteService;
		this.medicoService = medicoService;
	}

    @InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

    @GetMapping(value = "/pacientes/{pacienteId}")
    public ModelAndView showPaciente(@PathVariable("pacienteId") int pacienteId){
        ModelAndView mav = new ModelAndView("pacientes/pacienteDetails");
        mav.addObject(this.pacienteService.findPacienteById(pacienteId));
        return mav;
    }

    @GetMapping(value = "/pacientes/findbymedico/{medicoId}")
    public String initFindMedForm(Map<String, Object> model, @PathVariable("medicoId") int medicoId){
        
        Collection<Paciente> results = this.pacienteService.findPacienteByMedicoId(medicoId);
        if(results.isEmpty()){
           //Falta la página de error
            return "redirect:/pacientes/";
        }
       
        else{
            model.put("selections", results);
            
            return "pacientes/pacientesList";
        }
    }

	@GetMapping(value = "/paciente/save/{pacienteId}")
	public String savePaciente(@Valid final Paciente paciente, final BindingResult result, final ModelMap modelMap) {
		String view = "/pacientes";

		if (result.hasErrors()) {
			modelMap.addAttribute("paciente", paciente);
			return "pacientes/editPaciente";
		} else {
			this.pacienteService.savePacienteByMedico(paciente, 1);
			modelMap.addAttribute("message", "Paciente guardado exitosamente");
		}

		return view;
	}

	@GetMapping(value = "/paciente/delete/{pacienteId}")
	public String borrarPaciente(@PathParam("pacienteId") final int pacienteId, final ModelMap modelMap) {
		String view = "/pacientes";
		Optional<Paciente> paciente = this.pacienteService.findPacienteById(pacienteId);
		String id = SecurityContextHolder.getContext().getAuthentication().getName();
		if (paciente.isPresent()) {
			this.pacienteService.deletePacienteByMedico(pacienteId, 1);
			modelMap.addAttribute("message", "Paciente borrado exitosamiente");
		} else {
			modelMap.addAttribute("message", "Paciente no encontrado");
		}

		return view;
	}

	@GetMapping(value = "/pacientes/{pacienteId}/edit")
	public String initUpdatePacientesForm(@PathVariable("pacientesId") final int pacientesId, final Model model) {
		Paciente paciente = this.pacienteService.findPacienteById(pacientesId).get();
		model.addAttribute(paciente);
		return "pacientes/createOrUpdateOwnerForm";
	}

	@PostMapping(value = "/pacientes/{pacienteId}/edit")
	public String processUpdatePacienteForm(@Valid final Paciente paciente, final BindingResult result, @PathVariable("pacienteId") final int pacienteId) {
		if (result.hasErrors()) {
			return "pacientes/createOrUpdateOwnerForm";
		} else {
			paciente.setId(pacienteId);
			this.pacienteService.savePacienteByMedico(paciente, 1);
			return "redirect:/pacientes/{pacienteId}";
		}
	}

}
