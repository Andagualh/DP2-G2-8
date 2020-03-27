
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Paciente;

public interface HistoriaClinicaRepository extends CrudRepository<HistoriaClinica, Integer> {

	HistoriaClinica findHistoriaClinicaByPaciente(Paciente paciente) throws DataAccessException;

}
