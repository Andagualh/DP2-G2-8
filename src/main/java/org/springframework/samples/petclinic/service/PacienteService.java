
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.samples.petclinic.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository	pacienteRepo;
	@Autowired
	private CitaRepository		citaRepository;


	@Autowired
	public PacienteService(final PacienteRepository pacienteRepo) {
		this.pacienteRepo = pacienteRepo;
	}

	@Transactional
	public Optional<Paciente> existsPacienteById(final int id) {
		return this.pacienteRepo.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Paciente> findPacienteById(final int id) throws DataAccessException {
		return this.pacienteRepo.findById(id);
	}

	@Transactional
	public int pacienteCreate(final Paciente paciente) {
		System.out.println("paciente: " + paciente);
		return this.pacienteRepo.save(paciente).getId();
	}

	@Transactional
	public void pacienteDelete(final int idPaciente) {
		this.pacienteRepo.deleteById(idPaciente);
	}

	@Transactional
	public void savePaciente(final Paciente paciente) {
		this.pacienteRepo.save(paciente);
	}

	@Transactional
	public void savePacienteByMedico(final Paciente paciente, final int idMedico) {
		if (paciente.getMedico().getId() == idMedico) {
			this.pacienteRepo.save(paciente);
		} else {
			throw new IllegalAccessError();
		}
	}

	@Transactional
	public void deletePacienteByMedico(final int idPaciente, final int idMedico) {
		Paciente paciente = this.pacienteRepo.findById(idPaciente).get();
		LocalDate ultimaCita = paciente.getCitas().stream().map(Cita::getFecha).max(LocalDate::compareTo).get();
		LocalDate hoy = LocalDate.now();
		boolean puedeBorrarse = hoy.compareTo(ultimaCita) > 6 || hoy.compareTo(ultimaCita) == 5 && hoy.getDayOfYear() >= hoy.getDayOfYear();

		//Falta historia clinica
		if (paciente.getMedico().getId() == idMedico) {
			if (paciente.getCitas().isEmpty()) {
				this.pacienteRepo.deleteById(idPaciente);
			} else if (puedeBorrarse) {
				this.pacienteRepo.deleteById(idPaciente);
			}
		} else {
			throw new IllegalAccessError();
		}
	}

	@Transactional
	public int pacienteCount() {
		return (int) this.pacienteRepo.count();
	}

	@Transactional(readOnly = true)
	public Collection<Paciente> findPacienteByMedicoId(final int id) throws DataAccessException {
		return this.pacienteRepo.findPacientesByMedicoId(id);
	}
}
