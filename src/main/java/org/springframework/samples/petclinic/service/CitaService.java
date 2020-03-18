
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.repository.CitaRepository;
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
	public Iterable<Cita> findAll(){
		return citaRepo.findAll();
	}
	
	//No esta terminada
	@Transactional
	public Iterable<Cita> findAllByMedicoId(int medicoId){
		return citaRepo.findAll();
	}
	
	@Transactional
	public void save(Cita cita){
		citaRepo.save(cita);
	}
	
	@Transactional
	public void delete(Cita cita){
		citaRepo.delete(cita);
	}
	
	@Transactional
	public Optional<Cita> findCitaById(int citaId){
		return citaRepo.findById(citaId);
	}
	

}
