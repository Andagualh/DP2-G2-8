
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Tratamiento;

public interface TratamientoRepository extends CrudRepository<Tratamiento, Integer> {

}
