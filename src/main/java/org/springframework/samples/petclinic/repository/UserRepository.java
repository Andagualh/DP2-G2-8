
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.User;

public interface UserRepository extends CrudRepository<User, String> {

	Collection<User> findUsersByUsername(String username) throws DataAccessException;

	@Query("SELECT u from User u where u.username = ':username'")
	User findUserByUsername(String username) throws DataAccessException;
}
