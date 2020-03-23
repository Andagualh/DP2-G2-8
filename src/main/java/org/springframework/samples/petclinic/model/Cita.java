
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Cita extends NamedEntity {

	@Future
	private LocalDate	fecha;

	private String		lugar;

	@ManyToOne
	@JoinColumn(name = "paciente_id")
	@NotNull
	private Paciente	paciente;

}
