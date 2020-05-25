package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TratamientoServiceTest {
	
	@Autowired
	private TratamientoService		tratamientoService;
	@Autowired 
	private InformeService   		informeService;
	@Autowired
	private CitaService citaService;
	@Autowired
    private PacienteService pacienteService;
	@Autowired
    private HistoriaClinicaService historiaClinicaService;
	@Autowired
	private MedicoService medicoService;
	
	@Test
	public void testCountWithInitialData() {
		int count = this.tratamientoService.tratamientoCount();
		Assertions.assertEquals(count, 7);
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

		//this.tratamientoService.save(tratamiento);

		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.tratamientoService.save(tratamiento);
		});
		
	}
	
	public Cita createDummyCita1(final Paciente paciente) throws InvalidAttributeValueException {
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setFecha(LocalDate.now());
        cita.setLugar("Consulta 1");
        citaService.save(cita);
        return cita;
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

        hs.setDescripcion("placeholder");
        hs.setPaciente(paciente);
        this.historiaClinicaService.saveHistoriaClinica(hs);

        return paciente;
    }

	
	public Informe createDummyInforme(final Cita cita) throws DataAccessException, IllegalAccessException {
		Informe informe = new Informe();
        informe.setCita(cita);
        informe.setDiagnostico("Dermatitis");
        informe.setMotivo_consulta("Picor en frente");
        informeService.saveInforme(informe);
        return informe;
	}
	
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
	
	@Test
    public void testDeleteTratamientoSuccess() throws DataAccessException, IllegalAccessException, InvalidAttributeValueException{
		
		Tratamiento tratamiento = new Tratamiento();
		Medico medico = createDummyMedico();
		Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
		Cita cita = createDummyCita1(paciente);
		citaService.save(cita);
		Informe informe = new Informe();
		informe.setCita(cita);
		informe.setDiagnostico("Dermatitis");
        informe.setMotivo_consulta("Picor en frente");
        informeService.saveInforme(informe);
        
		
		tratamiento.setMedicamento("aspirina1");
		tratamiento.setDosis("1 pastilla cada 8 horas");
		tratamiento.setF_inicio_tratamiento(LocalDate.now());
		tratamiento.setF_fin_tratamiento(LocalDate.now().plusDays(5));
		tratamiento.setInforme(informe);
		
		LocalDate fecha = tratamiento.getInforme().getCita().getFecha();
		int idMedico = tratamiento.getInforme().getCita().getPaciente().getMedico().getId();
		
		Assertions.assertNotNull(tratamiento);
		Assertions.assertEquals(LocalDate.now(), fecha);
		
		this.tratamientoService.save(tratamiento);
		tratamientoService.deleteTratamiento(tratamiento.getId(), idMedico);
		
		Assertions.assertEquals(Optional.empty(), tratamientoService.findTratamientoById(tratamiento.getId()));
	}

	@Test
    public void testDeleteTratamientoWithWrongDate() throws DataAccessException, IllegalAccessException, InvalidAttributeValueException{
		
		Tratamiento tratamiento = new Tratamiento();
		
		tratamiento.setMedicamento("aspirina1");
		tratamiento.setDosis("1 pastilla cada 8 horas");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-23"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-25"));
		tratamiento.setInforme(informeService.findInformeById(1).get());
		
		tratamientoService.save(tratamiento);
		
		LocalDate fecha = tratamiento.getInforme().getCita().getFecha();
		int idMedico = tratamiento.getInforme().getCita().getPaciente().getMedico().getId();
		
		Assertions.assertNotNull(tratamiento);
		Assertions.assertNotNull(tratamiento.getInforme());
		Assertions.assertNotNull(tratamiento.getInforme().getCita());
		Assertions.assertNotEquals(LocalDate.now(), fecha);
		
		IllegalAccessException thrown = assertThrows(IllegalAccessException.class, 
		        () -> tratamientoService.deleteTratamiento(tratamiento.getId(), idMedico), "No se puede borrar este tratamiento");
		
		Assertions.assertTrue(thrown.getMessage().contains("No se puede borrar este tratamiento"));
	}
	
	@Test
    public void testDeleteTratamientoWithWrongDoctor() throws DataAccessException, IllegalAccessException, InvalidAttributeValueException{
		
		Tratamiento tratamiento = new Tratamiento();
		
		tratamiento.setMedicamento("aspirina1");
		tratamiento.setDosis("1 pastilla cada 8 horas");
		tratamiento.setF_inicio_tratamiento(LocalDate.now());
		tratamiento.setF_fin_tratamiento(LocalDate.now().plusDays(5));
		tratamiento.setInforme(informeService.findInformeById(1).get());
		
		tratamientoService.save(tratamiento);
		
		LocalDate fecha = tratamiento.getInforme().getCita().getFecha();
		int idMedico = tratamiento.getInforme().getCita().getPaciente().getMedico().getId();
		
		Assertions.assertNotNull(tratamiento);
		Assertions.assertNotNull(tratamiento.getInforme());
		Assertions.assertNotNull(tratamiento.getInforme().getCita());
		Assertions.assertNotEquals(LocalDate.now(), fecha);
		
		IllegalAccessException thrown = assertThrows(IllegalAccessException.class, 
		        () -> tratamientoService.deleteTratamiento(tratamiento.getId(), 0), "No se puede borrar este tratamiento");
		
		Assertions.assertTrue(thrown.getMessage().contains("No se puede borrar este tratamiento"));
	}
}
