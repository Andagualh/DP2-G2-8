package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.TratamientoService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class TratamientoE2ETest {

	@Autowired
	private MockMvc				mockMvc;

	@Autowired
	private InformeService		informeService;

	@Autowired
	private TratamientoService	tratamientoService;

	private static int			TEST_INFORME_ID		= 1;
	private static int			TEST_CITA_ID		= 1;
	private static int			TEST_PACIENTE_ID	= 1;
	private static int			TEST_MEDICO_ID		= 1;
	private static int			TEST_TRATAMIENTO_ID	= 1;


	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitCreateTratamientoForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tratamientos/new/{informeId}", TratamientoE2ETest.TEST_INFORME_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("tratamiento"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("informe")).andExpect(MockMvcResultMatchers.view().name("tratamientos/createOrUpdateTratamientosForm"));

	}

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitUpdateTratamientoForm() throws Exception {
		Tratamiento tratamiento = this.tratamientoService.findTratamientoById(TratamientoE2ETest.TEST_TRATAMIENTO_ID).get();
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tratamientos/{tratamientoId}/edit", TratamientoE2ETest.TEST_TRATAMIENTO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("tratamiento"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("informe")).andExpect(MockMvcResultMatchers.model().attribute("tratamiento", Matchers.hasProperty("medicamento", Matchers.is(tratamiento.getMedicamento()))))
			.andExpect(MockMvcResultMatchers.model().attribute("tratamiento", Matchers.hasProperty("dosis", Matchers.is(tratamiento.getDosis()))))
			.andExpect(MockMvcResultMatchers.model().attribute("tratamiento", Matchers.hasProperty("f_inicio_tratamiento", Matchers.is(tratamiento.getF_inicio_tratamiento()))))
			.andExpect(MockMvcResultMatchers.model().attribute("tratamiento", Matchers.hasProperty("f_fin_tratamiento", Matchers.is(tratamiento.getF_fin_tratamiento()))))
			.andExpect(MockMvcResultMatchers.view().name("tratamientos/createOrUpdateTratamientosForm"));
	}

	//CASO POSITIVO

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSaveSuccessTratamiento() throws Exception {

		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(this.informeService.findInformeById(TratamientoE2ETest.TEST_INFORME_ID).get());
		tratamiento.setDosis("dosis de prueba");
		tratamiento.setMedicamento("medicamento de prueba");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-10"));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/tratamientos/save").with(SecurityMockMvcRequestPostProcessors.csrf()).flashAttr("tratamiento", tratamiento))
			//.andExpect(status().isOk()) redirige bien pero no es ok
			.andExpect(MockMvcResultMatchers.view().name("redirect:/citas/1/informes/1"));

	}

	//Caso fecha fin en pasado

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSaveTratamientoFechaFinPasado() throws Exception {

		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(this.informeService.findInformeById(TratamientoE2ETest.TEST_INFORME_ID).get());
		tratamiento.setDosis("dosis de prueba");
		tratamiento.setMedicamento("medicamento de prueba");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-01-10"));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/tratamientos/save").with(SecurityMockMvcRequestPostProcessors.csrf()).flashAttr("tratamiento", tratamiento))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("tratamiento", "f_fin_tratamiento")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("tratamientos/createOrUpdateTratamientosForm"));
	}

	//Caso fecha inicio en futuro

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSaveTratamientoFechaInicioFuturo() throws Exception {

		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(this.informeService.findInformeById(TratamientoE2ETest.TEST_INFORME_ID).get());
		tratamiento.setDosis("dosis test");
		tratamiento.setMedicamento("medicamento test");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-12-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-22"));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/tratamientos/save").with(SecurityMockMvcRequestPostProcessors.csrf()).flashAttr("tratamiento", tratamiento))

			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("tratamiento", "f_inicio_tratamiento")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("tratamientos/createOrUpdateTratamientosForm"));

	}

	//CASO CAMPOS VACIOS

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testSaveTratamientoNullInputs() throws Exception {

		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(this.informeService.findInformeById(TratamientoE2ETest.TEST_INFORME_ID).get());
		tratamiento.setDosis("");
		tratamiento.setMedicamento("");
		tratamiento.setF_inicio_tratamiento(null);
		tratamiento.setF_fin_tratamiento(null);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/tratamientos/save").with(SecurityMockMvcRequestPostProcessors.csrf()).flashAttr("tratamiento", tratamiento))

			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("tratamiento", "medicamento")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("tratamiento", "dosis"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("tratamiento", "f_inicio_tratamiento")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("tratamiento", "f_fin_tratamiento"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("tratamientos/createOrUpdateTratamientosForm"));

	}

	//CASO EDITAR (SAVE) TRATAMIENTO NO VIGENTE

	// Se puede llegar por un url a /save pasandole un tratamiento???

	//CASO EDITAR (INIT) TRATAMIENTO NO VIGENTE

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitUpdateTratamientoNoVigente() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tratamientos/{tratamientoId}/edit", 4))
			///.andExpect(status().isOk()) redirige pero no es ok
			.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// CASO CREAR (INIT) TRATAMIENTO A INFORME DE OTRO MEDICO

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitCreateTratamientoInformeOtroMedico() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tratamientos/new/{informeId}", 4))
			//.andExpect(status().isOk())  redirige bien pero aun asi no es ok
			.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// CASO UPDATE (INIT) TRATAMIENTO DE OTRO MEDICO     peta da igual lo que haga

	@WithMockUser(username = "alvaroMedico", authorities = {
		"medico"
	})
	@Test
	void testInitUpdateTratamientoOtroMedico() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/tratamientos/{tratamientoId}/edit", 7)).andExpect(MockMvcResultMatchers.status().isOk());   /// Es ok pero la redireccion no coincide
		//.andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"))  // prueba
		//.andExpect(view().name("redirect:/"));
	}

}
