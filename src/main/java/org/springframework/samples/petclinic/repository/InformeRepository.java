
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Informe;

public interface InformeRepository extends CrudRepository<Informe, Integer> {

	@Query("DELETE i from Informe i where i.id = :id")
	void deleteInforme(@Param("id") int id) throws DataAccessException;
}
