
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.repository.InformeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformeService {

	@Autowired
	private InformeRepository		informeRepository;
	private HistoriaClinicaService	historiaClinicaService;


	@Transactional(readOnly = true)
	public Optional<Informe> findInformeById(final int id) throws DataAccessException {
		return this.informeRepository.findById(id);
	}

	@Transactional
	public void saveInforme(final Informe informe) throws DataAccessException, IllegalAccessException {
		if (informe.getCita().getFecha().equals(LocalDate.now())) {
			System.out.println("entra service guardar");
			this.informeRepository.save(informe);
		} else {
			throw new IllegalAccessException();
		}
	}

	@Transactional
	public void addInformeToHistoriaClinica(final Informe informe) throws DataAccessException {
		HistoriaClinica hc = this.historiaClinicaService.findHistoriaClinicaByPacienteId(informe.getCita().getPaciente().getId());
		informe.setHistoriaClinica(hc);
		this.informeRepository.save(informe);
	}

	@Transactional
	public void deleteInformeToHistoriaClinica(final Informe informe) throws DataAccessException {
		informe.setHistoriaClinica(null);
		this.informeRepository.save(informe);
	}
	
	@Transactional
	public void deleteInforme(final int id) {
		this.informeRepository.deleteById(id);
	}
	
}
