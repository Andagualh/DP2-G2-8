
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.TratamientoRepository;
import org.springframework.stereotype.Service;

@Service
public class TratamientoService {

	@Autowired
	private TratamientoRepository tratamientoRepository;

}
