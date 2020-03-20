
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
	public void testFindPacientesByMedicoId(){
		Medico medico = new Medico();
		Medico medico2 = new Medico();
		Paciente paciente = new Paciente();
		Paciente paciente2 = new Paciente();
		Paciente paciente3 = new Paciente();

		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678A");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");
		medico.setActivo(true);
		int idMedicoPaciente1 = this.medicoService.medicoCreate(medico);

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
		paciente.setMedico(this.medicoService.getMedicoById(idMedicoPaciente1));
		int idPaciente1 = this.pacienteService.pacienteCreate(paciente);
		
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
		paciente3.setMedico(this.medicoService.getMedicoById(idMedicoPaciente1));
		int idPaciente3 = this.pacienteService.pacienteCreate(paciente3);

		int countPaciente = this.pacienteService.pacienteCount();
		Assertions.assertEquals(countPaciente, 3);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPaciente1));
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPaciente2));
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPaciente3));
		
		
		for(Paciente p : this.pacienteService.findPacienteByMedicoId(idMedicoPaciente1)){
			Assertions.assertEquals(p.getMedico().getId(), idMedicoPaciente1);
		}

		for(Paciente p : this.pacienteService.findPacienteByMedicoId(idMedicoPaciente2)){
			Assertions.assertEquals(p.getMedico().getId(), idMedicoPaciente2);
		}
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
