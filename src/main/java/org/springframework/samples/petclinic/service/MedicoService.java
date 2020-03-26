
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
	private MedicoRepository	medicoRepository;
	@Autowired
	private UserService			userService;
	@Autowired
	private AuthoritiesService	authoritiesService;


	@Transactional
	public int medicoCreate(final Medico medico) throws DataAccessException {
		return this.medicoRepository.save(medico).getId();
	}

	@Transactional
	public Medico getMedicoById(final int id) throws DataAccessException {
		return this.medicoRepository.findById(id).get();
	}

	@Transactional
	public Collection<Medico> getMedicos() {
		Collection<Medico> medicos = new ArrayList<Medico>();
		this.medicoRepository.findAll().forEach(medicos::add);
		return medicos;
	}

	@Transactional(readOnly = true)
	public Collection<Medico> findMedicoByApellidos(final String apellidos) throws DataAccessException {
		return this.medicoRepository.findMedicoByApellidos(apellidos);
	}

	@Transactional
	public int medicoCount() throws DataAccessException {
		return (int) this.medicoRepository.count();
	}

	@Transactional
	public void saveMedico(final Medico medico) throws DataAccessException {
		this.medicoRepository.save(medico);
		this.userService.saveUser(medico.getUser());
		this.authoritiesService.saveAuthorities(medico.getUser().getUsername(), "medico");
	}

	public Medico findMedicoByUsername(final String username) {
		return this.medicoRepository.findMedicoByUser(this.userService.findUserByUsername(username).get());
	}
}
