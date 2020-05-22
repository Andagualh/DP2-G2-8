
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaService {

	private CitaRepository citaRepo;

	@Autowired
	public CitaService(CitaRepository citaRepo) {
		this.citaRepo = citaRepo;
	}

	@Transactional
	public int citaCount() {
		return (int) this.citaRepo.count();
	}

	@Transactional
	public Iterable<Cita> findAll() {
		return this.citaRepo.findAll();
	}

	@Transactional
	public int citaCreate(final Cita cita) {
		return this.citaRepo.save(cita).getId();
	}

	@Transactional
	public Collection<Cita> findAllByPaciente(final Paciente paciente) {
		return this.citaRepo.findCitasByPaciente(paciente);
	}

	@Transactional
	public Cita save(final Cita cita) throws InvalidAttributeValueException {
		if (cita.getFecha().isBefore(LocalDate.now())) {
			throw new InvalidAttributeValueException("No se puede poner una fecha en pasado");
		} else {
			return this.citaRepo.save(cita);
		}
	}

	@Transactional
	public Cita saveOldDate(final Cita cita) {
		return this.citaRepo.save(cita);
	}

	@Transactional
	public void delete(final Cita cita) {
		this.citaRepo.delete(cita);
	}

	@Transactional
	public void deleteAllByPaciente(final Paciente paciente) {
		this.citaRepo.deleteAllByPaciente(paciente);
	}

	@Transactional
	public Optional<Cita> findCitaById(final int citaId) {
		return this.citaRepo.findById(citaId);
	}

	@Transactional(readOnly = true)
	public Collection<Cita> findCitasByMedicoId(final int medicoId) throws DataAccessException {
		Collection<Paciente> pacientes = new ArrayList<>();
		pacientes.addAll(this.citaRepo.findPacientesByMedicoId(medicoId));
		Collection<Cita> citas = new ArrayList<>();
		for (Paciente p : pacientes) {
			citas.addAll(this.citaRepo.findCitasByPacienteId(p.getId()));
		}
		return citas;
	}

	@Transactional(readOnly = true)
	public Collection<Cita> findCitasByFecha(final LocalDate fecha) throws DataAccessException {
		Collection<Cita> citas = new ArrayList<>();
		citas.addAll(this.citaRepo.findByDate(fecha));
		return citas;
	}

	@Transactional(readOnly = true)
	public Boolean existsCitaPacienteDate(final LocalDate fecha, final Paciente paciente) throws DataAccessException {
		Boolean exists = false;
		Cita cita = this.citaRepo.findCitaByPacienteAndFecha(paciente, fecha);
		if(cita != null){
			exists = true;
		}
		return exists;
	}

}
