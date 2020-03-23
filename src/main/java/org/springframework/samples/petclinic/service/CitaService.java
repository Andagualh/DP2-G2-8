
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Paciente;
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
	
	@Transactional
	public int citaCreate(final Cita cita) {
		System.out.println("cita: " + cita);
		return this.citaRepo.save(cita).getId();
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
	
	@Transactional(readOnly = true)
	public Collection<Cita> findCitasByMedicoId(int medicoId) throws DataAccessException{
		Collection<Paciente> pacientes = new ArrayList<>();
		pacientes.addAll(citaRepo.findPacientesByMedicoId(medicoId));
		Collection<Cita> citas = new ArrayList<>();
		for (Paciente p: pacientes ) {
			citas.addAll(citaRepo.findCitasByPacienteId(p.getId()));
		}
		return citas;
	}
	
	@Transactional(readOnly = true)
	public Collection<Cita> findCitasByFecha(String fecha) throws DataAccessException{
		Collection<Cita> citas = new ArrayList<>();
		citas.addAll(citaRepo.findByDate(fecha));
		return citas;
	}
	

}
