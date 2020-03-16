package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.repository.springdatajpa.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaService {
	@Autowired
	private CitaRepository citaRepo;
	
	@Transactional
	public int citaCount() {
		return (int) citaRepo.count();
	}
	
	@Transactional
	public Iterable<Cita> findAll() {
		return citaRepo.findAll();
	}
}
