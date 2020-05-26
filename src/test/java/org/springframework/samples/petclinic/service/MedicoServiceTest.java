package org.springframework.samples.petclinic.service;

import java.util.Collection;

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
public class MedicoServiceTest {

	@Autowired
	private MedicoService			medicoService;
	
	public Medico createDummyMedico() {
		Medico medico = new Medico();
		User medicoUser = new User();
		Authorities authorities = new Authorities();

		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678Z");
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
	
	@Test
	public void testCountWithInitialData() {
		int countMedicos = this.medicoService.medicoCount();
		Assertions.assertEquals(countMedicos, 4);
	}

	@Test
	public void testCreateMedico() {
		int countMedicos = this.medicoService.medicoCount();
		
		Medico medico = new Medico();
		User medicoUser = new User();
		Authorities authorities = new Authorities();

		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678Z");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");
		medicoUser.setUsername("medico1");
		medicoUser.setPassword("medico1");
		medicoUser.setEnabled(true);
		medico.setUser(medicoUser);
		authorities.setUsername(medicoUser.getUsername());
		authorities.setAuthority("medico");

		this.medicoService.medicoCreate(medico);
		
		int count = this.medicoService.medicoCount();
		Assertions.assertEquals(count, countMedicos + 1);
	}
	
	@Test
	public void testGetMedicoById() {
		Medico medico = this.medicoService.getMedicoById(1);
		Assertions.assertEquals(medico.getId(), 1);
		Assertions.assertEquals(medico.getNombre(), "Alvaro");
		Assertions.assertEquals(medico.getApellidos(), "Alferez");
		Assertions.assertEquals(medico.getDNI(), "78429273D");
		Assertions.assertEquals(medico.getN_telefono(), "666666666");
		Assertions.assertEquals(medico.getDomicilio(), "Ecija");
		Assertions.assertEquals(medico.getUser().getUsername(), "alvaroMedico");
	}
	
	@Test
	public void testGetMedicos() {
		int countMedicos = this.medicoService.medicoCount();

		Collection<Medico> medicos = this.medicoService.getMedicos();
		Assertions.assertEquals(countMedicos, medicos.size());
	}
	
	@Test
	public void testFindMedicoByApellidos() {
		Collection<Medico> medicos = this.medicoService.findMedicoByApellidos("Alferez");
		Assertions.assertEquals(medicos.size(), 1);
	}
	
	@Test
	public void testSaveMedico() {
		int countMedicos = this.medicoService.medicoCount();
		
		Medico medico = new Medico();
		User medicoUser = new User();
		Authorities authorities = new Authorities();

		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678Z");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");
		medicoUser.setUsername("medico1");
		medicoUser.setPassword("medico1");
		medicoUser.setEnabled(true);
		medico.setUser(medicoUser);
		authorities.setUsername(medicoUser.getUsername());
		authorities.setAuthority("medico");

		this.medicoService.saveMedico(medico);
		
		int count = this.medicoService.medicoCount();
		Assertions.assertEquals(count, countMedicos + 1);
	}
	
	@Test
	public void testFindMedicoByUsername() {
		Medico medico = this.medicoService.findMedicoByUsername("alvaroMedico");
		Assertions.assertEquals(medico.getId(), 1);
		Assertions.assertEquals(medico.getNombre(), "Alvaro");
		Assertions.assertEquals(medico.getApellidos(), "Alferez");
		Assertions.assertEquals(medico.getDNI(), "78429273D");
		Assertions.assertEquals(medico.getN_telefono(), "666666666");
		Assertions.assertEquals(medico.getDomicilio(), "Ecija");
		Assertions.assertEquals(medico.getUser().getUsername(), "alvaroMedico");
	}
}
