
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Paciente;

public interface PacienteRepository extends CrudRepository<Paciente, Integer> {

}
