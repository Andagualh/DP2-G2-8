
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PacienteServiceTests {

	@Autowired
	private PacienteService	pacienteService;

	@Autowired
	private MedicoService	medicoService;


	@Test
	public void testCountWithInitialData() {
		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, 0);
	}

	@Test
	public void testDeletePacienteByMedico() {
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
		Assertions.assertEquals(count, 1);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());

		this.pacienteService.deletePacienteByMedico(idPacienteCreado, idMedicoPaciente);
		System.out.println("resultado: " + this.pacienteService.existsPacienteById(idPacienteCreado));
		Assert.assertFalse(this.pacienteService.existsPacienteById(idPacienteCreado).isPresent());
	}

	@Test
	public void testDeletePacienteByWrongMedico() {
		Medico medico1 = new Medico();
		Medico medico2 = new Medico();
		Paciente paciente = new Paciente();

		medico1.setNombre("Medico 1");
		medico1.setApellidos("Apellidos");
		medico1.setDNI("12345678A");
		medico1.setN_telefono("123456789");
		medico1.setDomicilio("Domicilio");
		medico1.setActivo(true);

		medico2.setNombre("Medico 2");
		medico2.setApellidos("Apellidos");
		medico2.setDNI("12345678A");
		medico2.setN_telefono("123456789");
		medico2.setDomicilio("Domicilio");
		medico2.setActivo(true);

		int idMedico1Paciente = this.medicoService.medicoCreate(medico1);
		int idMedico2 = this.medicoService.medicoCreate(medico2);

		paciente.setNombre("Paciente 1");
		paciente.setApellidos("Apellidos");
		paciente.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente.setDNI("12345678A");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(this.medicoService.getMedicoById(idMedico1Paciente));

		int idPacienteCreado = this.pacienteService.pacienteCreate(paciente);
		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, 1);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());

		Assertions.assertThrows(IllegalAccessError.class, () -> {
			this.pacienteService.deletePacienteByMedico(idPacienteCreado, idMedico2);
		});
	}

	@Test
	public void testPacienteDelete() {
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
		Assertions.assertEquals(count, 1);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());

		this.pacienteService.pacienteDelete(idPacienteCreado);
		System.out.println("resultado: " + this.pacienteService.existsPacienteById(idPacienteCreado));
		Assert.assertFalse(this.pacienteService.existsPacienteById(idPacienteCreado).isPresent());
		//Assertions.assert(this.pacienteService.findPacienteById(idPacienteCreado));
	}
}
