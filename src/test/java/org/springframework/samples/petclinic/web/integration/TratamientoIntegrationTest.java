package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.TratamientoService;
import org.springframework.samples.petclinic.web.TratamientoController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TratamientoIntegrationTest {

	@Autowired
	private TratamientoController tratamientoController;
	
	@Autowired
	private TratamientoService tratamientoService;
	
	@Autowired
	private InformeService informeService;
	
	private final int TEST_INFORME_ID = 1;
	private final int TEST_TRATAMIENTO_ID = 1;
	
	@Test
	void testInitUpdateTratamientosForm() throws Exception {
		ModelMap model= new ModelMap();
		
		String view = tratamientoController.initUpdateTratamientosForm(TEST_TRATAMIENTO_ID, model);
		
		assertEquals(view,"tratamientos/createOrUpdateTratamientosForm");
		assertNotNull(model.get("tratamiento"));
		assertNotNull(model.get("informe"));
	}
	
	@Test
	void testInitCreateTratamientosForm() throws Exception {
		ModelMap model= new ModelMap();
		
		String view = tratamientoController.initCreateTratamientosForm(TEST_INFORME_ID, model);
		
		assertEquals(view,"tratamientos/createOrUpdateTratamientosForm");
		assertNotNull(model.get("tratamiento"));
		assertNotNull(model.get("informe"));
	}
	
	@Test
	void testSaveTratamiento() throws Exception {
		BindingResult result= new MapBindingResult(Collections.emptyMap(),"");
		Tratamiento tratamiento = new Tratamiento();
		Informe informe = informeService.findInformeById(TEST_INFORME_ID).get();
		
		tratamiento.setName("name test");
		tratamiento.setMedicamento("Medicamento test");
		tratamiento.setDosis("Dosis Test");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-05-05"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-06-15"));
		tratamiento.setInforme(informe);
		
		String view = tratamientoController.saveTratamiento(tratamiento, result);
		
		assertEquals(view,"redirect:/citas/" + String.valueOf(informe.getCita().getId()) + "/informes/" + String.valueOf(informe.getId()));
	}
	
	@Test
	void testSaveTratamientoWithNullInputs() throws Exception {
		BindingResult result= new MapBindingResult(Collections.emptyMap(),"");
		Tratamiento tratamiento = new Tratamiento();
		Informe informe = informeService.findInformeById(TEST_INFORME_ID).get();
		
		tratamiento.setName("");
		tratamiento.setMedicamento("");
		tratamiento.setDosis("");
		tratamiento.setF_inicio_tratamiento(null);
		tratamiento.setF_fin_tratamiento(null);
		tratamiento.setInforme(informe);
		
		result.reject("medicamento", "El campo no debe estar vacio.");
		result.reject("dosis", "El campo no debe estar vacio.");
		result.reject("f_inicio_tratamiento", "El campo no debe estar vacio.");
		result.reject("f_fin_tratamiento", "El campo no debe estar vacio.");
		
		String view = tratamientoController.saveTratamiento(tratamiento, result);
		
		assertEquals(view,"tratamientos/createOrUpdateTratamientosForm");
	}
	
	@Test
	void testSaveTratamientoWithFechaFinPasado() throws Exception {
		BindingResult result= new MapBindingResult(Collections.emptyMap(),"");
		Tratamiento tratamiento = new Tratamiento();
		Informe informe = informeService.findInformeById(TEST_INFORME_ID).get();
		
		tratamiento.setName("name test");
		tratamiento.setMedicamento("Medicamento test");
		tratamiento.setDosis("Dosis Test");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-05-05"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-01-15"));
		tratamiento.setInforme(informe);
		
		result.reject("f_fin_tratamiento", "La fecha debe estar en presente o en futuro.");
		
		String view = tratamientoController.saveTratamiento(tratamiento, result);
		
		assertEquals(view,"tratamientos/createOrUpdateTratamientosForm");
	}
	
	@Test
	void testSaveTratamientoWithFechaInicioFuturo() throws Exception {
		BindingResult result= new MapBindingResult(Collections.emptyMap(),"");
		Tratamiento tratamiento = new Tratamiento();
		Informe informe = informeService.findInformeById(TEST_INFORME_ID).get();
		
		tratamiento.setName("name test");
		tratamiento.setMedicamento("Medicamento test");
		tratamiento.setDosis("Dosis Test");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-10-05"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-09-15"));
		tratamiento.setInforme(informe);
		
		result.reject("f_inicio_tratamiento", "La fecha debe estar en presente o en pasado.");
		
		String view = tratamientoController.saveTratamiento(tratamiento, result);
		
		assertEquals(view,"tratamientos/createOrUpdateTratamientosForm");
	}
	
	
	
}
