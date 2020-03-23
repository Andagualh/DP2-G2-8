
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Medico;

public interface MedicoRepository extends CrudRepository<Medico, Integer> {

	Collection<Medico> findMedicoByApellidos(String apellidos) throws DataAccessException;

}
