
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Cita extends NamedEntity {

	//@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate	fecha;

	private String		lugar;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	//@JoinColumn(name = "paciente_id")
	private Paciente	paciente;

}
