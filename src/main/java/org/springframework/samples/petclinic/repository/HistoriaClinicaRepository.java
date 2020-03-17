package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.HistoriaClinica;

public interface HistoriaClinicaRepository extends CrudRepository<HistoriaClinica,Integer>{}
