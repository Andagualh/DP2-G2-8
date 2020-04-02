
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.repository.InformeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformeService {

	@Autowired
	private InformeRepository informeRepository;
	
	@Transactional(readOnly = true)
	public Optional<Informe> findInformeById(final int id) throws DataAccessException {
		return this.informeRepository.findById(id);
	}

}
