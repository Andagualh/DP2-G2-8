package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class InformeServiceTest {

    @Autowired
    private InformeService informeService;
    @Autowired
    private HistoriaClinicaService historiaClinicaService;
    @Autowired
    private MedicoService medicoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private CitaService citaService;

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

        hs.setDescripcion("placeholder");
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

    public Cita createDummyCita1(final Paciente paciente) {
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setFecha(LocalDate.now());
        cita.setLugar("Consulta 1");
        citaService.save(cita);
        return cita;
    }

    public Cita createDummyCitaFuturo(final Paciente paciente) {
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setFecha(LocalDate.now().plusDays(1));
        cita.setLugar("Consulta futura");
        citaService.save(cita);
        return cita;
    }

    @Test
    public void testFindInformeById(){
        //Informe Existente en BD, comprobamos que tiene la ID esperada
        Assertions.assertEquals(1, informeService.findInformeById(1).get().getId());
        //Informe no existente en BD, comprobamos que no existe informe con dicha ID
        Assertions.assertTrue(!informeService.findInformeById(9999).isPresent());
    }

    @Test
    public void testSaveInforme() throws DataAccessException, IllegalAccessException {
        Medico medico = createDummyMedico();
		Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
        Cita cita = createDummyCita1(paciente);
        
        Informe informe = new Informe();
        informe.setCita(cita);
        cita.setInforme(informe);
        informe.setDiagnostico("Dermatitis");
        informe.setMotivo_consulta("Picor en frente");
        Assertions.assertNotNull(informe);
        informeService.saveInforme(informe);

        /*Probamos que la cita existe por su presunto ID en la BD.
        Cambiar el valor si se añaden más informes a Data.sql.
        Me vais a venir a protestar por esto y lo sé, pero 
        es casi el mismo caso que con las counts que 
        se cambian día si y día también.
        Además el método está probado en un test anterior*/
        assertEquals(informe.getId(), informeService.findInformeById(informe.getId()).get().getId());
    }

    @Test
    public void testSaveInformeforNotCurrentDate() throws DataAccessException, IllegalAccessException {
        Medico medico = createDummyMedico();
        Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
        Cita cita = createDummyCitaFuturo(paciente);

        Informe informe = new Informe();
        informe.setCita(cita);
        cita.setInforme(informe);
        informe.setDiagnostico("this will not enter");
        informe.setMotivo_consulta("this will not enter");
        

        IllegalAccessException thrown = assertThrows(IllegalAccessException.class, 
        () -> informeService.saveInforme(informe), "No se puede crear un informe para una cita futura");

        Assertions.assertTrue(thrown.getMessage().contains("No se puede crear un informe para una cita futura"));

    }

    @Test
    public void testSaveInformeWithHC() throws DataAccessException, IllegalAccessException {
        Medico medico = createDummyMedico();
        Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
        Cita cita = createDummyCita1(paciente);

        Informe informe = new Informe();
        informe.setCita(cita);
        cita.setInforme(informe);
        informe.setDiagnostico("Dermatitis");
        informe.setMotivo_consulta("Picor en frente");
        informeService.saveInforme(informe);
        informe.setHistoriaClinica(historiaClinicaService.findHistoriaClinicaByPaciente(paciente));
        Assertions.assertNotNull(informe);

        informeService.saveInformeWithHistoriaClinica(informe);

        Assertions.assertNotNull(informeService.findInformeById(informe.getId()).get().getHistoriaClinica());
        Assertions.assertFalse(informeService.findInformeById(informe.getId()).get().getHistoriaClinica().getDescripcion().isEmpty());
    }

    @Test
    public void testDeleteInformeSuccess() throws DataAccessException, IllegalAccessException{
        Medico medico = createDummyMedico();
		Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
        Cita cita = createDummyCita1(paciente);
        
        Informe informe = new Informe();
        informe.setCita(cita);
        cita.setInforme(informe);
        informe.setDiagnostico("Dermatitis");
        informe.setMotivo_consulta("Picor en frente");
       
        Assertions.assertNotNull(informe);
        Assertions.assertNull(informe.getHistoriaClinica());
        Assertions.assertEquals(LocalDate.now(), cita.getFecha());
        
        informeService.saveInforme(informe);
        informeService.deleteInforme(informe.getId());

        Assertions.assertEquals(Optional.empty(), informeService.findInformeById(informe.getId()));
    }

    @Test
    public void testDeleteInformeWithHC() throws DataAccessException, IllegalAccessException {
        Medico medico = createDummyMedico();
		Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
        Cita cita = createDummyCita1(paciente);
        
        Informe informe = new Informe();
        informe.setCita(cita);
        cita.setInforme(informe);
        informe.setDiagnostico("Dermatitis");
        informe.setMotivo_consulta("Picor en frente");
        informeService.saveInforme(informe);
        informe.setHistoriaClinica(historiaClinicaService.findHistoriaClinicaByPaciente(paciente));
        informeService.saveInformeWithHistoriaClinica(informe);


        Assertions.assertNotNull(informe);
        Assertions.assertNotNull(informe.getHistoriaClinica());
        Assertions.assertEquals(LocalDate.now(), cita.getFecha());
        
        IllegalAccessException thrown = assertThrows(IllegalAccessException.class, 
        () -> informeService.deleteInforme(informe.getId()), "No se puede borrar este informe");

        Assertions.assertTrue(thrown.getMessage().contains("No se puede borrar este informe"));
    }

    @Test
    public void testDeleteInformePast() throws DataAccessException, IllegalAccessException {
        Medico medico = createDummyMedico();
		Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
        
        
        Informe informe = informeService.findInformeById(3).get();

        Assertions.assertNotNull(informe);
        Assertions.assertTrue(LocalDate.now().isAfter(informe.getCita().getFecha()));
        
        IllegalAccessException thrown = assertThrows(IllegalAccessException.class, 
        () -> informeService.deleteInforme(informe.getId()), "No se puede borrar este informe");

        Assertions.assertTrue(thrown.getMessage().contains("No se puede borrar este informe"));
    }

    @Test
    public void testDeleteInformePastHist() throws DataAccessException, IllegalAccessException{
		Paciente paciente = pacienteService.getPacienteById(1);
        Informe informe = informeService.findInformeById(3).get();

        informe.setHistoriaClinica(historiaClinicaService.findHistoriaClinicaByPaciente(paciente));
        informeService.saveInformeWithHistoriaClinica(informe);
        Assertions.assertNotNull(informe);
        Assertions.assertNotNull(informe.getHistoriaClinica());

        IllegalAccessException thrown = assertThrows(IllegalAccessException.class, 
        () -> informeService.deleteInforme(informe.getId()), "No se puede borrar este informe");

        Assertions.assertTrue(thrown.getMessage().contains("No se puede borrar este informe"));
    
    }

    @Test
    public void testDeleteInformeFromHC() throws DataAccessException, IllegalAccessException{
        Medico medico = createDummyMedico();
        Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
        Cita cita = createDummyCita1(paciente);

        Informe informe = new Informe();
        informe.setCita(cita);
        cita.setInforme(informe);
        informe.setDiagnostico("Dermatitis");
        informe.setMotivo_consulta("Picor en frente");
        informeService.saveInforme(informe);
        informe.setHistoriaClinica(historiaClinicaService.findHistoriaClinicaByPaciente(paciente));
        Assertions.assertNotNull(informe);
        informeService.saveInformeWithHistoriaClinica(informe);
        
        Assertions.assertNotNull(informeService.findInformeById(informe.getId()).get().getHistoriaClinica());
        Assertions.assertFalse(informeService.findInformeById(informe.getId()).get().getHistoriaClinica().getDescripcion().isEmpty());

        informeService.deleteInformeToHistoriaClinica(informe);
        Assertions.assertNull(informeService.findInformeById(informe.getId()).get().getHistoriaClinica());

    }
    
}