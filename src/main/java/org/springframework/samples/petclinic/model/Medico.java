
package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity
@Table(name = "medico")
public class Medico extends BaseEntity {

	@NotEmpty
	private String	nombre;

	@NotEmpty
	private String	apellidos;

	@Length(min = 9, max = 9)
	private String	DNI;

	@Length(min = 9, max = 9)
	private String	n_telefono;

	@NotEmpty
	private String	domicilio;

	//private boolean	activo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	@Valid
	private User	user;
}
