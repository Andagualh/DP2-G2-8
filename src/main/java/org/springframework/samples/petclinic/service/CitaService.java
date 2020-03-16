
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.stereotype.Service;

@Service
public class CitaService {

	@Autowired
	private CitaRepository citaRepository;

}
