
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.time.Period;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PacienteServiceTests {

	@Autowired
	private PacienteService			pacienteService;

	@Autowired
	private MedicoService			medicoService;

	@Autowired
	private CitaService				citaService;

	@Autowired
	private HistoriaClinicaService	historiaClinicaService;


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

	public Medico createDummyMedico2() {
		Medico medico = new Medico();
		User medicoUser = new User();
		Authorities authorities = new Authorities();

		medico.setNombre("Medico 2");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678Z");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");
		medicoUser.setUsername("medico2");
		medicoUser.setPassword("medico2");
		medicoUser.setEnabled(true);
		medico.setUser(medicoUser);
		authorities.setUsername(medicoUser.getUsername());
		authorities.setAuthority("medico");

		this.medicoService.medicoCreate(medico);

		return medico;
	}

	public Paciente createDummyPaciente(final Medico medico, final HistoriaClinica hs) {
		Paciente paciente = new Paciente();
		paciente.setNombre("Paciente 1");
		paciente.setApellidos("Apellidos");
		paciente.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente.setDNI("12345678Z");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(medico);

		this.pacienteService.pacienteCreate(paciente);

		hs.setPaciente(paciente);
		this.historiaClinicaService.saveHistoriaClinica(hs);

		return paciente;
	}

	public Paciente createDummyPaciente2(final Medico medico, final HistoriaClinica hs) {
		Paciente paciente = new Paciente();
		paciente.setNombre("Paciente 2");
		paciente.setApellidos("Apellidos");
		paciente.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente.setDNI("12345678Z");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(medico);
		//paciente.setCitas(new ArrayList<Cita>());

		this.pacienteService.pacienteCreate(paciente);

		hs.setPaciente(paciente);
		this.historiaClinicaService.saveHistoriaClinica(hs);

		return paciente;
	}

	public Paciente createDummyPaciente3(final Medico medico, final HistoriaClinica hs) {
		Paciente paciente = new Paciente();
		paciente.setNombre("Paciente 3");
		paciente.setApellidos("Apellidos");
		paciente.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente.setDNI("12345678Z");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(medico);
		//paciente.setCitas(new ArrayList<Cita>());

		this.pacienteService.pacienteCreate(paciente);

		hs.setPaciente(paciente);
		this.historiaClinicaService.saveHistoriaClinica(hs);

		return paciente;
	}

	@Test
	public void testCountWithInitialData() {
		int countPacientes = this.pacienteService.pacienteCount();
		Assertions.assertEquals(countPacientes, 6);
	}

	@Test
	public void testFindPacientesByMedicoId() {
		int countPacientes = this.pacienteService.pacienteCount();
		Medico medico1 = this.createDummyMedico();
		Medico medico2 = this.createDummyMedico2();
		Paciente paciente1 = this.createDummyPaciente(medico1, new HistoriaClinica());
		Paciente paciente2 = this.createDummyPaciente(medico2, new HistoriaClinica());
		Paciente paciente3 = this.createDummyPaciente(medico1, new HistoriaClinica());

		int idMedicoPaciente1 = medico1.getId();
		int idMedicoPaciente2 = medico2.getId();

		int idPaciente1 = paciente1.getId();
		int idPaciente2 = paciente2.getId();
		int idPaciente3 = paciente3.getId();

		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, countPacientes + 3);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPaciente1));
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPaciente2));
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPaciente3));

		for (Paciente p : this.pacienteService.findPacienteByMedicoId(idMedicoPaciente1)) {
			Assertions.assertEquals(p.getMedico().getId(), idMedicoPaciente1);
		}

		for (Paciente p : this.pacienteService.findPacienteByMedicoId(idMedicoPaciente2)) {
			Assertions.assertEquals(p.getMedico().getId(), idMedicoPaciente2);
		}
	}

	@Test
	public void testUpdatePaciente() {
		Medico medico = this.createDummyMedico();
		Paciente paciente = this.createDummyPaciente(medico, new HistoriaClinica());

		int idMedicoPacienteCreado = medico.getId();
		int idPacienteCreado = paciente.getId();

		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());
		paciente.setN_telefono(123456789);
		paciente.setDomicilio("Cordoba");
		paciente.setEmail("email@mail.com");

		this.pacienteService.savePacienteByMedico(paciente, idMedicoPacienteCreado);

		Assert.assertTrue(this.pacienteService.findPacienteById(paciente.getId()).get().getN_telefono().equals(123456789));
		Assert.assertTrue(this.pacienteService.findPacienteById(paciente.getId()).get().getDomicilio().equals("Cordoba"));
		Assert.assertTrue(this.pacienteService.findPacienteById(paciente.getId()).get().getEmail().equals("email@mail.com"));
	}

	@Test
	public void testUpdatePacienteWrongNumero() {
		Medico medico = this.createDummyMedico();
		Paciente paciente = this.createDummyPaciente(medico, new HistoriaClinica());

		int idMedicoPacienteCreado = medico.getId();
		int idPacienteCreado = paciente.getId();

		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());
		paciente.setN_telefono(1);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.pacienteService.savePacienteByMedico(paciente, idMedicoPacienteCreado);
		});
	}

	@Test
	public void testUpdatePacienteWrongDni() {
		Medico medico = this.createDummyMedico();
		Paciente paciente = this.createDummyPaciente(medico, new HistoriaClinica());

		int idMedicoPacienteCreado = medico.getId();
		int idPacienteCreado = paciente.getId();

		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());
		paciente.setDNI("12345678A");

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.pacienteService.savePacienteByMedico(paciente, idMedicoPacienteCreado);
		});
	}

	@Test
	public void testUpdatePacienteWithoutFormaContacto() {
		Medico medico = this.createDummyMedico();
		Paciente paciente = this.createDummyPaciente(medico, new HistoriaClinica());

		int idMedicoPacienteCreado = medico.getId();
		int idPacienteCreado = paciente.getId();

		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());
		paciente.setN_telefono(null);
		paciente.setDomicilio("");
		paciente.setEmail("");

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.pacienteService.savePacienteByMedico(paciente, idMedicoPacienteCreado);
		});
	}

	@Test
	public void testDeletePacienteByMedico() {
		int countPacientes = this.pacienteService.pacienteCount();
		Medico medico = this.createDummyMedico();
		Paciente paciente = this.createDummyPaciente(medico, new HistoriaClinica());

		int idMedicoPacienteCreado = medico.getId();
		int idPacienteCreado = paciente.getId();

		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, countPacientes + 1);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());

		this.pacienteService.deletePacienteByMedico(idPacienteCreado, idMedicoPacienteCreado);
		Assert.assertFalse(this.pacienteService.existsPacienteById(idPacienteCreado).isPresent());
	}

	@Test
	public void testDeletePacienteWrongMedico() {
		int countPacientes = this.pacienteService.pacienteCount();
		Medico medico1 = this.createDummyMedico();
		Medico medico2 = this.createDummyMedico2();
		Paciente paciente = this.createDummyPaciente(medico1, new HistoriaClinica());

		int idMedico2 = medico2.getId();

		int idPacienteCreado = paciente.getId();

		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, countPacientes + 1);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());

		Assertions.assertThrows(IllegalAccessError.class, () -> {
			this.pacienteService.deletePacienteByMedico(idPacienteCreado, idMedico2);
		});
	}

	@Test
	public void testDeletePacienteWithCitasRecientes() {
		int countPacientes = this.pacienteService.pacienteCount();
		Medico medico = this.createDummyMedico();
		Paciente paciente = this.createDummyPaciente(medico, new HistoriaClinica());
		Cita cita = new Cita();
		cita.setFecha(LocalDate.now().minus(Period.ofDays(1)));
		cita.setLugar("Hospital Virgen del Rocio");
		cita.setPaciente(paciente);
		this.citaService.save(cita);

		int idMedicoPacienteCreado = medico.getId();
		int idPacienteCreado = paciente.getId();

		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, countPacientes + 1);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());

		Assertions.assertThrows(IllegalStateException.class, () -> {
			this.pacienteService.deletePacienteByMedico(idPacienteCreado, idMedicoPacienteCreado);
		});
	}

	@Test
	public void testDeletePacienteInactivo() {
		int countPacientes = this.pacienteService.pacienteCount();
		Medico medico = this.createDummyMedico();
		Paciente paciente = this.createDummyPaciente(medico, new HistoriaClinica());
		Cita cita = new Cita();
		cita.setFecha(LocalDate.now().minus(Period.ofYears(6)));
		cita.setLugar("Hospital Virgen del Rocio");
		cita.setPaciente(paciente);
		this.citaService.save(cita);
		int idMedicoPacienteCreado = medico.getId();
		int idPacienteCreado = paciente.getId();

		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, countPacientes + 1);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());

		this.pacienteService.deletePacienteByMedico(idPacienteCreado, idMedicoPacienteCreado);
		Assert.assertFalse(this.pacienteService.existsPacienteById(idPacienteCreado).isPresent());
	}

	@Test
	public void testDeletePacienteWithHistoriaClinica() {
		int countPacientes = this.pacienteService.pacienteCount();
		Medico medico = this.createDummyMedico();
		HistoriaClinica hs = new HistoriaClinica("descripcion historia clinica");
		Paciente paciente = this.createDummyPaciente(medico, hs);

		hs.setPaciente(paciente);
		this.historiaClinicaService.saveHistoriaClinica(hs);

		int idMedicoPacienteCreado = medico.getId();
		int idPacienteCreado = paciente.getId();

		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, countPacientes + 1);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());

		Assertions.assertThrows(IllegalStateException.class, () -> {
			this.pacienteService.deletePacienteByMedico(idPacienteCreado, idMedicoPacienteCreado);
		});
	}

	@Test
	public void testPacienteDeleteAsAdmin() {
		int countPacientes = this.pacienteService.pacienteCount();
		Medico medico = this.createDummyMedico();
		Paciente paciente = this.createDummyPaciente(medico, new HistoriaClinica());

		int idPacienteCreado = paciente.getId();

		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, countPacientes + 1);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado).get());

		this.pacienteService.pacienteDelete(idPacienteCreado);
		Assert.assertFalse(this.pacienteService.existsPacienteById(idPacienteCreado).isPresent());
		//Assertions.assert(this.pacienteService.findPacienteById(idPacienteCreado));
	}
}
