
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

	@Autowired
	private MedicoRepository medicoRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private AuthoritiesService authoritiesService;


	@Transactional
	public int medicoCreate(final Medico medico) throws DataAccessException {
		return this.medicoRepository.save(medico).getId();
	}

	@Transactional
	public Medico getMedicoById(final int id) throws DataAccessException {
		return this.medicoRepository.findById(id).get();
	}

	@Transactional
	public int pacienteCount() throws DataAccessException {
		return (int) this.medicoRepository.count();
	}

	@Transactional
	public void saveMedico(Medico medico) throws DataAccessException{
		medicoRepository.save(medico);
		userService.saveUser(medico.getUser());
		authoritiesService.saveAuthorities(medico.getUser().getUsername(), "medico");
		
	}

	@Transactional
	public Collection<Medico> getMedicos() {
		Collection<Medico> medicos = new ArrayList<Medico>();
		this.medicoRepository.findAll().forEach(medicos::add);
		return medicos;
	}
}
