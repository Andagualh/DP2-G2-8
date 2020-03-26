
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Paciente;

public interface CitaRepository extends CrudRepository<Cita, Integer> {

	@Query("SELECT c from Cita c where c.paciente = :paciente")
	Collection<Cita> findCitasByPaciente(Paciente paciente) throws DataAccessException;

}
