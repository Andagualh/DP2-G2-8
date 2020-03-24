
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Paciente;

public interface CitaRepository extends CrudRepository<Cita, Integer> {
	
	@Query("SELECT ALL p from Paciente p where p.medico.id =:id")
    Collection<Paciente> findPacientesByMedicoId(@Param("id") int id) throws DataAccessException;
	
	@Query("SELECT ALL c from Cita c where c.paciente.id =:id")
	Collection<Cita> findCitasByPacienteId(@Param("id") int id) throws DataAccessException;
}
