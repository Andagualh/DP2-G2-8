
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
		if (informe.getCita().getFecha().equals(LocalDate.now()) && !citaHasInforme(informe.getCita())){
			this.informeRepository.save(informe);
		} else {
			throw new IllegalAccessException("No se puede crear un informe para esta cita");
		}
	}
	
	@Transactional
	public void saveInformeOldDate(final Informe informe) throws DataAccessException, IllegalAccessException {
		this.informeRepository.save(informe);
	}

	@Transactional
	public void updateInforme(final Informe informe) throws DataAccessException, IllegalAccessException{
		if(informe.getCita().getFecha().equals(LocalDate.now()) && citaHasInforme(informe.getCita())){
			this.informeRepository.save(informe);
		} else {
			throw new IllegalAccessException("No se puede editar este informe");
		}
	}

	@Transactional
	public void saveInformeWithHistoriaClinica(final Informe informe) throws DataAccessException, IllegalAccessException {
		this.informeRepository.save(informe);
	}

	@Transactional
	public void deleteInformeToHistoriaClinica(final Informe informe) throws DataAccessException {
		informe.setHistoriaClinica(null);
		this.informeRepository.save(informe);
	}

	@Transactional
	public void deleteInforme(final int id) throws DataAccessException, IllegalAccessException{
		Informe informe = findInformeById(id).get();
		
		if(informe.getCita().getFecha().equals(LocalDate.now()) && informe.getHistoriaClinica() == null){
		this.informeRepository.deleteById(id);
		} else {
			throw new IllegalAccessException("No se puede borrar este informe");
		}
	}

	@Transactional
	public Boolean citaHasInforme (final Cita cita){
		Boolean citaHasInforme;
		if(this.informeRepository.findInformeByCita(cita.getId()) == null){
			citaHasInforme = false;
		} else {
			citaHasInforme = true;
		}
		return citaHasInforme;
	}

}
