
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.InformeRepository;
import org.springframework.stereotype.Service;

@Service
public class InformeService {

	@Autowired
	private InformeRepository informeRepository;

}
