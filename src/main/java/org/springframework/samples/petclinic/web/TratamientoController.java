
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.TratamientoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
	@Autowired
	private UserService		userService;
	

	private static final String	VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM	= "tratamientos/createOrUpdateTratamientosForm";

	
	public boolean authorizeTratamiento(Informe informe) {
		return userService.getCurrentMedico().getId().equals(informe.getCita().getPaciente().getMedico().getId());
	}

	//TODO: Comparar con Cita Fecha, no con final de tratamiento
	
	private boolean esVigente(Tratamiento tratamiento) {
		return tratamiento.getInforme().getCita().getFecha().isEqual(LocalDate.now());
	}
	
	@GetMapping(value = "/{tratamientoId}/edit")
	public String initUpdateTratamientosForm(@PathVariable("tratamientoId") final int tratamientoId, final ModelMap model) {
		Tratamiento tratamiento = this.tratamientoService.findTratamientoById(tratamientoId).get();
		Informe informe = this.informeService.findInformeById(tratamiento.getId()).get();
		boolean esVigente = esVigente(tratamiento);
		boolean autorizado = authorizeTratamiento(informe);
		if(esVigente && autorizado) {
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
		boolean autorizado = authorizeTratamiento(informe);
		boolean esVigente = esVigente(tratamiento);
		if(autorizado && esVigente) {
			model.addAttribute("informe", informe);
			model.addAttribute("tratamiento", tratamiento);
			
			return TratamientoController.VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM;
		}else {
			return "redirect:/";
		}
	}

	@PostMapping(value = "/save")
	public String saveTratamiento(@Valid final Tratamiento tratamiento, final BindingResult result, final ModelMap modelMap) {
        boolean fechasCorrectas = tratamiento.getF_fin_tratamiento().isAfter(tratamiento.getF_inicio_tratamiento());
		if(!authorizeTratamiento(tratamiento.getInforme())){
			return "accessNotAuthorized";
		}else if(!fechasCorrectas){
			result.rejectValue("f_fin_tratamiento", "error.fechasErroneas", "La fecha de inicio es mayor a fecha de fin.");
			result.rejectValue("f_inicio_tratamiento", "error.fechasErroneas", "La fecha de inicio es mayor a fecha de fin.");
			return TratamientoController.VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM;
		}else if(result.hasErrors()) {
			modelMap.addAttribute("informe", tratamiento.getInforme());
			modelMap.addAttribute("tratamiento", tratamiento);
			return TratamientoController.VIEWS_TRATAMIENTOS_CREATE_OR_UPDATE_FORM;
		} else {
			Informe informe = this.informeService.findInformeById(tratamiento.getInforme().getId()).get();
			tratamiento.setId(tratamiento.getId());
			tratamiento.setInforme(informe);
			this.tratamientoService.save(tratamiento);
			return "redirect:/citas/" + String.valueOf(informe.getCita().getId()) + "/informes/" + String.valueOf(informe.getId());
		}
	}

}
