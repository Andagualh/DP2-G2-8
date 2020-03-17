
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
	public PacienteService(PacienteRepository pacienteRepo){
		this.pacienteRepo = pacienteRepo;
	}

	@Transactional
	public Optional<Paciente> existsPacienteById(final int id) {
		return this.pacienteRepo.findById(id);
	}

	@Transactional
	public Paciente findPacienteById(final int id) {
		return this.pacienteRepo.findById(id).get();
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
	public void deletePacienteByMedico(final int idPaciente, final int idMedico) {
		Paciente paciente = this.pacienteRepo.findById(idPaciente).get();

		if (paciente.getMedico().getId() == idMedico) {
			this.pacienteRepo.deleteById(idPaciente);
		} else {
			throw new IllegalAccessError();
		}
	}

	@Transactional
	public int pacienteCount() {
		return (int) this.pacienteRepo.count();
	}

	@Transactional(readOnly = true)
	public Collection<Paciente> findPacienteByMedicoId(int id) throws DataAccessException{
		return pacienteRepo.findPacientesByMedicoId(id);
	}
}
