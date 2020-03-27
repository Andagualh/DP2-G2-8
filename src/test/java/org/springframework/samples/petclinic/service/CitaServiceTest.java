
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CitaServiceTest {

	@Autowired
	private CitaService		citaService;
	@Autowired
	private PacienteService	pacienteService;
	@Autowired
	private MedicoService	medicoService;
	@Autowired
	private HistoriaClinicaService	historiaClinicaService;

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

	public Medico createDummyMedico2() {
		Medico medico = new Medico();
		User medicoUser = new User();
		Authorities authorities = new Authorities();

		medico.setNombre("Medico 2");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678A");
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
		paciente.setDNI("12345678A");
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
		paciente.setDNI("12345678A");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(medico);
		

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
		paciente.setDNI("12345678A");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(medico);
		

		this.pacienteService.pacienteCreate(paciente);

		hs.setPaciente(paciente);
		this.historiaClinicaService.saveHistoriaClinica(hs);

		return paciente;
	}



	@Test
	public void testCountWithInitialData() {
		int count = this.citaService.citaCount();
		Assertions.assertEquals(count, 3);
	}

	@Test
	public void testCreateCita() {

		Medico medico = createDummyMedico();
		Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());

		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, 7);
		Assertions.assertNotNull(this.pacienteService.findPacienteById(paciente.getId()));

		Cita cita = new Cita();

		cita.setPaciente(this.pacienteService.findPacienteById(paciente.getId()).get());
		cita.setFecha(LocalDate.of(2020, 05, 26));
		cita.setLugar("Consulta 2");

		this.citaService.save(cita);

		int countCitas = this.citaService.citaCount();

		Assertions.assertEquals(countCitas, 7);

	}
	//TODO: Test not working, needs to be fixed
	@Test
	public void testDeleteCita() {

		Medico medico = new Medico();
		Paciente paciente = new Paciente();
		

		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678A");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");
		

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

		Assertions.assertEquals(this.citaService.citaCount(), 7);

		this.citaService.delete(this.citaService.findCitaById(1).get());

		Assertions.assertEquals(this.citaService.citaCount(), 6);

	}
	
	@Test
	public void testFindCitasByMedicoId() {
		Medico medico = createDummyMedico();
		Medico medico2 = createDummyMedico2();
		Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
		Paciente paciente2 = createDummyPaciente2(medico2, new HistoriaClinica());
		Paciente paciente3 = createDummyPaciente3(medico, new HistoriaClinica());
		Cita cita = new Cita();
		Cita cita2 = new Cita();
		Cita cita3 = new Cita();
		int count = this.pacienteService.pacienteCount();
		Assertions.assertEquals(count, 9);
		
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

		Assertions.assertEquals(countCita, 9);

		Assertions.assertNotNull(cita.getPaciente().getId());
		Assertions.assertNotNull(cita2.getPaciente().getId());
		Assertions.assertNotNull(cita3.getPaciente().getId());
	
		//Citas of Medico 1
		for(Cita c: this.citaService.findCitasByMedicoId(medico.getId())) {
			Paciente pacienteActual = this.pacienteService.findPacienteById(c.getPaciente().getId()).get();
			//Checking if the cita corresponds to a Paciente of given Medico
			Assertions.assertEquals(pacienteActual.getMedico().getId(), medico.getId());
		}
		//Citas of Medico 2
		for(Cita c: this.citaService.findCitasByMedicoId(medico2.getId())) {
			Paciente pacienteActual2 = this.pacienteService.findPacienteById(c.getPaciente().getId()).get();
			//Checking if the cita corresponds to a Paciente of given Medico
			Assertions.assertEquals(pacienteActual2.getMedico().getId(), medico2.getId());
		}
		
	}

	@Test
	public void TestFindCitaByUnexistingMedicoId(){
		Assertions.assertTrue(citaService.findCitasByMedicoId(12393493).isEmpty());
	}
	
	@Test
	public void findCitaById() {
			
		Medico medico = new Medico();
		medico.setNombre("Medico 1");
		medico.setApellidos("Apellidos");
		medico.setDNI("12345678A");
		medico.setN_telefono("123456789");
		medico.setDomicilio("Domicilio");
		int idMedicoPaciente = this.medicoService.medicoCreate(medico);

		Paciente paciente = new Paciente();
		paciente.setNombre("Paciente 1");
		paciente.setApellidos("Apellidos");
		paciente.setF_nacimiento(LocalDate.of(1996, 01, 12));
		paciente.setDNI("12345678A");
		paciente.setDomicilio("Sevilla");
		paciente.setEmail("paciente@email.com");
		paciente.setF_alta(LocalDate.now());
		paciente.setMedico(this.medicoService.getMedicoById(idMedicoPaciente));

		int idPacienteCreado = this.pacienteService.pacienteCreate(paciente);

		Assertions.assertNotNull(this.pacienteService.findPacienteById(idPacienteCreado));
		
		Cita cita = new Cita();
		cita.setFecha(LocalDate.of(2020, 8, 8));
		cita.setLugar("Sevilla");
		cita.setPaciente(paciente);
		int idCitaCreada = this.citaService.citaCreate(cita);
		
		Cita cita2 = new Cita();
		cita2.setFecha(LocalDate.of(2020, 9, 8));
		cita2.setLugar("Sevilla");
		cita2.setPaciente(paciente);
		int idCitaCreada2 = this.citaService.citaCreate(cita2);

		Assertions.assertNotNull(citaService.findCitaById(idCitaCreada).get().getFecha());
		Assertions.assertNotNull(citaService.findCitaById(idCitaCreada2).get().getFecha());
	
		Assertions.assertEquals(citaService.findCitaById(idCitaCreada).get().getFecha(),cita.getFecha());
		Assertions.assertEquals(citaService.findCitaById(idCitaCreada2).get().getFecha(),cita2.getFecha());
		
	}
	
	@Test
	void shouldFindCitasByDate() {
		Collection<Cita> citas = this.citaService.findCitasByFecha(LocalDate.of(2020,3,9));
		assertThat(citas.size()).isEqualTo(1);
	}

	@Test
	void shouldNotFindCitasByUnexistingCitaDate(){
		Collection <Cita> citas = this.citaService.findCitasByFecha(LocalDate.of(2030, 1, 1));
		assertThat(citas.isEmpty()).isTrue();
	}

}
