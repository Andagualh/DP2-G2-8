package org.springframework.samples.petclinic.web;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.TratamientoService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tratamientos")
public class TratamientoController {
	
	@Autowired
	private TratamientoService		tratamientoService;
	@Autowired
	private InformeService				informeService;
	
	private static final String		VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM	= "tratamientos/createOrUpdateTratamientosForm";
	
	@GetMapping(value = "/{tratamientoId}/edit")
	public String initUpdateTratamientosForm(@PathVariable("tratamientoId") final int tratamientoId, final Model model) {
		Tratamiento tratamiento = this.tratamientoService.findTratamientoById(tratamientoId).get();
		Informe informe = this.informeService.findInformeById(tratamiento.getId()).get();
		model.addAttribute("informe", informe);
		model.addAttribute("tratamiento", tratamiento);
		return TratamientoController.VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/save")
	public String saveTratamiento(@Valid final Tratamiento tratamiento, final BindingResult result) {
		
		boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("admin"));

		if (result.hasErrors()) {
			System.out.println("ERRORES: "+result.getAllErrors());
			return TratamientoController.VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM;
		} else if (isAdmin) {
			tratamiento.setId(tratamiento.getId());
			this.tratamientoService.save(tratamiento);
			return "redirect:/tratamientos/{tratamientoId}";
		} else {
			tratamiento.setId(tratamiento.getId());
			tratamiento.setInforme(informeService.findInformeById(tratamiento.getInforme().getId()).get());
			this.tratamientoService.save(tratamiento);
			//modelMap.addAttribute("message", "Tratamiento successfully edited");
			return "redirect:/pacientes";
		}
	}

}
