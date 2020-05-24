
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.repository.TratamientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TratamientoService {

	@Autowired
	private TratamientoRepository	tratamientoRepo;

	
	@Transactional
	public int tratamientoCount() {
		return (int) this.tratamientoRepo.count();
	}

	@Transactional
	public Tratamiento save(final Tratamiento tratamiento) {
		try {
			if(tratamiento.getF_fin_tratamiento().isAfter(tratamiento.getF_inicio_tratamiento())) {
				return this.tratamientoRepo.save(tratamiento);
			}else {
				throw new IllegalArgumentException("la fecha final es anterior a la de inicio.");
			}
		}catch(Exception e) {
			throw new IllegalArgumentException("El tratamiento no ha podido guardarse.");
		}
	}

	@Transactional(readOnly = true)
	public Optional<Tratamiento> findTratamientoById(final int id) throws DataAccessException {
		return this.tratamientoRepo.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Tratamiento> findTratamientosByInforme(final Informe informe) {
		Collection<Tratamiento> tratamientos = new ArrayList<Tratamiento>();

		for (Tratamiento tratamiento : this.tratamientoRepo.findAll()) {
			if (tratamiento.getInforme().equals(informe)) {
				tratamientos.add(tratamiento);
			}
		}

		return tratamientos;
	}

}
