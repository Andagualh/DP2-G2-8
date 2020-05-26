package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class UserServiceTest {

	@Autowired
	private UserService			userService;
	
	public User createDummyUser() {
		User user = new User();
		user.setUsername("usuario");
		user.setPassword("password");
		user.setEnabled(true);
		this.userService.saveUser(user);
		
		return user;
	}
	
	@Test
	public void testCountWithInitialData() {
		int countUsers = this.userService.userCount();
		Assertions.assertEquals(countUsers, 8);
	}
	
	@Test
	public void testSaveUser() {
		int countUsers = this.userService.userCount();

		User user = new User();
		user.setUsername("usuario");
		user.setPassword("password");
		user.setEnabled(true);
		this.userService.saveUser(user);
		
		Assertions.assertEquals(this.userService.userCount(), countUsers+1);
	}
	
	@Test
	public void testGetUserByUsername() {
		User user = this.userService.getUserByUsername("alvaroMedico");
		Assertions.assertEquals(user.getUsername(), user.getUsername());
		Assertions.assertEquals(user.getPassword(), user.getPassword());
	}
	
	@Test
	public void testFindUserByUsername() {
		User user = this.userService.findUserByUsername("alvaroMedico").get();
		Assertions.assertEquals(user.getUsername(), user.getUsername());
		Assertions.assertEquals(user.getPassword(), user.getPassword());
	}
	
	@Test
	public void testGetUsers() {
		Collection<User> users = this.userService.getUsers();
		Assertions.assertEquals(this.userService.userCount(), users.size());
	}
	
	@Test
	public void testFindUsersByUsername() {
		Collection<User> users = this.userService.findUsersByUsername("alvaroMedico");
		Assertions.assertEquals(users.size(), 1);
	}
}
