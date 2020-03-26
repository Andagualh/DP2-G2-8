
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Cita extends NamedEntity {

	//@FutureOrPresent
	private LocalDate	fecha;

	private String		lugar;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "paciente_id")
	@NotNull
	private Paciente	paciente;

}
