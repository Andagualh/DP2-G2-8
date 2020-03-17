package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.HistoriaClinicaRepository;
import org.springframework.transaction.annotation.Transactional;

public class HistoriaClinicaService {
	
	@Autowired
	private HistoriaClinicaRepository historiaRepo;
	
	@Transactional
	public int historiaClinicaCount() {
		return (int) historiaRepo.count();
	}

}
