package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import javax.management.InvalidAttributeValueException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.TratamientoService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class TratamientoE2ETest {
	
	@Autowired
	private MockMvc				mockMvc;
	
	@Autowired
	private InformeService informeService;
	@Autowired
	private TratamientoService tratamientoService;
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private MedicoService medicoService;
	@Autowired
	private CitaService citaService;
	
	
	private static int TEST_INFORME_ID = 1 ;
	private static int TEST_INFORME_ID2 = 1;
	private static int TEST_TRATAMIENTO_ID = 1;
	private static int TEST_TRATAMIENTO_ID2 = 1;
	private static int TEST_TRATAMIENTO_ID3 = 1;
	
	@BeforeEach
	void setup() throws InvalidAttributeValueException{
		
		Cita cita = citaService.findCitaById(1).get();
		cita.setFecha(LocalDate.now());
		citaService.save(cita);
		
	
	}
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitCreateTratamientoForm() throws Exception {
		mockMvc.perform(get("/tratamientos/new/{informeId}", TEST_INFORME_ID))
				.andExpect(status().isOk())
				//.andExpect(model().attributeExists("tratamiento"))
				//.andExpect(model().attributeExists("informe"))
				.andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"));
		
	}	
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitUpdateTratamientoForm() throws Exception {
		Tratamiento tratamiento = tratamientoService.findTratamientoById(TEST_TRATAMIENTO_ID).get() ;
		mockMvc.perform(get("/tratamientos/{tratamientoId}/edit", TEST_TRATAMIENTO_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("tratamiento"))
				.andExpect(model().attributeExists("informe"))
				.andExpect(model().attribute("tratamiento", hasProperty("medicamento", is(tratamiento.getMedicamento()))))
				.andExpect(model().attribute("tratamiento", hasProperty("dosis", is(tratamiento.getDosis()))))
				.andExpect(model().attribute("tratamiento", hasProperty("f_inicio_tratamiento", is(tratamiento.getF_inicio_tratamiento()))))
				.andExpect(model().attribute("tratamiento", hasProperty("f_fin_tratamiento", is(tratamiento.getF_fin_tratamiento()))))
				.andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"));
	}	
	
	//CASO POSITIVO
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testSaveSuccessTratamiento() throws Exception {
        
		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(informeService.findInformeById(TEST_INFORME_ID).get());
		tratamiento.setDosis("dosis de prueba");
		tratamiento.setMedicamento("medicamento de prueba");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-10"));
		
        mockMvc.perform(post("/tratamientos/save")
        		.with(csrf())
        	    .flashAttr("tratamiento", tratamiento))
		//.andExpect(status().isOk()) redirige bien pero no es ok
		.andExpect(view().name("redirect:/citas/1/informes/1"));	

	}
	
	//Caso fecha fin en pasado
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
    void testSaveTratamientoFechaFinPasado() throws Exception{

		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(informeService.findInformeById(TEST_INFORME_ID).get());
		tratamiento.setDosis("dosis de prueba");
		tratamiento.setMedicamento("medicamento de prueba");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-01-10"));
		
        mockMvc.perform(post("/tratamientos/save")
        		.with(csrf())
        	    .flashAttr("tratamiento", tratamiento))
        .andExpect(model().attributeHasFieldErrors("tratamiento","f_fin_tratamiento"))
		.andExpect(status().isOk())
        .andExpect(view().name("tratamientos/createOrUpdateTratamientosForm")
        );
}
	
	//Caso fecha inicio en futuro
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
    void testSaveTratamientoFechaInicioFuturo() throws Exception{
		
		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(informeService.findInformeById(TEST_INFORME_ID).get());
		tratamiento.setDosis("dosis test");
		tratamiento.setMedicamento("medicamento test");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-12-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-22"));
		
        mockMvc.perform(post("/tratamientos/save")
        		.with(csrf())
        	    .flashAttr("tratamiento", tratamiento))
        
        .andExpect(model().attributeHasFieldErrors("tratamiento","f_inicio_tratamiento"))
        .andExpect(status().isOk())
        .andExpect(view().name("tratamientos/createOrUpdateTratamientosForm")
        );

    }

	
	//CASO CAMPOS VACIOS
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
    void testSaveTratamientoNullInputs() throws Exception{
		
		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(informeService.findInformeById(TEST_INFORME_ID).get());
		tratamiento.setDosis("");
		tratamiento.setMedicamento("");
		tratamiento.setF_inicio_tratamiento(null);
		tratamiento.setF_fin_tratamiento(null);
		
        mockMvc.perform(post("/tratamientos/save")
        		.with(csrf())
        	    .flashAttr("tratamiento", tratamiento))
        /*
        .andExpect(model().attributeHasFieldErrors("tratamiento","medicamento"))
        .andExpect(model().attributeHasFieldErrors("tratamiento","dosis"))
        .andExpect(model().attributeHasFieldErrors("tratamiento","f_inicio_tratamiento"))
        .andExpect(model().attributeHasFieldErrors("tratamiento","f_fin_tratamiento"))
        */
        .andExpect(status().isOk())
        //.andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"))
        ;
	}

	//CASO EDITAR (INIT) TRATAMIENTO NO VIGENTE
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitUpdateTratamientoNoVigente() throws Exception {
		mockMvc.perform(get("/tratamientos/{tratamientoId}/edit", 4))
				.andExpect(status().isOk())
				//.andExpect(view().name("redirect:/"))
				;
	}
	
	// CASO CREAR (INIT) TRATAMIENTO A INFORME DE OTRO MEDICO
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitCreateTratamientoInformeOtroMedico() throws Exception {
		mockMvc.perform(get("/tratamientos/new/{informeId}", 4))
				.andExpect(view().name("redirect:/"));
	}
	
	// CASO UPDATE (INIT) TRATAMIENTO DE OTRO MEDICO     peta da igual lo que haga
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitUpdateTratamientoOtroMedico() throws Exception {
		mockMvc.perform(get("/tratamientos/{tratamientoId}/edit", 7))
				.andExpect(view().name("redirect:/"));
	}



}
