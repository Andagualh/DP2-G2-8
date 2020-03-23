
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.HistoriaClinica;

public interface HistoriaClinicaRepository extends CrudRepository<HistoriaClinica, Integer> {

	HistoriaClinica findById(int id) throws DataAccessException;

	@Query("SELECT h from HistoriaClinica h where h.paciente.id =:id")
	HistoriaClinica findHistoriaClinicaByPacienteId(@Param("id") int id) throws DataAccessException;

}
