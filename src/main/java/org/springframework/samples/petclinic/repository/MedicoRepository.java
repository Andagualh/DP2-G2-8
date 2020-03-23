
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.User;

public interface MedicoRepository extends CrudRepository<Medico, Integer> {

	Collection<Medico> findMedicoByApellidos(String apellidos) throws DataAccessException;

	@Query("SELECT m from Medico m where m.user = :user")
	Medico findMedicoByUser(User user) throws DataAccessException;

}
