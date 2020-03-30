
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Paciente;

public interface HistoriaClinicaRepository extends CrudRepository<HistoriaClinica, Integer> {

	@Query("SELECT h from HistoriaClinica h where h.paciente = :paciente")
	HistoriaClinica findHistoriaClinicaByPaciente(Paciente paciente) throws DataAccessException;

	@Query("SELECT h from HistoriaClinica h where h.paciente.id = :pacienteid")
	HistoriaClinica findHistoriaClinicaByPacienteId(int pacienteid) throws DataAccessException;
}
