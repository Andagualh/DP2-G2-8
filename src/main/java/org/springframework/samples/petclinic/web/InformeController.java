
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
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
import org.springframework.web.bind.annotation.RequestParam;
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
	private final TratamientoService		tratamientoService;


	@Autowired
	public InformeController(final PacienteService pacienteService, final MedicoService medicoService, final UserService userService, final AuthoritiesService authoritiesService, final CitaService citaService,
		final HistoriaClinicaService historiaClinicaService, final InformeService informeService, final TratamientoService tratamientoService) {
		this.informeService = informeService;
		this.pacienteService = pacienteService;
		this.medicoService = medicoService;
		this.userService = userService;
		this.citaService = citaService;
		this.historiaClinicaService = historiaClinicaService;
		this.tratamientoService = tratamientoService;
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
	@GetMapping(path = "informes/delete/{informeId}")
	public String borrarInforme(@PathVariable("informeId") final int informeId, final ModelMap modelMap)
			throws DataAccessException, IllegalAccessException {
		Optional<Informe> informe = this.informeService.findInformeById(informeId);

		if (informe.get().getHistoriaClinica() == null) {
			this.informeService.deleteInforme(informeId);
			modelMap.addAttribute("message", "Informe succesfully deleted");
		} else {
			modelMap.addAttribute("message", "Informe has not a clinic history");
		}
		return "redirect:/";
	}

	@GetMapping(value = "/informes/{informeId}")
	public ModelAndView showInforme(@PathVariable("informeId") final int informeId) {
		ModelAndView mav = new ModelAndView("informes/informeDetails");
		Informe informe = this.informeService.findInformeById(informeId).get();

		mav.addObject(informe);

		Cita cita = this.citaService.findCitaById(informe.getCita().getId()).get();
		Paciente paciente = cita.getPaciente();
		mav.addObject(paciente);

		Boolean canBeDeleted = informe.getCita().getFecha().plusDays(1).equals(LocalDate.now().plusDays(1)) && informe.getHistoriaClinica() == null;
		mav.getModel().put("cannotbedeleted", !canBeDeleted);

		Collection<Tratamiento> tratamientos = this.tratamientoService.findTratamientosByInforme(informe);
		mav.getModel().put("tratamientos", tratamientos);

		//		Boolean informeInHistoriaClinica = informe.getHistoriaClinica() != null;
		//		mav.getModel().put("informeInHistoriaClinica", informeInHistoriaClinica);

		Boolean canBeEdited = informe.getCita().getFecha().equals(LocalDate.now());
		mav.getModel().put("canbeedited", canBeEdited);

		return mav;
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
		
		System.out.println("citahola"+cita);
		System.out.println("informehola"+informe.getDiagnostico()+"-"+informe.getMotivo_consulta());
		if (result.hasErrors()) {
			model.put("informe", informe);
			return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
		} else {
			informe.setCita(cita);
			this.informeService.saveInforme(informe);
		}
		return "redirect:/citas/" + informe.getCita().getPaciente().getMedico().getId();
	}

	@GetMapping(value = "/informes/{informeId}/addtohistoriaclinica")
	public String addHistoriaClinicaToInforme(@PathVariable("informeId") final int informeId) throws DataAccessException, IllegalAccessException {
		Informe informe = this.informeService.findInformeById(informeId).get();
		HistoriaClinica hc = this.historiaClinicaService.findHistoriaClinicaByPaciente(informe.getCita().getPaciente());
		informe.setHistoriaClinica(hc);
		this.informeService.saveInformeWithHistoriaClinica(informe);
		return "redirect:/pacientes/" + informe.getCita().getPaciente().getId() + "/historiaclinica";
	}

	@GetMapping(value = "/informes/{informeId}/detelefromhistoriaclinica")
	public String deleteFromHistoriaClinica(@PathVariable("informeId") final int informeId) throws DataAccessException, IllegalAccessException {
		Informe informe = this.informeService.findInformeById(informeId).get();
		this.informeService.deleteInformeToHistoriaClinica(informe);
		return "redirect:/citas/" + informe.getCita().getPaciente().getMedico().getId() + "/informes/" + informe.getId();
	}

	@GetMapping(value = "/informes/{informeId}/edit")
	public String initUpdateInformeForm(@PathVariable("informeId") final int informeId, final ModelMap model) {

		Informe informe = this.informeService.findInformeById(informeId).get();
		model.put("informe", informe);
		model.put("motivo_consulta", informe.getMotivo_consulta());
		model.put("diagnostico", informe.getDiagnostico());
		model.put("cita", informe.getCita());

		return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/informes/{informeId}/edit")
	public String processUpdateInformeForm(final Cita cita, @Valid final Informe informe, final BindingResult result ,@PathVariable("informeId") final int informeId, final ModelMap model) throws DataAccessException, IllegalAccessException {
		if (result.hasErrors()) {
			model.put("informe", informe);
			return InformeController.VIEWS_INFORME_CREATE_OR_UPDATE_FORM;
		} else {
			
			informe.setId(informeId);
			this.informeService.saveInforme(informe);
		}
		return "redirect:/citas/" + informe.getCita().getPaciente().getMedico().getId();
	}

}
