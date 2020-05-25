package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
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
	private TratamientoRepository tratamientoRepo;

	private boolean esVigente(Tratamiento tratamiento) {
		return tratamiento.getInforme().getCita().getFecha().equals(LocalDate.now());
	}

	private boolean fechaInicioFinOk(Tratamiento tratamiento) {
		if (tratamiento.getF_inicio_tratamiento().equals(tratamiento.getF_fin_tratamiento())
				|| tratamiento.getF_inicio_tratamiento().isBefore(tratamiento.getF_fin_tratamiento())) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public int tratamientoCount() {
		return (int) this.tratamientoRepo.count();
	}

	@Transactional
	public Tratamiento save(final Tratamiento tratamiento) {
		try {
			if (esVigente(tratamiento)) {
				if (fechaInicioFinOk(tratamiento)) {
					return this.tratamientoRepo.save(tratamiento);
				} else {
					throw new IllegalArgumentException("El tratamiento no ha podido guardarse.");
				}
			} else {
				throw new IllegalAccessError("El tratamiento no ha podido guardarse.");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("El tratamiento no ha podido guardarse.");
		}
	}

	@Transactional
	public Tratamiento saveTratamientoOldCita(final Tratamiento tratamiento) {
		return this.tratamientoRepo.save(tratamiento);
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

	@Transactional
	public void deleteTratamiento(final int id, final int idMedico) throws DataAccessException, IllegalAccessException {
		Tratamiento tratamiento = findTratamientoById(id).get();

		if (tratamiento.getInforme().getCita().getPaciente().getMedico().getId() == idMedico
				&& tratamiento.getInforme().getCita().getFecha().equals(LocalDate.now())) {
			this.tratamientoRepo.deleteById(id);
		} else {
			throw new IllegalAccessException("No se puede borrar este tratamiento");
		}
	}

}