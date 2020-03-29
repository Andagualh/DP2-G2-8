package org.springframework.samples.petclinic.service;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.repository.PacienteRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class HistoriaClinicaServiceTest {
	
	@Autowired
	private HistoriaClinicaService historiaService;
	
	@Autowired
	private PacienteRepository pacienteRepo;
	
	@Test
	public void testCountWithInitialData() {
		int count = historiaService.historiaClinicaCount();
		assertEquals(count,0);
	}

	@BeforeAll
	void setup() {
		
		HistoriaClinica hC = new HistoriaClinica();
		hC.setDescripcion("Test description");
		hC.setName("Historia de test");
		hC.setId(2);
		hC.setPaciente(pacienteRepo.findById(1).get());
	}
	
	
	
	
}
