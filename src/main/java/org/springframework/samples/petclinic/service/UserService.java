/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class UserService {

	private UserRepository	userRepository;
	private MedicoService	medicoService;


	@Autowired
	public UserService(final UserRepository userRepository, final MedicoService medicoService) {
		this.userRepository = userRepository;
		this.medicoService = medicoService;
	}

	@Transactional
	public int userCount() throws DataAccessException {
		return (int) this.userRepository.count();
	}
	
	@Transactional
	public void saveUser(final User user) throws DataAccessException {
		user.setEnabled(true);
		this.userRepository.save(user);
	}

	@Transactional
	public User getUserByUsername(final String userId) throws DataAccessException {
		return this.userRepository.findById(userId).get();
	}

	@Transactional
	public Optional<User> findUserByUsername(final String userId) throws DataAccessException {
		return this.userRepository.findById(userId);
	}

	@Transactional
	public void acceptUser(final User user) throws DataAccessException {
		user.setEnabled(true);
		this.userRepository.save(user);
	}

	@Transactional
	public void denyUser(final User user) throws DataAccessException {
		user.setEnabled(false);
		this.userRepository.save(user);
	}

	@Transactional
	public Collection<User> getUsers() {
		Collection<User> users = new ArrayList<User>();
		this.userRepository.findAll().forEach(users::add);
		return users;
	}
	
	@Transactional(readOnly = true)
	public Collection<User> findUsersByUsername(final String username) throws DataAccessException {
		return this.userRepository.findUsersByUsername(username);
	}

	@Transactional
	public Medico getCurrentMedico() throws DataAccessException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		Medico medico = this.medicoService.findMedicoByUsername(username);
		return medico;
	}
}
