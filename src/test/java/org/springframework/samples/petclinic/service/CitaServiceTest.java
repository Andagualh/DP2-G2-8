package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CitaServiceTest {
	
	@Autowired
	private CitaService citaService;
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private MedicoService medicoService;
	
	@Test
	public void testCountWithInitialData() {
		int count = citaService.citaCount();
		assertEquals(count,1);
	}
	
	@Test
	public void testCreateCita() {
		Medico medico = new Medico();
		Paciente paciente = new Paciente();

		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678A");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");
		medico.setActivo(true);

		int idMedicoPaciente = this.medicoService.medicoCreate(medico);

		paciente.setNombre("Paciente 1");
		paciente.setApellidos("Apellidos");
		paciente.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente.setDNI("12345678A");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(this.medicoService.getMedicoById(idMedicoPaciente));

		int idPacienteCreado = this.pacienteService.pacienteCreate(paciente);
		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, 2);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado));

		this.pacienteService.deletePacienteByMedico(idPacienteCreado, idMedicoPaciente);
		System.out.println("resultado: " + this.pacienteService.existsPacienteById(idPacienteCreado));
		Assert.assertFalse(this.pacienteService.existsPacienteById(idPacienteCreado).isPresent());
	}
	
	@Test
	public void testFindCitasByMedicoId() {
		Medico medico = new Medico();
		Medico medico2 = new Medico();
		Paciente paciente = new Paciente();
		Paciente paciente2 = new Paciente();
		Paciente paciente3 = new Paciente();
		Cita cita = new Cita();
		Cita cita2 = new Cita();
		Cita cita3 = new Cita();

		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678A");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");
		medico.setActivo(true);
		int idMedicoPaciente = this.medicoService.medicoCreate(medico);
		
		medico2.setNombre("Medico 2");
		medico2.setApellidos("Apellidos");
		medico2.setDNI("12345678A");
		medico2.setN_telefono("123456789");
		medico2.setDomicilio("Domicilio");
		medico2.setActivo(true);
		int idMedicoPaciente2 = this.medicoService.medicoCreate(medico2);

		paciente.setNombre("Paciente 1");
		paciente.setApellidos("Apellidos");
		paciente.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente.setDNI("12345678A");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(this.medicoService.getMedicoById(idMedicoPaciente));

		int idPacienteCreado = this.pacienteService.pacienteCreate(paciente);
		
		paciente2.setNombre("Paciente 2");
		paciente2.setApellidos("Apellidos");
		paciente2.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente2.setDNI("12345678A");
		paciente2.setDomicilio("Sevilla");
		paciente2.setEmail("paciente@email.com");
		paciente2.setF_alta(LocalDate.now());
		paciente2.setMedico(this.medicoService.getMedicoById(idMedicoPaciente2));
		int idPaciente2 = this.pacienteService.pacienteCreate(paciente2);

		paciente3.setNombre("Paciente 3");
		paciente3.setApellidos("Apellidos");
		paciente3.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente3.setDNI("12345678A");
		paciente3.setDomicilio("Sevilla");
		paciente3.setEmail("paciente@email.com");
		paciente3.setF_alta(LocalDate.now());
		paciente3.setMedico(this.medicoService.getMedicoById(idMedicoPaciente));
		int idPaciente3 = this.pacienteService.pacienteCreate(paciente3);
		
		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, 4);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado));
		
		
		cita.setFecha(LocalDate.of(2020, 8, 8));
		cita.setLugar("Sevilla");
		cita.setPaciente(paciente);
		int idCitaCreada = this.citaService.citaCreate(cita);
		
		cita2.setFecha(LocalDate.of(2020, 9, 8));
		cita2.setLugar("Sevilla");
		cita2.setPaciente(paciente2);
		int idCitaCreada2 = this.citaService.citaCreate(cita2);
		
		cita3.setFecha(LocalDate.of(2020, 10, 8));
		cita3.setLugar("Sevilla");
		cita3.setPaciente(paciente3);
		int idCitaCreada3 = this.citaService.citaCreate(cita3);
		
		int countCita = this.citaService.citaCount();
		Assertions.assertEquals(countCita, 4);
		Assertions.assertNotNull(this.citaService.findCitasByMedicoId(idMedicoPaciente));
		Assertions.assertNotNull(cita.getPaciente().getId());
		Assertions.assertNotNull(cita2.getPaciente().getId());
		Assertions.assertNotNull(cita3.getPaciente().getId());
	
		
		for(Cita c: this.citaService.findCitasByMedicoId(idMedicoPaciente)) {
			Paciente pacienteActual = this.pacienteService.findPacienteById(c.getPaciente().getId());
			Assertions.assertEquals(pacienteActual.getMedico().getId(), idMedicoPaciente);
		}
		
		for(Cita c: this.citaService.findCitasByMedicoId(idMedicoPaciente2)) {
			Paciente pacienteActual2 = this.pacienteService.findPacienteById(c.getPaciente().getId());
			Assertions.assertEquals(pacienteActual2.getMedico().getId(), idMedicoPaciente2);
		}
		
	}
	
	@Test
	void shouldFindCitasByDate() {
		Collection<Cita> citas = this.citaService.findCitasByFecha(LocalDate.of(2020, 8, 8));
		assertThat(citas.size()).isEqualTo(1);

		citas = this.citaService.findCitasByFecha(LocalDate.of(2030, 1, 1));
		assertThat(citas.isEmpty()).isTrue();
	}

}
