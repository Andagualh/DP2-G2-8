
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Paciente;

public interface PacienteRepository extends CrudRepository<Paciente, Integer> {


    
    @Query("SELECT ALL p from Paciente p where p.medico.id =:id")
    Collection<Paciente> findPacientesByMedicoId(@Param("id") int id) throws DataAccessException;
    

}
