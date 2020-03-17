package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PacienteController {

    private static final String VIEWS_PACIENTE_CREATE_OR_UPDATE_FORM = "null";
    @Autowired
    private final PacienteService pacienteService;

    @Autowired
	public PacienteController(PacienteService pacienteService, UserService userService, AuthoritiesService authoritiesService) {
		this.pacienteService = pacienteService;
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
    
    

}