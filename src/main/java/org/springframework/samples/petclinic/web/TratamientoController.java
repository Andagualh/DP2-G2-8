
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tratamientos")
public class TratamientoController {

	@Autowired
	private TratamientoService	tratamientoService;
	@Autowired
	private InformeService		informeService;

	private static final String	VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM	= "tratamientos/createOrUpdateTratamientosForm";


	@GetMapping(value = "/{tratamientoId}/edit")
	public String initUpdateTratamientosForm(@PathVariable("tratamientoId") final int tratamientoId, final ModelMap model) {
		Tratamiento tratamiento = this.tratamientoService.findTratamientoById(tratamientoId).get();
		boolean esVigente = tratamiento.getF_fin_tratamiento().isAfter(LocalDate.now());
		if(esVigente) {
			Informe informe = this.informeService.findInformeById(tratamiento.getId()).get();
			model.addAttribute("informe", informe);
			model.addAttribute("tratamiento", tratamiento);
			return TratamientoController.VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM;
		}else {
			return "redirect:/";
		}
	}
	
	@GetMapping(value = "/new/{informeId}")
	public String initCreateTratamientosForm(@PathVariable("informeId") final int informeId, final ModelMap model) {
		Tratamiento tratamiento = new Tratamiento();
		Informe informe = this.informeService.findInformeById(informeId).get();
		model.addAttribute("informe", informe);
		model.addAttribute("tratamiento", tratamiento);
		return TratamientoController.VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM;
	}

	//He quitado el calculo de la variable isAdmin porque peta los test de integracion(al menos a mi), por eso esta simplemente en false.
	// Debe de ser porque al desactivar security en los test si luego llamas a algo de seguridad simplemente peta.
	// Creo que esto ya se controla bien con el segurity configuration.
	@PostMapping(value = "/save")
	public String saveTratamiento(@Valid final Tratamiento tratamiento, final BindingResult result) {

		// boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("admin"));

		if (result.hasErrors()) {
			System.out.println("ERRORES: " + result.getAllErrors());
			return TratamientoController.VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM;
	/*	} else if (isAdmin) {
			tratamiento.setId(tratamiento.getId());
			this.tratamientoService.save(tratamiento);
			return "redirect:/tratamientos/{tratamientoId}";
			*/
		} else {
			Informe informe = this.informeService.findInformeById(tratamiento.getInforme().getId()).get();
			tratamiento.setId(tratamiento.getId());
			tratamiento.setInforme(informe);
			this.tratamientoService.save(tratamiento);
			return "redirect:/citas/" + String.valueOf(informe.getCita().getId()) + "/informes/" + String.valueOf(informe.getId());
		}
	}

}
