
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.repository.HistoriaClinicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistoriaClinicaService {

	@Autowired
	private HistoriaClinicaRepository	historiaRepository;
	@Autowired
	private PacienteService				pacienteService;


	@Transactional
	public int historiaClinicaCount() {
		return (int) this.historiaRepository.count();
	}

	public void deleteHistoriaClinica(final HistoriaClinica hs) throws DataAccessException {
		this.historiaRepository.delete(hs);
	}

	public HistoriaClinica saveHistoriaClinica(final HistoriaClinica hs) throws DataAccessException {
		HistoriaClinica result = this.historiaRepository.save(hs);
		return result;
	}

	public HistoriaClinica findHistoriaClinicaByPaciente(final Paciente paciente) throws DataAccessException {
		return this.historiaRepository.findHistoriaClinicaByPaciente(paciente);
	}
}
