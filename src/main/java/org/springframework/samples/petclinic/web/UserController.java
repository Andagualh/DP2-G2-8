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

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class UserController {

	private static final String	VIEWS_MEDICO_CREATE_FORM	= "users/createMedicoForm";

	@Autowired
	private UserService			userService;
	@Autowired
	private final MedicoService	medicoService;


	@Autowired
	public UserController(final MedicoService medicoService, final UserService userService) {
		this.medicoService = medicoService;
		this.userService = userService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/users/new")
	public String initCreationForm(final Map<String, Object> model) {
		Medico medico = new Medico();
		model.put("medico", medico);
		return UserController.VIEWS_MEDICO_CREATE_FORM;
	}

	@PostMapping(value = "/users/new")
	public String processCreationForm(@Valid final Medico medico, final BindingResult result) {
		if (result.hasErrors()) {
			return UserController.VIEWS_MEDICO_CREATE_FORM;
		} else {
			//creating owner, user, and authority
			this.medicoService.saveMedico(medico);
			System.out.println("medico:" + medico);
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/users/accept/{username}")
	public String acceptUser(@PathVariable("username") final String username, final ModelMap modelMap) {
		String view = "/medicos";
		Optional<User> user = this.userService.findUserByUsername(username);
		if (user.isPresent()) {
			this.userService.acceptUser(user.get());
			modelMap.addAttribute("message", "Usuario acceptado");
			view = "redirect:/medicos";
		} else {
			modelMap.addAttribute("message", "Usuario no encontrado");
		}
		return view;
	}

	@RequestMapping(value = "/users/deny/{username}")
	public String denyUser(@PathVariable("username") final String username, final ModelMap modelMap) {
		String view = "/medicos";
		Optional<User> user = this.userService.findUserByUsername(username);
		if (user.isPresent()) {
			this.userService.denyUser(user.get());
			modelMap.addAttribute("message", "Usuario denegado");
			view = "redirect:/medicos";
		} else {
			modelMap.addAttribute("message", "Usuario no encontrado");
		}
		return view;
	}

	@GetMapping(value = "/users")
	public String processFindForm(User user, final BindingResult result, final Map<String, Object> model) {
		if (user.getUsername() == null) {
			user.setUsername("");
		}

		Collection<User> results;

		if (user.getUsername() == "") {
			results = this.userService.getUsers();
		} else {
			results = this.userService.findUsersByUsername(user.getUsername());
		}

		if (results.isEmpty()) {
			result.rejectValue("apellidos", "notFound", "not found");
			return "medicos/findMedicos";
		} else if (results.size() == 1) {
			user = results.iterator().next();
			return "redirect:/medicos/" + user.getUsername();
		} else {
			model.put("selections", results);
			return "users/usersList";
		}
	}
}
