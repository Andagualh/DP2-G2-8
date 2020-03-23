
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CitaServiceTest {

	@Autowired
	private CitaService		citaService;
	@Autowired
	private PacienteService	pacienteService;
	@Autowired
	private MedicoService	medicoService;


	@Test
	public void testCountWithInitialData() {
		int count = this.citaService.citaCount();
		Assertions.assertEquals(count, 3);
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
		medico.getUser().setEnabled(true);

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

		Cita cita = new Cita();

		cita.setPaciente(this.pacienteService.findPacienteById(idPacienteCreado).get());
		cita.setFecha(LocalDate.of(2020, 05, 26));
		cita.setLugar("Consulta 2");

		this.citaService.save(cita);

		int countCitas = this.citaService.citaCount();

		Assertions.assertEquals(countCitas, 4);

	}

	@Test
	public void testDeleteCita() {

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

		Cita cita = new Cita();

		cita.setPaciente(this.pacienteService.findPacienteById(idPacienteCreado).get());
		cita.setFecha(LocalDate.of(2020, 05, 26));
		cita.setLugar("Consulta 2");

		this.citaService.save(cita);

		Assertions.assertEquals(this.citaService.citaCount(), 4);

		this.citaService.delete(this.citaService.findCitaById(1).get());

		Assertions.assertEquals(this.citaService.citaCount(), 3);

	}

}
