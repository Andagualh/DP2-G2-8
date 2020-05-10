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
	@Autowired 
	private InformeService   		informeService;
	
	@Test
	public void testCountWithInitialData() {
		int count = this.tratamientoService.tratamientoCount();
		Assertions.assertEquals(count, 3);
	}
	
	@Test
	public void testCreateTratamiento() {
		
		Tratamiento tratamiento = new Tratamiento();
		
		tratamiento.setMedicamento("aspirina1");
		tratamiento.setDosis("1 pastilla cada 8 horas");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-23"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-25"));
		tratamiento.setInforme(informeService.findInformeById(1).get());

		// De esta forma el test no se rompe cuando añades mas beans en el data
		int initCount = this.tratamientoService.tratamientoCount();
		
		this.tratamientoService.save(tratamiento);
		
		int postCount = this.tratamientoService.tratamientoCount();
		
		Assertions.assertEquals(postCount, initCount + 1);
	}
	
	@Test
	public void testCreateTratamientoWithoutInforme() {
		
		Tratamiento tratamiento = new Tratamiento();
		
		tratamiento.setMedicamento("aspirina1");
		tratamiento.setDosis("1 pastilla cada 8 horas");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-23"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-25"));
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.tratamientoService.save(tratamiento);
		});
	}
	
	@Test
	public void testCreateTratamientoNullFecha() {
		
		Tratamiento tratamiento = new Tratamiento();
		
		tratamiento.setMedicamento("aspirina1");
		tratamiento.setDosis("1 pastilla cada 8 horas");
		tratamiento.setF_inicio_tratamiento(null);
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-25"));
		tratamiento.setInforme(informeService.findInformeById(1).get());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.tratamientoService.save(tratamiento);
		});
	}
	
	@Test
	public void testCreateTratamientoFechaFinPasado() {
		
		Tratamiento tratamiento = new Tratamiento();
		
		tratamiento.setMedicamento("aspirina1");
		tratamiento.setDosis("1 pastilla cada 8 horas");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-01-23"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-01-22"));
		tratamiento.setInforme(informeService.findInformeById(1).get());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.tratamientoService.save(tratamiento);
		});
	}
	
	@Test
	public void testCreateTratamientoNullMedicamento() {
		
		Tratamiento tratamiento = new Tratamiento();
		
		tratamiento.setMedicamento("");
		tratamiento.setDosis("1 pastilla cada 8 horas");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-01-23"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-22"));
		tratamiento.setInforme(informeService.findInformeById(1).get());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.tratamientoService.save(tratamiento);
		});
	}
	
	@Test
	public void testCreateTratamientoNullInputs() {
		
		Tratamiento tratamiento = new Tratamiento();
		
		tratamiento.setMedicamento("");
		tratamiento.setDosis("");
		tratamiento.setF_inicio_tratamiento(null);
		tratamiento.setF_fin_tratamiento(null);
		tratamiento.setInforme(informeService.findInformeById(1).get());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.tratamientoService.save(tratamiento);
		});
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
	
	@Test
	public void testUpdateTratamientoWithNullDosis() {
		
		Tratamiento tratamiento = tratamientoService.findTratamientoById(2).get();

		Assertions.assertNotNull(tratamiento);
		
		tratamiento.setMedicamento("aspirina");
		tratamiento.setDosis(null);
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-22"));

		this.tratamientoService.save(tratamiento);
		
		/*
	
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.tratamientoService.save(tratamiento);
		});
		*/
	}
	
	@Test
	public void testUpdateTratamientoWithFechaFinPasado() {
		
		Tratamiento tratamiento = tratamientoService.findTratamientoById(3).get();

		Assertions.assertNotNull(tratamiento);
		
		tratamiento.setMedicamento("aspirina");
		tratamiento.setDosis("dosis test");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-01-20"));

		this.tratamientoService.save(tratamiento);

		/*
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.tratamientoService.save(tratamiento);
		});
		*/
	}

}
