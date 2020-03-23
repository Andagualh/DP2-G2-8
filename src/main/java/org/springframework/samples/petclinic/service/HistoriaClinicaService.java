
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.repository.HistoriaClinicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistoriaClinicaService {

	@Autowired
	private HistoriaClinicaRepository historiaRepo;


	@Transactional
	public int historiaClinicaCount() {
		return (int) this.historiaRepo.count();
	}

	@Autowired
	public HistoriaClinicaService(final HistoriaClinicaRepository historiaRepo) {
		this.historiaRepo = historiaRepo;
	}

	@Transactional(readOnly = true)
	public HistoriaClinica findHistoriaClinicaById(final int id) throws DataAccessException {
		return this.historiaRepo.findById(id);
	}

	@Transactional(readOnly = true)
	public HistoriaClinica findHistoriaClinicaByPacienteId(final int id) throws DataAccessException {
		return this.historiaRepo.findHistoriaClinicaByPacienteId(id);
	}

	@Transactional
	public void save(final HistoriaClinica historiaclinica) {
		this.historiaRepo.save(historiaclinica);
	}

}
