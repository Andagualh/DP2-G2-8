package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TratamientoServiceTest {
	
	@Autowired
	private TratamientoService		tratamientoService;
	
	@Test
	public void testCountWithInitialData() {
		int count = this.tratamientoService.tratamientoCount();
		Assertions.assertEquals(count, 3);
	}
	
	@Test
	public void testUpdateTratamiento() {
		
		Tratamiento tratamiento = tratamientoService.findTratamientoById(2).get();

		Assertions.assertNotNull(tratamiento);
		
		tratamiento.setMedicamento("aspirina");
		tratamiento.setDosis("1 pastilla cada 12 horas");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-22"));

		this.tratamientoService.save(tratamiento);

		Assert.assertTrue(this.tratamientoService.findTratamientoById(2).get().getMedicamento().equals("aspirina"));
		Assert.assertTrue(this.tratamientoService.findTratamientoById(2).get().getDosis().equals("1 pastilla cada 12 horas"));
		Assert.assertTrue(this.tratamientoService.findTratamientoById(2).get().getF_inicio_tratamiento().equals(LocalDate.parse("2020-04-22")));
		Assert.assertTrue(this.tratamientoService.findTratamientoById(2).get().getF_fin_tratamiento().equals(LocalDate.parse("2020-10-22")));
	}

}
