package org.springframework.samples.petclinic.service;



import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class HistoriaClinicaServiceTest {
	
	@Autowired
	private HistoriaClinicaService historiaClinicaService;
	@Autowired
	private MedicoService medicoService;
	@Autowired
	private PacienteService	pacienteService;

	public Medico createDummyMedico() {
		Medico medico = new Medico();
		User medicoUser = new User();
		Authorities authorities = new Authorities();

		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678A");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");
		medicoUser.setUsername("medico1");
		medicoUser.setPassword("medico1");
		medicoUser.setEnabled(true);
		medico.setUser(medicoUser);
		authorities.setUsername(medicoUser.getUsername());
		authorities.setAuthority("medico");

		this.medicoService.medicoCreate(medico);

		return medico;
	}
	
	public Paciente createDummyPaciente(final Medico medico) {
		Paciente paciente = new Paciente();
		paciente.setNombre("Paciente 1");
		paciente.setApellidos("Apellidos");
		paciente.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente.setDNI("12345678A");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(medico);

		this.pacienteService.pacienteCreate(paciente);

		return paciente;
	}
	
	
	@Test
	void testCountWithInitialData() {
		int count = historiaClinicaService.historiaClinicaCount();
		assertEquals(count,3);
	}

	@Test
	void testCreateHistoriaClinica() throws Exception{
		
		Medico medico = createDummyMedico();
		Paciente paciente = createDummyPaciente(medico);

		
		HistoriaClinica hC = new HistoriaClinica();
		hC.setPaciente(paciente);
		hC.setDescripcion("Descripcion 4");
		
		this.historiaClinicaService.saveHistoriaClinica(hC);
		
		int count = this.historiaClinicaService.historiaClinicaCount();
		
		Assertions.assertEquals(count, 4);
		
		
	}
	
	@Test
	void testFindHistoriaClinicaByPaciente() throws Exception{
		Medico medico = createDummyMedico();
		Paciente paciente = createDummyPaciente(medico);
		Paciente paciente2 = createDummyPaciente(medico);
		
		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, 8);
		
		Assertions.assertNotNull(paciente);
		Assertions.assertNotNull(paciente2);
		
		HistoriaClinica hC = new HistoriaClinica();
		hC.setPaciente(paciente);
		hC.setDescripcion("Descripcion 5");
		this.historiaClinicaService.saveHistoriaClinica(hC);
		
		HistoriaClinica hC2 = new HistoriaClinica();
		hC2.setPaciente(paciente2);
		hC2.setDescripcion("Descripcion 6");
		this.historiaClinicaService.saveHistoriaClinica(hC2);
		
		Assertions.assertEquals(this.historiaClinicaService.findHistoriaClinicaByPaciente(paciente), hC);
		Assertions.assertEquals(this.historiaClinicaService.findHistoriaClinicaByPaciente(paciente2), hC2);

	}
	
	@Test
	void testFindHistoriaClinicaByPacienteId() throws Exception{
		Medico medico = createDummyMedico();
		Paciente paciente = createDummyPaciente(medico);
		Paciente paciente2 = createDummyPaciente(medico);
		
		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, 8);
		
		Assertions.assertNotNull(paciente);
		Assertions.assertNotNull(paciente2);
		
		HistoriaClinica hC = new HistoriaClinica();
		hC.setPaciente(paciente);
		hC.setDescripcion("Descripcion 5");
		this.historiaClinicaService.saveHistoriaClinica(hC);
		
		HistoriaClinica hC2 = new HistoriaClinica();
		hC2.setPaciente(paciente2);
		hC2.setDescripcion("Descripcion 6");
		this.historiaClinicaService.saveHistoriaClinica(hC2);
		
		Assertions.assertEquals(this.historiaClinicaService.findHistoriaClinicaByPacienteId(paciente.getId()), hC);
		Assertions.assertEquals(this.historiaClinicaService.findHistoriaClinicaByPacienteId(paciente2.getId()), hC2);
	}
}
