
package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
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

	@Query("SELECT DISTINCT cita FROM Cita cita WHERE cita.fecha LIKE :fecha")
	Collection<Cita> findByDate(@Param("fecha") LocalDate fecha);

	@Query("SELECT c from Cita c where c.paciente = :paciente")
	Collection<Cita> findCitasByPaciente(Paciente paciente) throws DataAccessException;

	Collection<Cita> deleteAllByPaciente(Paciente paciente) throws DataAccessException;

	@Query("SELECT c from Cita c where c.paciente =:paciente AND c.fecha =:fecha")
	Cita findCitaByPacienteAndFecha(Paciente paciente, LocalDate fecha) throws DataAccessException;
}
