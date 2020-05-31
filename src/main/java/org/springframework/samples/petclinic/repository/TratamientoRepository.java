
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Tratamiento;

public interface TratamientoRepository extends CrudRepository<Tratamiento, Integer> {


    @Query("SELECT ALL t from Tratamiento t where t.informe.id = :id")
    Page<Tratamiento> findTratamientoByInforme(int id, Pageable pageable) throws DataAccessException;

}
