
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity
public class Medico extends NamedEntity {

	private String	nombre;

	private String	apellidos;

	@Length(min = 9, max = 9)
	private String	DNI;

	@Length(min = 9, max = 9)
	private String	n_telefono;

	private String	domicilio;

	private boolean	activo;
}
