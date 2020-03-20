
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

	@Autowired
	private MedicoRepository medicoRepository;


	@Transactional
	public int medicoCreate(final Medico medico) {
		return this.medicoRepository.save(medico).getId();
	}

	@Transactional
	public Collection<Medico> getMedicos() {
		Collection<Medico> medicos = new ArrayList<Medico>();
		this.medicoRepository.findAll().forEach(medicos::add);
		return medicos;
	}

	@Transactional
	public Medico getMedicoById(final int id) {
		return this.medicoRepository.findById(id).get();
	}

	@Transactional
	public int pacienteCount() {
		return (int) this.medicoRepository.count();
	}
}
